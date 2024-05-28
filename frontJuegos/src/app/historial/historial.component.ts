import { Component, OnInit } from '@angular/core';
import { MatchService } from '../match-service.service';

@Component({
  selector: 'app-historial',
  templateUrl: './historial.component.html',
  styleUrls: ['./historial.component.css']
})
export class HistorialComponent implements OnInit {

  history: any[] = [];

  constructor(private matchService: MatchService) {
    
  }

  ngOnInit(): void {
    this.matchService.obtenerhistorialPartidas().subscribe((data) => {
      this.history = data;
    });
  }
}

