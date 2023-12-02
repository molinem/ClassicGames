import { Component, OnInit } from '@angular/core';
import { WebsocketService } from '../websocket.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sala-de-espera',
  templateUrl: './sala-de-espera.component.html',
  styleUrls: ['./sala-de-espera.component.css']
})
export class SalaDeEsperaComponent implements OnInit {

  ws_tablero!: WebsocketService;
  id_partida: string="";
  http_id: string="";

  constructor(private router: Router){
    this.ws_tablero = new WebsocketService
  }

  ngOnInit() : void {
    const navigation = this.router.getCurrentNavigation();
    //const estado = navigation?.extras.state;
    const estado = history.state;
    
    if (estado) {
      this.http_id = estado['http_id'];
      this.id_partida = estado['id_partida'];
      console.log(this.id_partida);
      
      this.ws_tablero.connect("ws://localhost:8080/wsTablero?httpId="+ this.http_id);
      this.ws_tablero.messages.subscribe(msg => {
        console.log(msg);
      });

      
    }
  }

  EstoyListo(): void {
    let msg_ready = {
      type : "PLAYER READY",
      matchId : this.id_partida
    }

    this.ws_tablero.sendMessage(msg_ready);
  }

}
