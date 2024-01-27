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
    let nuevoMensaje1: Mensaje = {
      autor: "Nombre del Autor1",
      contenido: "Este es el contenido del mensaje",
      timestamp: new Date()
    };

    this.dataService.nickJugadorActual.subscribe(nick => {
      this.nickJugador = nick;
    });

    this.dataService.matchId.subscribe(id => {
      this.matchId = id;
    });

    this.dataService.mensajesActual.subscribe(mensajes => {
      this.mensajes = mensajes;
    });
    
    /*
    this.websocketService.messages.subscribe(msg => {
      const data = JSON.parse(JSON.stringify(msg));
      console.log(data.type);
      switch (data.type) {
        case "MSG":
          let nuevoMensaje: Mensaje = {
            autor: data.nombre,
            contenido: data.msg,
            timestamp: new Date()
          };
          this.mensajes.push(nuevoMensaje);
          break;
      }
    });*/
  }


  enviarMensajeChat (mensaje: string):void {
    let msg = {
      type : "MSG",
      nombre : this.nickJugador,
      matchId : this.matchId,
      contenido: mensaje
    }

    this.websocketService.sendMessage(msg);
  }

}



