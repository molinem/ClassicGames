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
  cartasMano: Carta[];
  cartasMesa: Carta[];
  cartas1: Carta[];
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
    this.cartasMano=[
      { palo: 0, valor: 0, seleccionada: false },
      { palo: 0, valor: 0, seleccionada: false },
      { palo: 0, valor: 0, seleccionada: false }
    ];

    this.cartasMesa=[
      { palo: 0, valor: 0, seleccionada: false },
      { palo: 0, valor: 0, seleccionada: false },
      { palo: 0, valor: 0, seleccionada: false }
    ];

    this.cartas1=[
      { palo: 0, valor: 0, seleccionada: false },
      { palo: 0, valor: 0, seleccionada: false },
      { palo: 0, valor: 0, seleccionada: false }
    ];

    this.mia=[{ palo: 0, valor: 0, seleccionada: false }];
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
        this.cartasMano = JSON.parse(JSON.stringify(result));
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

  seleccionarCarta(carta: Carta) {
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
            carta.seleccionada = !carta.seleccionada;
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
        this.enviarNotificacion(this.mensaje_notificacion, 5000);
    }
  }

  contarCartasSeleccionadas() {
    return [...this.cartasMano, ...this.cartasMesa].filter(carta => carta.seleccionada).length;
  }

  confirmarSeleccion() {
    const cartasManoSeleccionadas = this.cartasMano.filter(c => c.seleccionada);
    const cartasMesaSeleccionadas = this.cartasMesa.filter(c => c.seleccionada);

    console.log("Cartas mano "+ cartasManoSeleccionadas);
    console.log("Cartas mesa "+ cartasMesaSeleccionadas);

    this.matchService.ponerCarta(this.id_partida_curso, this.mia, this.cartasMesa, cartasMesaSeleccionadas, cartasManoSeleccionadas).subscribe(
      result => {
        let msg_movimiento = {
          type: "MOVEMENTCARTA",
          matchId: this.id_partida_curso
        }
        this.websocketService.ws.send(JSON.stringify(msg_movimiento));
      },
      error => {
        console.log("[PonerCarta] Se ha producido el siguiente error: " + error);
      }
    );
  }


}
