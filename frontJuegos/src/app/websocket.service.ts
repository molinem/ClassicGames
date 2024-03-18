import { Injectable } from '@angular/core';
import { Observable, Observer, Subject, map } from 'rxjs';
import { AnonymousSubject } from 'rxjs/internal/Subject';

export interface Message {
  source: string;
  content: string;
}

@Injectable({
  providedIn: 'root'
})

export class WebsocketService {
  
  ws!: WebSocket;
  observador: any
  
  constructor() {
    
  }

  inicializar(){
    let self = this
    this.ws = new WebSocket("ws://localhost:8080/wsTablero")
    
    this.ws.onopen = function name(e) {
        console.log("Websocket conectado")
    }

    this.ws.onmessage = function(e){
       let data = e.data
       console.log(data)
       data = JSON.parse(e.data)
       self.observador.setMessage(data)
    }

    this.ws.onerror = function(e){
        console.log("error en el websocket")
    }

    this.ws.onclose = function(e){
        console.log("websocket cerrado")
    }
  }


}
