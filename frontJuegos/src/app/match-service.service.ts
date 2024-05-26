import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Carta } from './models/carta.model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  constructor(private client : HttpClient) {

  }

  //JUEGO 4 en RAYA
  iniciarPartida4R():Observable<any>{
    let info = {
      juego : "Tablero4R"
    }
    
    return this.client.get<any>("http://localhost:8080/matches/start?juego="+info.juego, {withCredentials:true})
  }

  obtenerTurnoPartida(id_partida:string):Observable<any>{
    let info = {
      id : id_partida
    }
    
    return this.client.get<any>("http://localhost:8080/matches/meToca?id="+info.id, {withCredentials:true})
  }

  ponerFicha4R(id_partida:string,columnaIndex:number):Observable<any>{
    let info = {
      id: id_partida,
      col : columnaIndex
    }
    
    return this.client.post<any>("http://localhost:8080/matches/poner",info, {withCredentials:true})
  }


  //JUEGO ESCOBA
  iniciarPartidaEscoba():Observable<any>{
    let info = {
      juego : "Escoba"
    }
    
    return this.client.get<any>("http://localhost:8080/matches/start?juego="+info.juego, {withCredentials:true})
  }

  obtenerManoJugador(id_partida:string):Observable<any>{
    let info1 = {
      id : id_partida
    }

    return this.client.get<any>("http://localhost:8080/matches/obtenerManoJugador?id=" + info1.id, {withCredentials:true})
  }

  obtenerManoMesa(id_partida:string):Observable<any>{
    let info1 = {
      id : id_partida
    }

    return this.client.get<any>("http://localhost:8080/matches/obtenerCartasMesa?id=" + info1.id, {withCredentials:true})
  }

  queJugadorSoy(id_partida:string):Observable<any>{
    let info1 = {
      id : id_partida
    }

    return this.client.get<any>("http://localhost:8080/matches/queJugadorSoy?id=" + info1.id, {withCredentials:true})
  }

  ponerCarta(id_partida:string, miaC:Carta[], mesaC:Carta[], cartasMesaSeleccionadas:Carta[], cartasManoSeleccionadas:Carta[]):Observable<any>{
    let infoCarta = {
      id : id_partida,
      mia : miaC,
      mesa: mesaC,
      cartasMesaSelecc: cartasMesaSeleccionadas,
      cartasManoSelecc: cartasManoSeleccionadas
    }

    return this.client.post<any>("http://localhost:8080/matches/poner",infoCarta, {withCredentials:true})
  }
  
  //Historial de partidas
  obtenerhistorialPartidas():Observable<any>{
    return this.client.get<any>("http://localhost:8080/matches/history", {withCredentials:true})
  }

}
