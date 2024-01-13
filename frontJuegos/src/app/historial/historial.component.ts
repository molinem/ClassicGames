

// using-ng-pipe.component

import { UpperCasePipe, CurrencyPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ADTSettings } from 'angular-datatables/src/models/settings';

import { AgGridReact } from 'ag-grid-react'; // React Grid Logic
//import "ag-grid-community/styles/ag-grid.css"; // Core CSS
//import "ag-grid-community/styles/ag-theme-quartz.css"; // Theme

//import css from "styles.css";

//import { Component } from '@angular/core';
import { ColDef } from 'ag-grid-community';
//import 'ag-grid-community/styles/ag-grid.css';
//import 'ag-grid-community/styles/ag-theme-quartz.css';
import { LocalStorageService } from '../local-storage.service';
import { IRow } from '../irow';



// Row Data Interface


@Component({
  selector: 'app-historial',
  templateUrl: './historial.component.html',
  styleUrls: ['./historial.component.css']
})
export class HistorialComponent implements OnInit {
  public info!: LocalStorageService;
  public lst!:string[];
  public colDefs: ColDef[] = [
    { field: 'idPartida' },
    // Using dot notation to access nested property
    { field: 'Ganador'},
    // Show default header name
    { field: 'Location' },
    {field: 'Date'}
  ];
  public rowData: IRow[] | null;
  themeClass =
    "ag-theme-quartz";



  /*rowData1: IRow[] = [
    {
      idPartida: "123",
      ganador: "aaa",
      location:"toledo",
      date:"hoy"
    }
  ];
  */
  //rowData: IRow[]=fillRow(this.rowData1)
 
    
  
  /*colDefs: ColDef<IRow>[] = [
    { field: 'athlete' },
    { field: 'sport' },
    { field: 'age' },
  ];*/
  
    

    
  
  constructor(info:LocalStorageService){
    info=new LocalStorageService;
    this.lst=[];
    this.rowData=this.info.getList();
    

    /*this.colDefs= [
    { idPartida: 'idPartida' },
    { ganador: '"Ganador"' },
    { field: 'location' },
    { field: 'date' },
    
  ];*/

  
  }
  ngOnInit(): void {
    //this.obtenerHistorial();
    //this.rowData!.forEach(fruit => {
      //console.log(fruit);
  //});

  //let longFruits = this.lst.filter(fruit => fruit.length > 5);
  }
  obtenerHistorial() {
    //this.lst=this.info.getList("Ganador");
  }

  

  // Row Data: The data to be displayed.
  rellenarFila(){
    
    this.lst.forEach(fruit => {
      console.log(fruit)
  });
  }
  /*
  rowData: IRow[] = [
    {
      jugador1: "",
  jugador2: "",
  partida: "",
  location: "",
  date: "",
  ganador: ""
    },
    
  ];
  */

  // Column Definitions: Defines & controls grid columns.
  /*colDefs: ColDef<IRow>[] = [
    { field: "IdPartida" },
    { field: "Ganador" },
    { field: 'location' },
    { field: 'date' },
    
  ];*/
}

function fillRow(rowData1: IRow[]): IRow[] {
  throw new Error('Function not implemented.');
}

