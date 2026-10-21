import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResultadoExecucaoResponse } from '../models/resultado-execucao.model';

@Injectable({
  providedIn: 'root'
})
export class ResultadoExecucaoService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/resultados';

  findByIdExecucao(id: number): Observable<ResultadoExecucaoResponse[]> {
    return this.http.get<ResultadoExecucaoResponse[]>(this.apiUrl+'/'+id + '/resultado');
	
  }
}