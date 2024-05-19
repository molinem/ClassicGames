import { Component } from '@angular/core';
import { WebsocketService } from '../websocket.service';
import { MatchService } from '../match-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { BehaviorSubject } from 'rxjs';
import { Mensaje } from '../models/mensaje.model';

@Component({
  selector: 'app-mesa',
  templateUrl: './mesa.component.html',
  styleUrls: ['./mesa.component.css']
})
export class MesaComponent {
  id_partida_curso: string;
  nick_jugador: string;
  mostrarCartasMano: boolean;
  num_jugador: number;

  constructor(private matchService : MatchService, private snackBar: MatSnackBar, private router: Router, private dataService: DataService, private websocketService: WebsocketService){
    this.id_partida_curso = "";
    this.nick_jugador = "";
    this.num_jugador = 0;
    this.mostrarCartasMano = false;
    this.websocketService.observador = this;
    this.websocketService.inicializar();
  }

  enviarNotificacion(message: string, viewTime: number): void {
    this.snackBar.open(message, '', {
      duration: viewTime,
    });
  }

  ngOnInit() : void {
    const estado = history.state;
    if (!estado || !estado['id_partida']) {
      this.router.navigate(['/ElegirJuego']);
    }else{
      this.id_partida_curso = estado['id_partida'];
      this.nick_jugador = estado['nick_jugador'];
      this.dataService.compartirNickJugador(this.nick_jugador);
      this.dataService.compartirMatchId(this.id_partida_curso);
      this.dataService.cambiarDato(this.id_partida_curso);
    }
  }

  ngAfterViewInit() {
    this.matchService.queJugadorSoy(this.id_partida_curso).subscribe(
      result => {
        this.num_jugador = result;
        this.dataService.compartirNumJugador(this.num_jugador);
        if(result == 2){
          this.mostrarCartasMano = true;
        }
      },
      error => {
        console.log("[QueJugadorSoy] Se ha producido el siguiente error: " + error);
      }
    )
  }

  setMessage(data:any) {
    let message = "";
    switch (data.type) {
      case "START":
        message = "El jugador " + data.player_2 + " ha entrado a la partida";
        this.enviarNotificacion(message, 5000);
        this.mostrarCartasMano = true;
        break;
    }
  }

  public desconectar() {
    this.websocketService.ws.close()
    this.dataService.inicializarMensajes();
    this.router.navigate(['/ElegirJuego']);
  }

  public verHistorialPartidas() {}
}
