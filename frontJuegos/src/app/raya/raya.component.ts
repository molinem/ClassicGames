import { Component } from '@angular/core';
import { raya } from './raya';
import { MatchService } from '../match-service.service';
import { Observable } from 'rxjs';
import { WebsocketService } from '../websocket.service';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})
export class RayaComponent {
  partida:raya
  columnaHover: number | null = null;
  jugadorActual: string = 'X';
  id_partida: string="";
  http_id: string="";

  ws_tablero!: WebsocketService;

  constructor(private matchService : MatchService){
    this.partida = new raya
    this.ws_tablero = new WebsocketService
  }

  seleccionarColumna(columnaIndex: number): void {
    for (let i = this.partida.celdas.length - 1; i >= 0; i--) {
      if (this.partida.celdas[i][columnaIndex] === '.') {
        this.partida.celdas[i][columnaIndex] = this.jugadorActual;
        this.jugadorActual = this.jugadorActual === 'X' ? 'O' : 'X';
        break;
      }
    }

    this.matchService.ponerFicha4R(this.id_partida,columnaIndex).subscribe(
      result =>{
      console.log(JSON.stringify(result));
      },
      error => {
        alert(error)
      });

    console.log("Columna seleccionada:", columnaIndex);
  }

  mouseOverColumna(columnaIndex: number): void {
    this.columnaHover = columnaIndex;
  }

  mouseOutColumna(): void {
    this.columnaHover = null;
  }

  // get-> /start
  crearPartida4R():void{
    this.matchService.iniciarPartida4R().subscribe(
      result =>{
        const js = JSON.stringify(result)
        const jsonObj = JSON.parse(js);
        
        this.http_id = jsonObj.httpId;
        this.id_partida = jsonObj.tablero.id;

        console.log("http_id = " + this.http_id);
        //console.log(JSON.stringify(result));

        //this.ws_tablero.connect("ws://localhost:8080/wsTablero?httpId="+ this.http_id +"?idPartida="+ this.id_partida);
        this.ws_tablero.connect("ws://localhost:8080/wsTablero?httpId="+ this.http_id);
        this.ws_tablero.messages.subscribe(msg => {
          console.log(msg);
        });
      },
      error => {
          console.log("Se ha producido un error al obtener los mensajes del WebSocket: "+ error)
      },
    );
  }
}


