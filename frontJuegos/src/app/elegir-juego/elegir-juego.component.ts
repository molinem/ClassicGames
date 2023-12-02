import { Component } from '@angular/core';
import { MatchService } from '../match-service.service';
import { raya } from '../raya/raya';
import { WebsocketService } from '../websocket.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-elegir-juego',
  templateUrl: './elegir-juego.component.html',
  styleUrls: ['./elegir-juego.component.css']
})
export class ElegirJuegoComponent {

  ws_tablero!: WebsocketService;
  id_partida: string="";
  http_id: string="";

  constructor(private matchService : MatchService, private router: Router){
    this.ws_tablero = new WebsocketService
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
        this.router.navigate(['/4EnRaya']);
      },
      error => {
          console.log("Se ha producido un error al obtener los mensajes del WebSocket: "+ error)
      },
    );
  }
}
