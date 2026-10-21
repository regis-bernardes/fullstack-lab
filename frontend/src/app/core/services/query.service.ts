import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface QueryRequest {
  serverId: number;
  query: string;
}

export interface QueryResult {
  executionId: string;
  status: string;
  columns?: string[];
  rows?: any[];
  errorMessage?: string;
  executionTimeMs?: number;
}

@Injectable({
  providedIn: 'root'
})
export class QueryService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/queries'; // Ajuste conforme o endpoint do seu backend

  executeQuery(request: QueryRequest): Observable<QueryResult> {
    return this.http.post<QueryResult>(`${this.apiUrl}/execute`, request);
  }
}