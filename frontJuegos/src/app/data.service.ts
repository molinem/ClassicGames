import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

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
