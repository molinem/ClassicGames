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

        this.router.navigate(
          ['4EnRaya'], 
          { 
            state: { 
              id_partida: this.id_partida
            } 
          }
        );

      },
      error => {
        console.log("[CrearPartida] -> Se ha producido un error al crear la partida: "+error);
      },
    );
  }
}
