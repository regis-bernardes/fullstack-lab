import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ServidorService } from '../../services/servidor.service';
import { ExecucaoService } from '../../services/execucao.service';
import { ServidorSgbd } from '../../models/servidorSgbd.model';
import { ExecucaoResponse } from '../../models/execucao.model';

@Component({
  selector: 'app-sql-editor',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule],
  templateUrl: './sql-editor.component.html'
})
export class SqlEditorComponent implements OnInit {
  private servidorService = inject(ServidorService);
  private execucaoService = inject(ExecucaoService);
  private fb = inject(FormBuilder);

  servidores: ServidorSgbd[] = [];
  execucoes: ExecucaoResponse[] = [];
  loading = false;

  sqlForm: FormGroup = this.fb.group({
    apelido: ['', Validators.required],
    tipoExecucao: ['QUERY', Validators.required],
    idServidor: [null, Validators.required],
    conteudoSql: ['', Validators.required]
  });

  ngOnInit(): void {
    this.carregarServidores();
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
}
