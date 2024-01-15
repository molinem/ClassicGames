import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { WebsocketService } from './websocket.service';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  private fuenteDeDato = new BehaviorSubject<string>('Dato inicial');
  datoActual = this.fuenteDeDato.asObservable();
  
  constructor() { }

  cambiarDato(dato: string) {
    this.fuenteDeDato.next(dato);
  }
}
