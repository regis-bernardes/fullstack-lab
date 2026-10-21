import { Component, OnInit, OnDestroy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ServidorService } from '../../services/servidor.service'
import { ExecucaoService } from '../../services/execucao.service';
import { WebsocketService } from '../../services/websocket.service';
import { ServidorSgbd } from '../../models/servidorSgbd.model';
import { ExecucaoResponse } from '../../models/execucao.model';
import { DetalheExecucaoComponent } from '../../components/detalhe-execucao.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, DetalheExecucaoComponent],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit, OnDestroy {
  private servidorService = inject(ServidorService);
  private execucaoService = inject(ExecucaoService);
  private websocketService = inject(WebsocketService);
  private fb = inject(FormBuilder);

  servidores: ServidorSgbd[] = [];
  execucoes: ExecucaoResponse[] = [];
  execucaoSelecionada: ExecucaoResponse | null = null;
  loading = false;
  private wsSub!: Subscription;

  sqlForm: FormGroup = this.fb.group({
    apelido: ['', Validators.required],
    tipoExecucao: ['QUERY', Validators.required],
    idServidor: [null, Validators.required],
    conteudoSql: ['', Validators.required]
  });

  ngOnInit(): void {
    this.carregarServidores();
    this.carregarExecucoes();

    this.websocketService.connect();
    this.wsSub = this.websocketService.getExecucoesUpdates().subscribe(atualizacao => {
      const index = this.execucoes.findIndex(e => e.idExecucao === atualizacao.idExecucao);
      if (index !== -1) {
        this.execucoes[index] = atualizacao;
      } else {
        this.execucoes.unshift(atualizacao);
      }
    });
  }

  ngOnDestroy(): void {
    if (this.wsSub) {
      this.wsSub.unsubscribe();
    }
    this.websocketService.disconnect();
  }

  carregarServidores() {
    this.servidorService.listar().subscribe({
      next: (data) => {
        this.servidores = data.content;
        if (data.length > 0 && !this.sqlForm.value.idServidor) {
          this.sqlForm.patchValue({ idServidor: data[0].idServidor });
        }
      },
      error: (err) => console.error('Erro ao carregar servidores:', err)
    });
  }

  carregarExecucoes() {
    this.execucaoService.listar().subscribe({
      next: (data) => this.execucoes = data,
      error: (err) => console.error('Erro ao carregar execuções:', err)
    });
  }

  dispararSql() {
    if (this.sqlForm.invalid) return;

    this.loading = true;
    const formVal = this.sqlForm.value;
    
	const payload = {
      apelido: formVal.apelido,
      tipoExecucao: formVal.tipoExecucao,
      idServidor: formVal.idServidor,
      conteudoSql: formVal.conteudoSql
    };

    this.execucaoService.disparar(payload).subscribe({
      next: (res) => {
        this.loading = false;
        this.execucoes.unshift(res);
        this.sqlForm.patchValue({ apelido: '', conteudoSql: '' });
      },
      error: (err) => {
        this.loading = false;
        console.error('Erro ao disparar execução:', err);
      }
    });
  }

  abrirDetalhes(exec: ExecucaoResponse) {
    this.execucaoSelecionada = exec;
  }

  fecharDetalhes() {
    this.execucaoSelecionada = null;
  }

  cancelarExecucao(id: number) {
    this.execucaoService.cancelar(id).subscribe({
      next: () => console.log(`Execução ${id} cancelada com sucesso.`),
      error: (err) => console.error('Erro ao cancelar execução:', err)
    });
  }
}