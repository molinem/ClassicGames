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
  id_partida_curso: string;
  nick_jugador: string;
  mostrarCartasMano: boolean;

  constructor(private matchService : MatchService, private snackBar: MatSnackBar, private router: Router, private dataService: DataService, private websocketService: WebsocketService){
    this.id_partida_curso = "";
    this.nick_jugador = "";
    this.mostrarCartasMano = false;
  }

  enviarDato() {
    this.dataService.cambiarDato(this.id_partida_curso);
    
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
      this.websocketService.connect("ws://localhost:8080/wsTablero");
      this.id_partida_curso = estado['id_partida'];
      this.nick_jugador = estado['nick_jugador'];
      this.enviarDato();
    }
  }

  ngAfterViewInit() {
    this.websocketService.messages.subscribe(msg => {
      const data = JSON.parse(JSON.stringify(msg));
      console.log(msg);
      let message = "";
      switch (data.type) {
        case "START":
          message = "El jugador " + data.player_2 + " ha entrado a la partida";
          this.enviarNotificacion(message, 5000);
          this.mostrarCartasMano = true;
          break;
        
      }
    });
    this.matchService.queJugadorSoy(this.id_partida_curso).subscribe(
      result => {
        if(result == 2){
          this.mostrarCartasMano = true;
        }
      },
      error => {
        console.log("[QueJugadorSoy] Se ha producido el siguiente error: " + error);
      }
    )
  }

}
