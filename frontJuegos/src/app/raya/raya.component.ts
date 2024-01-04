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
  es_mi_turno: number;
  notificado: boolean;
  columnaSeleccionada: number = 0;
  mensaje_notificacion: string="";

  ws_tablero!: WebsocketService;

  cadena: String="";
  elementos: string[] = [];

  constructor(private matchService : MatchService, private renderer: Renderer2, private el: ElementRef, private snackBar: MatSnackBar, private router: Router){
    this.partida = new raya
    this.ws_tablero = new WebsocketService
    this.es_mi_turno = 0;
    this.notificado = false;
    this.id_partida_curso = "";
    this.nick_jugador = "";
  }

  ngOnInit() : void {
    const estado = history.state;
    if (!estado || !estado['id_partida']) {
      this.router.navigate(['/ElegirJuego']);
    }else{
      this.ws_tablero.connect("ws://localhost:8080/wsTablero");
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
      /*
      if(data.type == "START"){
        const message = "El jugador "+ data.player_2 + " ha entrado a la partida";
        this.enviarNotificacion(message, 5000);
      }
      */
      switch(data.type){
        case "START":
          const message = "El jugador "+ data.player_2 + " ha entrado a la partida";
          this.enviarNotificacion(message, 5000);
          break;
        case "MATCH UPDATE":
          console.log("Tablero actualizado: "+data.board);
          this.cadena = String(data.board);
          this.elementos = this.cadena.split(',').map(elemento => elemento);

          this.elementos.forEach((elemento, index) => {
            let valor: string;
            switch(elemento) {
              case ' ':
                valor = '.';
                break;
              case 'R':
                valor = 'X';
                break;
              case 'A':
                valor = 'O';
                break;
              default:
                valor = elemento;
                break;
            }
            const fila = Math.floor(index / 7);
            const columna = index % 7;
            this.partida.celdas[fila][columna] = valor;
          });
          break;  
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
        /* 0 -> Es tu turno
		     * 1 -> No es tu turno
		     * 2 -> Partida no lista   
		    */
        this.es_mi_turno = result;
        
        //Es mi turno
        if(this.es_mi_turno == 0){
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
              console.log("[PonerFicha4R] Se ha producido el siguiente error: " + error);
            }
          );
        }else{
          //No es mi turno
          if(this.es_mi_turno == 1){
            this.mensaje_notificacion = "No es tu turno";
            this.enviarNotificacion(this.mensaje_notificacion, 5000);
          }else{
            this.mensaje_notificacion = "La partida no está lista";
            this.enviarNotificacion(this.mensaje_notificacion, 5000);
          }
          
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


