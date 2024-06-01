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
import { WeatherService } from '../weather.service';
import { LocalStorageService } from '../local-storage.service';
import { IRow } from '../irow';
import { DataService } from '../data.service';
import { Mensaje } from '../models/mensaje.model';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})
export class RayaComponent{

  partida: raya
  columnaHover: number | null = null;
  jugadorActual: string = 'X';
  id_partida_curso: string;
  http_id: string = "";
  nick_jugador: string;
  es_mi_turno: number;
  partida_finalizada: boolean;
  columnaSeleccionada: number = 0;
  mensaje_notificacion: string = "";
  mensaje_tiempo: string = "";

  mostrarChat:boolean;

  cadena: String = "";
  elementos: string[] = [];
  //datoHoy: string| null;
  //datoHistorial: string|null;
  localStorageService: LocalStorageService;
  public dataRow!:IRow[];

  constructor(private matchService: MatchService, private snackBar: MatSnackBar, private router: Router, private weath: WeatherService, localStorageService: LocalStorageService, private websocketService: WebsocketService, private dataService: DataService) {
    this.partida = new raya;
    this.es_mi_turno = 0;
    this.partida_finalizada = false;
    this.id_partida_curso = "";
    this.nick_jugador = "";
    this.mensaje_tiempo = "";
    this.localStorageService = new LocalStorageService;
    this.mostrarChat = false;
    this.websocketService.observador = this;
    this.websocketService.inicializar();
  }

  saveToLocalStorage(clave: string, valor: string) {
    //this.localStorageService.setItem(clave, valor);
  }
  getFromLocalStorage(clave: string){
    //this.localStorageService.getItem(clave);
    this.dataRow=this.localStorageService.getList()
  }

  ngOnInit(): void {
    const estado = history.state;
    if (!estado || !estado['id_partida']) {
      this.router.navigate(['/ElegirJuego']);
    } else {
      this.id_partida_curso = estado['id_partida'];
      this.nick_jugador = estado['nick_jugador'];
      this.dataService.compartirNickJugador(this.nick_jugador);
      this.dataService.compartirMatchId(this.id_partida_curso);
    }
  }

  public desconectar() {
    this.websocketService.ws.close()
    this.matchService.desconectar(this.id_partida_curso).subscribe({
      next: (result) => {
        console.log("Desconectado");
      },
      error: (error) => {
        console.error(error);
      }
    });

    this.dataService.inicializarMensajes();
    this.router.navigate(['/ElegirJuego']);
  }

  public enviarElTiempo() {
    this.weath.obtenerCiudad().subscribe({
      next: (resultadoDeCiudad) => {
        this.mensaje_tiempo = resultadoDeCiudad;
      },
      error: (error) => {
        console.error(error);
      }
    });

    this.weath.obtenerElTiempo().subscribe({
      next: (resultadoDelTiempo) => {
        this.mensaje_tiempo += resultadoDelTiempo;
      },
      error: (error) => {
        console.error(error);
      }
    });

    let msg_weather = {
      type: "WEATHER",
      idPartida: this.id_partida_curso,
      user: this.nick_jugador,
      tiempo: this.mensaje_tiempo,
    }
    this.websocketService.ws.send(JSON.stringify(msg_weather));
  }


  enviarNotificacion(message: string, viewTime: number): void {
    this.snackBar.open(message, '', {
      duration: viewTime,
    });
  }

  actualizarTablero(data: any): void {
    this.cadena = String(data.board);
    this.elementos = this.cadena.split(',').map(elemento => elemento);

    this.elementos.forEach((elemento, index) => {
      let valor: string;
      switch (elemento) {
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
  }

  setMessage(data:any) {
    let message
    switch (data.type) {
      case "START":
        message = "El jugador " + data.player_2 + " ha entrado a la partida";
        this.enviarNotificacion(message, 5000);
        break;
      case "MATCH UPDATE":
        this.actualizarTablero(data);
        if (data.winner !== undefined) {
          this.partida_finalizada = true;
          message = "El jugador " + data.nickWinner + " ha ganado";
          this.enviarNotificacion(message, 5000);
        }
        break;
      case "MSG":
        let nuevoMensaje: Mensaje = {
          autor: data.nombre,
          contenido: data.msg,
          timestamp: new Date().toLocaleTimeString()
        };
        this.dataService.addMensaje(nuevoMensaje);
        break;
    }
    
    
  }

  seleccionarColumna(columnaIndex: number): void {
    this.columnaSeleccionada = columnaIndex;

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
            this.matchService.ponerFicha4R(this.id_partida_curso, this.columnaSeleccionada).subscribe(
              result => {
                let msg_movimiento = {
                  type: "MOVEMENT",
                  col: this.columnaSeleccionada,
                  matchId: this.id_partida_curso
                }
                this.websocketService.ws.send(JSON.stringify(msg_movimiento));
              },
              error => {
                console.log("[PonerFicha4R] Se ha producido el siguiente error: " + error);
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
          console.log("[ObtenerTurnoPartida4R] > Se ha producido un error: " + error);
        }
      );
    } else {
      this.mensaje_notificacion = "La partida ha finalizado";
      /*this.websocketService.messages.subscribe(msg => {
        const data = JSON.parse(JSON.stringify(msg));
        let message = "";
        message = "El jugador " + data.nickWinner + " ha ganado";
      });*/
      this.enviarNotificacion(this.mensaje_notificacion, 5000);
    }

  }


  mouseOverColumna(columnaIndex: number): void {
    this.columnaHover = columnaIndex;
  }

  mouseOutColumna(): void {
    this.columnaHover = null;
  }

}


