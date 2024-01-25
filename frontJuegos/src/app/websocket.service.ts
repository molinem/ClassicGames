import { Injectable } from '@angular/core';
import { Observable, Observer, Subject, map } from 'rxjs';
import { AnonymousSubject } from 'rxjs/internal/Subject';

export interface Message {
  source: string;
  content: string;
}

@Injectable({
  providedIn: 'root'
})

export class WebsocketService {
  
  private subject!: AnonymousSubject<MessageEvent>;
  public messages!: Subject<Message>;
  private ws!: AnonymousSubject<MessageEvent>;
  
  constructor() { }

  public connect(url: string): AnonymousSubject<MessageEvent> {
        if (!this.subject) {
            this.subject = this.create(url);
            this.messages = <Subject<Message>>this.subject.pipe(
                map(
                    (response: MessageEvent): Message => {
                        //console.log(response.data);
                        const data = JSON.parse(response.data);
                        return data;
                    },
                ),
            );
            console.log('Conexión realizada correctamente: ' + url);
        }
        this.ws = this.subject;
        return this.subject;
    }

    private create(url: string): AnonymousSubject<MessageEvent> {
        const ws = new WebSocket(url);
        const observable = new Observable((obs: Observer<MessageEvent>) => {
            ws.onmessage = obs.next.bind(obs);
            ws.onerror = obs.error.bind(obs);
            ws.onclose = obs.complete.bind(obs);
            return ws.close.bind(ws);
        });
        const observer = {
            next: (data: Object) => {
                console.log('Message sent to websocket: ', data);
                if (ws.readyState === WebSocket.OPEN) {
                    ws.send(JSON.stringify(data));
                }
            },
            error: (err: any) => {
              console.error('WebSocket error:', err);
            },
            complete: () => {
                console.log('WebSocket connection completed');
            },
        };
        return new AnonymousSubject<MessageEvent>(observer, observable);
    }

    public sendMessage(message: any): void {
        if (this.subject) {
            this.subject.next(message);
        } else {
            console.warn('WebSocket not connected, cannot send message');
        }
    }

    public disconnect():void {
        if (this.subject) {
            this.subject.complete();
            this.subject = new AnonymousSubject<MessageEvent>();
        } else {
            console.warn('WebSocket not connected, cannot disconnect');
        }
    }
}
