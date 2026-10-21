import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ServerService } from '../../services/server.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-server-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './server-register.component.html'
})
export class ServerRegisterComponent {
  private fb = inject(FormBuilder);
  private serverService = inject(ServerService);
  private router = inject(Router);

  serverForm: FormGroup = this.fb.group({
    nomeServidor: ['', Validators.required],
    tipo: ['POSTGRESQL', Validators.required],
    databaseName: ['', Validators.required],
    host: ['', Validators.required],
    porta: ['', [Validators.required, Validators.pattern(/^[0-9]+$/)]],
    username: ['', Validators.required],
    senhaCriptografada: ['', Validators.required]
  });

  onSubmit() {
    if (this.serverForm.valid) {
      this.serverService.createServer(this.serverForm.value).subscribe({
        next: () => {
          alert('Servidor cadastrado com sucesso!');
          this.router.navigate(['/editor']);
        },
        error: (err) => {
          console.error('Erro ao cadastrar servidor:', err);
          alert('Erro ao cadastrar servidor. Verifique os dados e tente novamente.');
        }
      });
    }
  }
}