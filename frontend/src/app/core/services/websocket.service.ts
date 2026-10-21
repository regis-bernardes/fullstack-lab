import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WebsocketService {
  private stompClient: any;
  private execucaoSubject = new Subject<any>();

  connect() {
    const socket = new SockJS('http://localhost:8080/ws-sql');
    this.stompClient = Stomp.over(socket);
    
    // Desativar logs excessivos no console do navegador (opcional)
    this.stompClient.debug = () => {};

    this.stompClient.connect({}, () => {
      console.log('Conectado ao WebSocket STOMP com sucesso!');
      
      // Subscrever ao canal de atualizações de execução
      this.stompClient.subscribe('/topic/execucoes', (message: any) => {
        const dados = JSON.parse(message.body);
        this.execucaoSubject.next(dados);
      });
    }, (error: any) => {
      console.error('Erro na ligação WebSocket:', error);
      setTimeout(() => this.connect(), 5000); // Tentar reconectar após 5s
    });
  }

  getExecucoesUpdates(): Observable<any> {
    return this.execucaoSubject.asObservable();
  }

  disconnect() {
    if (this.stompClient) {
      this.stompClient.disconnect(() => {
        console.log('Desconectado do WebSocket.');
      });
    }
  }
}