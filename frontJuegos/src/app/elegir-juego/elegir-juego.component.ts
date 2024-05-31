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
  id_partida: string="";
  http_id: string="";
  nick_jugador: string="";

  constructor(private matchService : MatchService, private router: Router, private websocketService: WebsocketService){    
  }

  // get-> /start
  crearPartida4R():void{  
    this.matchService.iniciarPartida4R().subscribe(
      result =>{
        const js = JSON.stringify(result)
        const jsonObj = JSON.parse(js);
        
        this.http_id = jsonObj.httpId;
        this.id_partida = jsonObj.tablero.id;
        this.nick_jugador = jsonObj.nickJugador;

        this.router.navigate(
          ['4EnRaya'], 
          { 
            state: { 
              id_partida: this.id_partida,
              nick_jugador: this.nick_jugador
            } 
          }
        );

      },
      error => {
        if(error.status == 406){
          console.log("[CrearPartida4R] -> No hay créditos suficientes para jugar");
          this.router.navigate(['/RecargarCreditos']);
        }else{
          console.log("[CrearPartida4R] -> Se ha producido un error al crear la partida: "+error);
        }
        
      },
    );
  }

  crearPartidaEscoba():void {
    this.matchService.iniciarPartidaEscoba().subscribe(
      result =>{
        const js = JSON.stringify(result)
        const jsonObj = JSON.parse(js);
        
        this.http_id = jsonObj.httpId;
        this.id_partida = jsonObj.tablero.id;
        console.log(jsonObj.tablero);
        this.nick_jugador = jsonObj.nickJugador;

        this.router.navigate(
          ['Escoba'], 
          { 
            state: { 
              id_partida: this.id_partida,
              nick_jugador: this.nick_jugador
            } 
          }
        );

      },
      error => {
        console.log("[CrearPartidaEscoba] -> Se ha producido un error al crear la partida: "+error);
      },
    );
  }
}
