import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  constructor(private client : HttpClient) {

  }

  iniciarPartida4R():Observable<any>{
    let info = {
      juego : "Tablero4R"
    }
    
    return this.client.get<any>("http://localhost:8080/matches/start?juego="+info.juego, {withCredentials:true})
  }

  ponerFicha4R(id_partida:string,columnaIndex:number):Observable<any>{
    let info = {
      id: id_partida,
      col : columnaIndex
    }
    
    return this.client.post<any>("http://localhost:8080/matches/poner",info, {withCredentials:true})
  }
  
}
