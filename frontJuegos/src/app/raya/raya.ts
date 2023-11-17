export class raya{
    celdas:string[][]

    constructor(){
       this.celdas = Array(6).fill(null).map(() => Array(7).fill('.'));
    }

}