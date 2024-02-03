import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { WebsocketService } from './websocket.service';
import { Mensaje } from './models/mensaje.model';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private fuenteDeDato = new BehaviorSubject<string>('Dato inicial');
  datoActual = this.fuenteDeDato.asObservable();
  
  private nickJugadorSource = new BehaviorSubject<string>('');
  nickJugadorActual = this.nickJugadorSource.asObservable();

  private matchIdSource = new BehaviorSubject<string>('');
  matchId = this.matchIdSource.asObservable();

  private mensajes: Mensaje[] = [];
  mensajesActual = new BehaviorSubject<Mensaje[]>(this.mensajes);

  constructor() { }

  cambiarDato(dato: string) {
    this.fuenteDeDato.next(dato);
  }

  compartirNickJugador(nick: string) {
    this.nickJugadorSource.next(nick);
  }

  compartirMatchId(id: string) {
    this.matchIdSource.next(id);
  }

  inicializarMensajes() {
    this.mensajes = [];
    this.mensajesActual = new BehaviorSubject<Mensaje[]>(this.mensajes);
  }

  addMensaje(mensaje: Mensaje) {
    this.mensajes.push(mensaje);
    this.mensajesActual.next(this.mensajes);
  }
  

}
