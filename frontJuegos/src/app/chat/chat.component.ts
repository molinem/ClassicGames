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
  mensajes: Mensaje[] = [];

  txtMessage: String;

  ngOnInit() : void {
    
  }

  constructor(private chatService: ChatService, private websocketService: WebsocketService){
    this.txtMessage = "";

  }

  ngAfterViewInit() {
    let nuevoMensaje1: Mensaje = {
      autor: "Nombre del Autor1",
      contenido: "Este es el contenido del mensaje",
      timestamp: new Date()
    };

    
    this.websocketService.messages.subscribe((msg: any) => {
      const datas = JSON.parse(JSON.stringify(msg));
    });
    

  }


  enviarMensajeChat ():void {
    let msg = {
      type : "IDENT",
      nombre : "Pepe"
    }

    this.websocketService.sendMessage(msg);
  }

}



