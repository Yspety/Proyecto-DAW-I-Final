import { Component, inject, signal } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private auth = inject(AuthService);
  private router = inject(Router);

  form = this.fb.group({
    usuario: ['', Validators.required],
    password: ['', Validators.required]
  });

  cargando = signal(false);
  error = signal('');

  features = [
    { label: 'Gestión de almacenes y productos' },
    { label: 'Control de stock en tiempo real' },
    { label: 'Registro de entradas y salidas' },
    { label: 'Reportes y trazabilidad completa' }
  ];

  submit() {
    if (this.form.invalid || this.cargando()) return;
    this.error.set('');
    this.cargando.set(true);

    const { usuario, password } = this.form.value;
    this.auth.login(usuario!, password!).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (err: Error) => {
        this.cargando.set(false);
        this.error.set(err.message);
        setTimeout(() => this.error.set(''), 5000);
      }
    });
  }
}
