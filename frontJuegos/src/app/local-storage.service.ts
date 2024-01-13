import { Injectable } from '@angular/core';
import { HistorialComponent } from './historial/historial.component';
import { IRow } from './irow';

@Injectable({
  providedIn: 'root'
})

export class LocalStorageService {

  constructor() { }

  // Set a value in local storage
  setItem(key: string, value: string): void {
    localStorage.setItem(key, value);
  }
  getList(){
     
     let rowData: IRow[]=[{idPartida:'',ganador:'',location:'',date:''}];
     

    if(localStorage.length>0){
      for(let i = 0 ; i < localStorage.length ; i++){
        if(localStorage.getItem("idPartida")!=null){
          let cad=localStorage.getItem("idPartida");
          let cad1=localStorage.getItem("Ganador");
          let cad2=localStorage.getItem("Location");
          let cad3=localStorage.getItem("Date");
          if(cad){
            
            const jsonString: string = `{"idPartida": ${cad}, "Ganador": ${cad1}, "Location": ${cad2}, "Date": ${cad3}}`;
            //const rowAsString: string = JSON.stringify(rowData[0]);
            const parsedRow: IRow = JSON.parse(jsonString) as IRow;
            console.log("------->"+parsedRow);
            rowData.push(parsedRow);
          }
          
        }
        

      }
    }
    return rowData;
  }

  // Get a value from local storage
  getItem(key: string): string | null {
    return localStorage.getItem(key);
  }

  // Remove a value from local storage
  removeItem(key: string): void {
    localStorage.removeItem(key);
  }

  // Clear all items from local storage
  clear(): void {
    localStorage.clear();
  }
}
