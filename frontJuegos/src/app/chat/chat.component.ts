import { AfterViewInit, Component } from '@angular/core';
import { Mensaje } from '../models/mensaje.model';
import { ChatService } from '../chat.service';
import { WebsocketService } from '../websocket.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements AfterViewInit {
  mensajes: Mensaje[] = [

  ];

  txtMessage: any;
  ws_chat!: WebsocketService;
  
  ngOnInit() : void {
    this.ws_chat.connect("ws://localhost:8080/wsGames");
  }

  constructor(private chatService: ChatService){
    let nuevoMensaje1: Mensaje = {
      autor: "Nombre del Autor1",
      contenido: "Este es el contenido del mensaje",
      timestamp: new Date()
    };

    this.ws_chat = new WebsocketService;
  }

  ngAfterViewInit() {
    this.ws_chat.messages.subscribe(msg => {
      const data = JSON.parse(JSON.stringify(msg));
      console.log(msg)
      if(data.type == ""){
        
      }
    });
  }


  enviarMensajeChat ():void {
    let msg = {
      type : "IDENT",
      nombre : "Pepe"
    }

    this.ws_chat.sendMessage(msg);
  }

}



