import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServidorSgbd } from '../models/servidorSgbd.model';

@Injectable({
  providedIn: 'root'
})
export class ServidorService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/servidores';

  //deixado assim para verificar o uso do any e o listar traz todos sem paginacao
  listar(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  criar(servidor: ServidorSgbd): Observable<ServidorSgbd> {
    return this.http.post<ServidorSgbd>(this.apiUrl, servidor);
  }

  atualizar(id: number, servidor: ServidorSgbd): Observable<ServidorSgbd> {
    return this.http.put<ServidorSgbd>(`${this.apiUrl}/${id}`, servidor);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}