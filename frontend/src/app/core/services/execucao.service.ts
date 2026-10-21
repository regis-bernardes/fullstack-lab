import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ExecucaoRequest, ExecucaoResponse } from '../models/execucao.model';

@Injectable({
  providedIn: 'root'
})
export class ExecucaoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/execucoes';

  disparar(request: ExecucaoRequest): Observable<ExecucaoResponse> {
    return this.http.post<ExecucaoResponse>(this.apiUrl, request);
  }

  listar(): Observable<ExecucaoResponse[]> {
    return this.http.get<ExecucaoResponse[]>(this.apiUrl);
  }

  findByIdExecucao(id: number): Observable<ExecucaoResponse> {
    return this.http.get<ExecucaoResponse>(this.apiUrl);
  }

  cancelar(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/cancelar`, {});
  }
}