import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  constructor(private client : HttpClient) {

  }

  prueba():Observable<undefined>{
    /*
    let info = {
      email : usuario.email,
      pwd : usuario.pwd
    }
    */
    return this.client.get<any>("http://localhost:8080/matches/start")
  }
}
