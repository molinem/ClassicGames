import { Component, OnInit } from '@angular/core';
import { Carta } from '../models/carta.model';
import { MatchService } from '../match-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { LocalStorageService } from '../local-storage.service';
import { IRow } from '../irow';
import { WebsocketService } from '../websocket.service';

@Component({
  selector: 'app-mano',
  templateUrl: './mano.component.html',
  styleUrls: ['./mano.component.css']
})
export class ManoComponent implements OnInit{
  cartas1: Carta[];
  cartasMesa: Carta[];
  mia: Carta[];
  id_partida_curso: string;
  nickUsuario: string;
  partida_finalizada: boolean;
  es_mi_turno: number;
  mensaje_notificacion: string = "";
  
  localStorageService: LocalStorageService;
  public dataRow!:IRow[];

  constructor(private matchService : MatchService, private snackBar: MatSnackBar, private router: Router, private dataService: DataService, private websocketService: WebsocketService) {
    this.id_partida_curso = "";
    this.nickUsuario = "";
    this.cartas1=[
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 }
    ];

    this.cartasMesa=[
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 }
    ];

    this.mia=[{ palo: 0, valor: 0 }];
    this.partida_finalizada = false;
    this.es_mi_turno = 0;
    
    this.localStorageService = new LocalStorageService;
  }


  ngOnInit(): void {
    this.dataService.datoActual.subscribe(dato => this.id_partida_curso = dato);
    //this.dataService.wsTablero$.subscribe(data => this.ws_tablero = data);
    this.obtenerCartasMultiple();
  }

  saveToLocalStorage(clave: string, valor: string) {
    this.localStorageService.setItem(clave, valor);
  }
  getFromLocalStorage(clave: string){
    //this.localStorageService.getItem(clave);
    this.dataRow=this.localStorageService.getList()
  }

  obtenerCartasMultiple():void{
    this.matchService.obtenerManoJugador(this.id_partida_curso).subscribe(
      result =>{             
        this.cartas1 = JSON.parse(JSON.stringify(result));
      },
      error => {
        console.log("[ObtenerManoJugador] Se ha producido el siguiente error: " + error);
      }
    );

    this.matchService.obtenerManoMesa(this.id_partida_curso).subscribe(
      result =>{             
        this.cartasMesa = JSON.parse(JSON.stringify(result));
      },
      error => {
        console.log("[obtenerManoMesa] Se ha producido el siguiente error: " + error);
      }
    );
  }

  enviarNotificacion(message: string, viewTime: number): void {
    this.snackBar.open(message, '', {
      duration: viewTime,
    });
  }

  usarCarta(paloObtenido: number, valorObtenido: number) {
    //miaC:Carta[], mesaC:Carta[]
    this.mia=[{ palo: paloObtenido, valor: valorObtenido }];      
    if (!this.partida_finalizada) {
      this.matchService.obtenerTurnoPartida(this.id_partida_curso).subscribe(
        result => {
          /* 0 -> Es tu turno
           * 1 -> No es tu turno
           * 2 -> Partida no lista   
          */
          this.es_mi_turno = result;

          //Es mi turno
          if (this.es_mi_turno == 0) {
            this.matchService.ponerCarta(this.id_partida_curso, this.mia, this.cartasMesa).subscribe(
              result => {
                let msg_movimiento = {
                  type: "MOVEMENTCARTA",
                  matchId: this.id_partida_curso
                }
                this.websocketService.sendMessage(msg_movimiento);
              },
              error => {
                console.log("[PonerCarta] Se ha producido el siguiente error: " + error);
              }
            );
          } else {
            //No es mi turno
            if (this.es_mi_turno == 1) {
              this.mensaje_notificacion = "No es tu turno";
              this.enviarNotificacion(this.mensaje_notificacion, 5000);
            } else {
              this.mensaje_notificacion = "La partida no está lista";
              this.enviarNotificacion(this.mensaje_notificacion, 5000);
            }
          }
        },
        error => {
          console.log("[ObtenerTurnoPartidaEscoba] > Se ha producido un error: " + error);
        }
      );
    } else {
        this.mensaje_notificacion = "La partida ha finalizado";
        this.websocketService.messages.subscribe((msg: any) => {
          const data = JSON.parse(JSON.stringify(msg));
          let message = "";
          message = "El jugador " + data.nickWinner + " ha ganado";
        });
      this.enviarNotificacion(this.mensaje_notificacion, 5000);
    }




  }
}
