import { Component } from '@angular/core';
import { raya } from './raya';

@Component({
  selector: 'app-raya',
  templateUrl: './raya.component.html',
  styleUrls: ['./raya.component.css']
})
export class RayaComponent {
  partida:raya

  constructor(){
    this.partida = new raya
  }

  ocuparCelda(row:number, col:number){
    //console.log("fila:" +row + "columna:" +col)
    if(this.puedoPoner()){
      this.partida.celdas[row][col]='X'
    }else{

    }
  }

  puedoPoner():boolean{
    return true
  }

}


