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
  cartas1: Carta[];
  cartasMesa: Carta[];
  mia: Carta[];
  id_partida_curso: string;
  nickUsuario: string;

  constructor(private matchService : MatchService, private snackBar: MatSnackBar, private router: Router, private dataService: DataService) {
    this.id_partida_curso = "";
    this.nickUsuario = "";
    this.cartas1=[
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 }
    ];

    this.cartasMesa=[
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 },
      { palo: 0, valor: 0 }
    ];

    this.mia=[{ palo: 0, valor: 0 }];
  }

  ngOnInit(): void {
    this.dataService.datoActual.subscribe(dato => this.id_partida_curso = dato);
    this.matchService.obtenerManoJugador(this.id_partida_curso).subscribe(
      result =>{             
        this.cartas1 = JSON.parse(JSON.stringify(result));
      },
      error => {
        console.log("[ObtenerManoJugador] Se ha producido el siguiente error: " + error);
      }
    );

    this.matchService.obtenerManoMesa(this.id_partida_curso).subscribe(
      result =>{             
        this.cartasMesa = JSON.parse(JSON.stringify(result));
      },
      error => {
        console.log("[obtenerManoMesa] Se ha producido el siguiente error: " + error);
      }
    );
  }

  usarCarta(paloObtenido: number, valorObtenido: number) {
    //miaC:Carta[], mesaC:Carta[]
    this.mia=[{ palo: paloObtenido, valor: valorObtenido }];
    this.matchService.ponerCarta(this.id_partida_curso,this.mia, this.cartasMesa).subscribe(
      result =>{
        this.cartasMesa = JSON.parse(JSON.stringify(result));
      },
      error => {
        console.log("[ponerCarta] Se ha producido el siguiente error: " + error);
      }
    );  
  }
}
