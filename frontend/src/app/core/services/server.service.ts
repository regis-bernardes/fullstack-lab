import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServerConnection } from '../models/server-connection.model';
import { ServidorSgbd } from '../models/servidorSgbd.model';

@Injectable({
  providedIn: 'root'
})
export class ServerService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/servidores'; // Ajuste o endpoint se necessário conforme o seu backend

  getServers(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  createServer(server: ServerConnection): Observable<ServerConnection> {
    return this.http.post<ServerConnection>(this.apiUrl, server);
  }
}