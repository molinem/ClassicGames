import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class PaymentsService {

  constructor(private client : HttpClient) { 

  }

  prepay(matches:number):Observable<Object>{
    return this.client.get<any>("http://localhost:8080/payments/autorizarPago?matches="+ matches, {withCredentials:true})
  }

  confirm():Observable<Object>{
    return this.client.get<any>("http://localhost:8080/payments/confirmarPago", {withCredentials:true})
  }

}



