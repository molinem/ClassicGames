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
  ngOnInit() : void {
    
  }

  constructor(private chatService: ChatService, private websocketService: WebsocketService){
    let nuevoMensaje1: Mensaje = {
      autor: "Nombre del Autor1",
      contenido: "Este es el contenido del mensaje",
      timestamp: new Date()
    };

  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.websocketService.messages.subscribe((msg: any) => {
        const data = JSON.parse(JSON.stringify(msg));
        console.log(data);
      });
    }, 5000);

  }


  enviarMensajeChat ():void {
    let msg = {
      type : "IDENT",
      nombre : "Pepe"
    }

    this.websocketService.sendMessage(msg);
  }

}



