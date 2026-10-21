import { Component, input, output, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ExecucaoResponse } from '../models/execucao.model';
import { ResultadoExecucaoResponse, ResultadoTabela } from '../models/resultado-execucao.model';
import { ResultadoExecucaoService } from '../services/resultado-execucao.service';

@Component({
  selector: 'app-detalhe-execucao',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './detalhe-execucao.component.html'
})
export class DetalheExecucaoComponent {
  private service = inject(ResultadoExecucaoService);
  resultadoExecucao: ResultadoExecucaoResponse[] = [];
  resultadosTabela: ResultadoTabela[] = [];
	
  execucaoSelecionada = input<ExecucaoResponse | null>(null);
  fechar = output<void>();

  constructor() {
    effect(() => {
      if (this.execucaoSelecionada()) {
        this.carregarResultados();
      }
    });
  }
  
  carregarResultados() {
  	const id = this.execucaoSelecionada()?.idExecucao;

  	if (id == null)
  		return;

    this.service.findByIdExecucao(id).subscribe({
      next: (data) => {
        this.resultadoExecucao = data;
        this.resultadosTabela = data
          .map((resultado) => this.parseResultado(resultado))
          .filter((tabela): tabela is ResultadoTabela => tabela !== null);
      },
      error: (err) => console.error('Erro ao carregar Resultado de execuções:', err)
    });
  }

  private parseResultado(resultado: ResultadoExecucaoResponse): ResultadoTabela | null {
    try {
      const parsed = JSON.parse(resultado.resultados);
      return {
        colunas: Array.isArray(parsed.colunas) ? parsed.colunas : [],
        linhas: Array.isArray(parsed.linhas) ? parsed.linhas : []
      };
    } catch (err) {
      console.error('Erro ao parsear resultado da execução:', err);
      return null;
    }
  }
}
