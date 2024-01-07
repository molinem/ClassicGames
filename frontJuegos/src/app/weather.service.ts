import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WeatherService {
  private apiKeyWeather: string = '26B976WW76VUTJA8WNM3MBDRZ';
  private apiKeyCity: string = '6fe339460ff84c099d208718a1886fa4';

  constructor(private http: HttpClient) {}

  public obtenerCiudad(): Observable<string> {
    return new Observable<string>(subscriber => {
      navigator.geolocation.getCurrentPosition(
        position => {
          const url = `https://api.opencagedata.com/geocode/v1/json?q=${position.coords.latitude}+${position.coords.longitude}&key=${this.apiKeyCity}`;
          this.http.get(url).pipe(
            map((response: any) => {
              const components = response.results[0].components;
              const ciudad = components.city || components.town || components.village;
              return 'En '+ciudad;
            })
          ).subscribe({
            next: (result) => subscriber.next(result),
            error: (err) => {
              console.error('Se ha producido un error en la petición al obtener el tiempo', err);
              subscriber.error(err);
            }
          });
        },
        error => {
          console.error(error);
          subscriber.error(error);
        }
      );
    });
  }

  public obtenerElTiempo(): Observable<string> {
    return new Observable<string>(subscriber => {
      navigator.geolocation.getCurrentPosition(
        position => {
          const url = `https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${position.coords.latitude},${position.coords.longitude}?unitGroup=metric&include=current&key=${this.apiKeyWeather}&contentType=json`;
          this.http.get(url).pipe(
            map((response: any) => {
              const tempMax = response.days[0].tempmax;
              const tempMin = response.days[0].tempmin;
              return ` hace una máxima de ${tempMax} ºC y una mínima de ${tempMin} ºC`;
            })
          ).subscribe({
            next: (result) => subscriber.next(result),
            error: (err) => {
              console.error('Se ha producido un error en la petición al obtener el tiempo', err);
              subscriber.error(err);
            }
          });
        },
        error => {
          console.error(error);
          subscriber.error(error);
        }
      );
    });
  }


}

