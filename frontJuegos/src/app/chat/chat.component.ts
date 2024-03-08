import { AfterViewInit, Component } from '@angular/core';
import { Mensaje } from '../models/mensaje.model';
import { ChatService } from '../chat.service';
import { WebsocketService } from '../websocket.service';
import { DataService } from '../data.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements AfterViewInit {
  mensajes: Mensaje[] = [];
  txtMessage: string;
  nickJugador: string;
  matchId: string;

  ngOnInit() : void {
  }

  constructor(private chatService: ChatService, private websocketService: WebsocketService, private dataService: DataService){
    this.txtMessage = "";
    this.nickJugador = "";
    this.matchId = "";
  }

  ngAfterViewInit() {
    this.dataService.nickJugadorActual.subscribe(nick => {
      this.nickJugador = nick;
    });

    this.dataService.matchId.subscribe(id => {
      this.matchId = id;
    });

    this.dataService.mensajesActual.subscribe(mensajes => {
      this.mensajes = mensajes;
    });
  }


  enviarMensajeChat (mensaje: string):void {
    let msg = {
      type : "MSG",
      nombre : this.nickJugador,
      matchId : this.matchId,
      contenido: mensaje
    }

    this.txtMessage = '';
    this.websocketService.ws.send(JSON.stringify(msg));
  }

}



