import { Component } from '@angular/core';
import { WeatherService } from './weather.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontJuegosPrueba';
  ws? :WebSocket
  position? : GeolocationPosition
  weath! : WeatherService

  constructor(){
    
  }

  ngOnInit(){
    //this.weath.obtenerElTiempo();
  }
}
