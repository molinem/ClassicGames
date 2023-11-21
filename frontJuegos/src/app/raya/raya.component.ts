import { Component } from '@angular/core';
import { raya } from './raya';
import { MatchService } from '../match-service.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})
export class RayaComponent {
  partida:raya
  columnaHover: number | null = null;
  jugadorActual: string = 'X';

  constructor(private matchService : MatchService){
    this.partida = new raya
  }

  seleccionarColumna(columnaIndex: number): void {
    for (let i = this.partida.celdas.length - 1; i >= 0; i--) {
      if (this.partida.celdas[i][columnaIndex] === '.') {
        this.partida.celdas[i][columnaIndex] = this.jugadorActual;
        this.jugadorActual = this.jugadorActual === 'X' ? 'O' : 'X';
        break;
      }
    }
    //Llamada back-end //to-be-continued
    console.log("Columna seleccionada:", columnaIndex);
  }

  mouseOverColumna(columnaIndex: number): void {
    this.columnaHover = columnaIndex;
  }

  mouseOutColumna(): void {
    this.columnaHover = null;
  }


  crearPartida4R():void{
    this.matchService.iniciarPartida4R().subscribe(
      result =>{
        console.log(JSON.stringify(result));
      },
      error => {
        alert(error)
      });
  }
}


