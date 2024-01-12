import { Component, OnInit } from '@angular/core';
import { Carta } from '../models/carta.model';
import { MatchService } from '../match-service.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-mano',
  templateUrl: './mano.component.html',
  styleUrls: ['./mano.component.css']
})
export class ManoComponent implements OnInit{
  cartas1: Carta[] = [
    { palo: 1, valor: 9 },
    { palo: 3, valor: 4 },
    { palo: 2, valor: 8 }
  ];
  id_partida_curso: string;
  nickUsuario: string;

  //Jugador 1 [cartas_1] ve solo sus cartas
  constructor(private matchService : MatchService, private snackBar: MatSnackBar, private router: Router, private dataService: DataService) {
    this.id_partida_curso = "";
    this.nickUsuario = "";
  }

  ngOnInit(): void {
    this.dataService.datoActual.subscribe(dato => this.id_partida_curso = dato);
    this.matchService.obtenerManoJugador(this.id_partida_curso).subscribe(
      result =>{             
        console.log(result);
      },
      error => {
        console.log("[ObtenerManoJugador] Se ha producido el siguiente error: " + error);
      }
    );
  }

}
