import { Component } from '@angular/core';
import { raya } from './raya';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})
export class RayaComponent {
  partida:raya
  columnaHover: number | null = null;
  jugadorActual: string = 'X';

  constructor(){
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
    //Llamada back-end
    console.log("Columna seleccionada:", columnaIndex);
  }

  mouseOverColumna(columnaIndex: number): void {
    this.columnaHover = columnaIndex;
  }

  mouseOutColumna(): void {
    this.columnaHover = null;
  }

}


