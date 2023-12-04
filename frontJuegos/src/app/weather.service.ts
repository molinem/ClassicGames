import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  position? : GeolocationPosition;
  tempMax ?: number
  tempMin ?: number

  constructor() {

  }

  public obtenerElTiempo() : void{
    let self = this
    navigator.geolocation.getCurrentPosition(
      position => {
        this.position = position
        console.log("Latitud: " + position.coords.latitude + " Longitud: " + position.coords.longitude);
        let url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/38.9903762%2C%20-3.9203192?unitGroup=metric&include=current&key=G94RAC9R3W3GNMLK7B9B8Q24B&contentType=json"
        //let url = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/"+ position.coords.latitude + "%2C%20" + position.coords.longitude + " ?unitGroup=metric&include=current&key=26B976WW76VUTJA8WNM3MBDRZ&contentType=json"
        let req = new XMLHttpRequest();

        req.onreadystatechange = function (){
          if(this.readyState == 4 ){
            if(this.status >= 200 && this.status < 400){
              console.log("Ejecución correcta")
              let response = req.response
              response = JSON.parse(response)

              self.tempMax = response.days[0].tempmax
              self.tempMin = response.days[0].tempmin
              console.log("TemperaturaMax: " + self.tempMax)
              console.log("TemperaturaMin: " + self.tempMin)
            }else{
              console.log("Se ha producido un error en la petición al obtener el tiempo")
            }
          }
        }

        req.open("GET",url)
        req.send()
      },

      error => {
        console.log(error);
      })

    
  }

}
