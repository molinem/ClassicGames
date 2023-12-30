import { Component } from '@angular/core';
import { Mensaje } from '../models/mensaje.model';
import { ChatService } from '../chat.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent {
  mensajes: Mensaje[] = [
    // ... aquí irían los mensajes obtenidos de alguna manera
  ];
  txtMessage: any;
  constructor(private chatService: ChatService){

  }
  sendMessage ():void {
    
  }

}



