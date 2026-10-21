import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ServidorSgbd } from '../../models/servidorSgbd.model';

// Interface para mapear a resposta paginada que vem do Spring Boot (Page<T>)
export interface RestPage<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
}

@Component({
  selector: 'app-servidores',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './servidorSgbd.component.html'
})
export class ServidorSgbdComponent implements OnInit {
  
  // Injeção moderna do HttpClient (Angular 19+)
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/servidores'; // Ajuste a URL se necessário

  servidores: ServidorSgbd[] = [];
  servidorAtual: Partial<ServidorSgbd> = {};
  
  exibirFormulario: boolean = false;
  modoEdicao: boolean = false;

  // Controlo de Paginação (Integrada com o Backend)
  paginaAtual: number = 1;      // O utilizador vê a partir da página 1
  itensPorPagina: number = 5;
  totalPaginas: number = 0;
  totalElementos: number = 0;

  ngOnInit(): void {
    this.carregarServidores();
  }

  // --- READ (Listar com paginação do Spring Boot) ---
  carregarServidores() {
    // O Spring Boot espera a página 0 para a primeira página, por isso subtraímos 1
    const pageIndex = this.paginaAtual - 1;
    
    const params = new HttpParams()
      .set('page', pageIndex.toString())
      .set('size', this.itensPorPagina.toString())
      .set('sortBy', 'idServidor');

    this.http.get<RestPage<ServidorSgbd>>(this.apiUrl, { params }).subscribe({
      next: (response) => {
        this.servidores = response.content;
        this.totalPaginas = response.totalPages;
        this.totalElementos = response.totalElements;
      },
      error: (err) => console.error('Erro ao carregar servidores:', err)
    });
  }

  // --- NAVEGAÇÃO DE PÁGINAS ---
  proximaPagina() {
    if (this.paginaAtual < this.totalPaginas) {
      this.paginaAtual++;
      this.carregarServidores();
    }
  }

  paginaAnterior() {
    if (this.paginaAtual > 1) {
      this.paginaAtual--;
      this.carregarServidores();
    }
  }

  // --- CONTROLOS DO FORMULÁRIO (MODAL) ---
  abrirFormularioNovo() {
    this.servidorAtual = {};
    this.modoEdicao = false;
    this.exibirFormulario = true;
  }

  prepararEdicao(srv: ServidorSgbd) {
    this.servidorAtual = { ...srv }; // Cópia para edição isolada
    this.modoEdicao = true;
    this.exibirFormulario = true;
  }

  fecharFormulario() {
    this.exibirFormulario = false;
    this.servidorAtual = {};
  }

  // --- CREATE (POST) & UPDATE (PUT) ---
  salvarServidor() {
    if (this.modoEdicao && this.servidorAtual.idServidor) {
      // Atualizar Servidor existente (PUT)
      this.http.put<ServidorSgbd>(`${this.apiUrl}/${this.servidorAtual.idServidor}`, this.servidorAtual).subscribe({
        next: () => {
          this.fecharFormulario();
          this.carregarServidores();
        },
        error: (err) => console.error('Erro ao atualizar servidor:', err)
      });
    } else {
      // Criar novo Servidor (POST)
      this.http.post<ServidorSgbd>(this.apiUrl, this.servidorAtual).subscribe({
        next: () => {
          this.fecharFormulario();
          this.carregarServidores();
        },
        error: (err) => console.error('Erro ao criar servidor:', err)
      });
    }
  }

  // --- DELETE (Excluir) ---
  excluirServidor(id: number) {
    if (confirm('Tem certeza que deseja excluir este servidor?')) {
      this.http.delete(`${this.apiUrl}/${id}`).subscribe({
        next: () => {
          // Se apagarmos o último elemento da página atual e não estivermos na pág 1, recuamos 1 página
          if (this.servidores.length === 1 && this.paginaAtual > 1) {
            this.paginaAtual--;
          }
          this.carregarServidores();
        },
        error: (err) => console.error('Erro ao excluir servidor:', err)
      });
    }
  }

  // Getters auxiliares opcionais para exibir os índices visuais no HTML
  get indiceInicial(): number {
    if (this.totalElementos === 0) return 0;
    return (this.paginaAtual - 1) * this.itensPorPagina + 1;
  }

  get indiceFinal(): number {
    const fim = this.paginaAtual * this.itensPorPagina;
    return fim > this.totalElementos ? this.totalElementos : fim;
  }
}