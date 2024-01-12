import { Component } from '@angular/core';
import { WebsocketService } from '../websocket.service';
import { MatchService } from '../match-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-mesa',
  templateUrl: './mesa.component.html',
  styleUrls: ['./mesa.component.css']
})
export class MesaComponent {
  ws_tablero!: WebsocketService;
  id_partida_curso: string;
  nick_jugador: string;


  constructor(private matchService : MatchService, private snackBar: MatSnackBar, private router: Router, private dataService: DataService){
    this.ws_tablero = new WebsocketService;
    this.id_partida_curso = "";
    this.nick_jugador = "";
  }

  enviarDato() {
    this.dataService.cambiarDato(this.id_partida_curso);
  }

  ngOnInit() : void {
    const estado = history.state;
    if (!estado || !estado['id_partida']) {
      this.router.navigate(['/ElegirJuego']);
    }else{
      this.ws_tablero.connect("ws://localhost:8080/wsTablero");
      this.id_partida_curso = estado['id_partida'];
      this.nick_jugador = estado['nick_jugador'];
      this.enviarDato();
    }
  }


}
