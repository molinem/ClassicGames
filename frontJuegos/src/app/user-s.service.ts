import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { user } from './user/user';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserSService {

  private userNick = new BehaviorSubject<string>('');
  currentNick = this.userNick.asObservable();
  
  constructor(private client : HttpClient) {

  }

  changeNick(nick: string) {
    this.userNick.next(nick);
  }

  registrarUsuario(usuario:user):Observable<undefined>{
    let info = {
      nombre : usuario.nombre,
      email : usuario.email,
      pwd1 : usuario.pwd1,
      pwd2 : usuario.pwd2
    }

    return this.client.post<any>("http://localhost:8080/users/register",info)
  }

  logearUsuario(usuario:user):Observable<undefined>{
    let info = {
      email : usuario.email,
      pwd : usuario.pwd
    }

    return this.client.post<any>("http://localhost:8080/users/login",info,{withCredentials:true})
  }

  cerrarSesion():Observable<undefined>{
    return this.client.get<any>("http://localhost:8080/users/logout", {withCredentials:true})
  }
}
