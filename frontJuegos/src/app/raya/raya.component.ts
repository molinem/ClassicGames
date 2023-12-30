import { AfterViewInit, Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { raya } from './raya';
import { MatchService } from '../match-service.service';
import { WebsocketService } from '../websocket.service';
import { Router, NavigationStart } from '@angular/router';
import {
  MatSnackBar,
  MatSnackBarAction,
  MatSnackBarActions,
  MatSnackBarLabel,
  MatSnackBarRef,
} from '@angular/material/snack-bar';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})
export class RayaComponent implements AfterViewInit {
  @ViewChild('tablero') miDiv!: ElementRef<HTMLDivElement>;
  
  partida:raya
  columnaHover: number | null = null;
  jugadorActual: string = 'X';
  id_partida_curso: string;
  http_id: string="";
  nick_jugador: string;
  es_mi_turno: boolean;
  notificado: boolean;
  columnaSeleccionada: number = 0;

  ws_tablero!: WebsocketService;


  constructor(private matchService : MatchService, private renderer: Renderer2, private el: ElementRef, private snackBar: MatSnackBar, private router: Router){
    this.partida = new raya
    this.ws_tablero = new WebsocketService
    this.es_mi_turno = false;
    this.notificado = false;
    this.id_partida_curso = "";
    this.nick_jugador = "";
  }

  ngOnInit() : void {
    this.ws_tablero.connect("ws://localhost:8080/wsTablero");
    const estado = history.state;
    if (estado) {
      this.id_partida_curso = estado['id_partida'];
    }
  }

  enviarNotificacion(message: string, viewTime: number): void{
    this.snackBar.open(message,'', {
      duration: viewTime,
    });
  }

  ngAfterViewInit() {
    this.ws_tablero.messages.subscribe(msg => {
      const data = JSON.parse(JSON.stringify(msg));
      console.log(msg)
      if(data.type == "START"){
        const message = "El jugador "+ data.player_2 + " ha entrado a la partida";
        this.enviarNotificacion(message, 5000);
      }
    });
  }

  
  seleccionarColumna(columnaIndex: number): void {

    /*
    for (let i = this.partida.celdas.length - 1; i >= 0; i--) {
      if (this.partida.celdas[i][columnaIndex] === '.') {
        this.partida.celdas[i][columnaIndex] = this.jugadorActual;
        this.jugadorActual = this.jugadorActual === 'X' ? 'O' : 'X';
        break;
      }
    }*/
    
    this.columnaSeleccionada = columnaIndex;

    this.matchService.obtenerTurnoPartida4R(this.id_partida_curso).subscribe(
      result =>{
        if(result){
          this.es_mi_turno = true;
        }else{
          this.es_mi_turno = false;
        }

        if(this.es_mi_turno == true){
          this.matchService.ponerFicha4R(this.id_partida_curso,this.columnaSeleccionada).subscribe(
            result =>{
              console.log(JSON.stringify(result));
              
              let msg_movimiento = {
                type : "MOVEMENT",
                col: this.columnaSeleccionada,
                matchId : this.id_partida_curso
              }
              this.ws_tablero.sendMessage(msg_movimiento);
              //Pintar movimiento de este jugador
            },
            error => {
              alert(error)
            }
          );
        }else{
          const message = "No es tu turno";
          this.enviarNotificacion(message, 5000);
        }
      },
      error => {
        console.log("[ObtenerTurnoPartida4R] > Se ha producido un error: "+ error);
      }
    );
  }


  mouseOverColumna(columnaIndex: number): void {
    this.columnaHover = columnaIndex;
  }

  mouseOutColumna(): void {
    this.columnaHover = null;
  }

}


