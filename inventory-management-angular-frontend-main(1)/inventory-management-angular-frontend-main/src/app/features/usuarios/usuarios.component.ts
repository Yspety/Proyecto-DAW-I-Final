import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { SlicePipe } from '@angular/common';
import { Router } from '@angular/router';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { UsuarioService } from '../../core/services/usuario.service';
import { AuthService } from '../../core/services/auth.service';
import { Usuario, Rol } from '../../core/models/usuario.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, SlicePipe, NavbarComponent, FooterComponent],
  templateUrl: './usuarios.component.html'
})
export class UsuariosComponent implements OnInit {
  private svc = inject(UsuarioService);
  private auth = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  usuarios: Usuario[] = [];
  filtrados: Usuario[] = [];
  roles: Rol[] = [];
  searchTerm = '';
  cargando = signal(false);
  modalAbierto = signal(false);
  modoEdicion = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);
  editId: number | null = null;

  form = this.fb.group({
    nombre: ['', Validators.required],
    apellido: ['', Validators.required],
    usuario: ['', Validators.required],
    password: [''],
    rolId: [null as number | null, Validators.required],
    activo: [true]
  });

  ngOnInit() {
    if (!this.auth.isAdmin()) {
      this.router.navigate(['/home']);
      return;
    }
    this.cargar();
  }

  cargar() {
    this.cargando.set(true);
    forkJoin({
      usuarios: this.svc.getAll(),
      roles: this.svc.getRoles()
    }).subscribe({
      next: ({ usuarios, roles }) => {
        this.usuarios = usuarios;
        this.roles = roles;
        this.filtrar();
        this.cargando.set(false);
      },
      error: () => { this.mostrarAlerta('err', 'Error al cargar usuarios'); this.cargando.set(false); }
    });
  }

  filtrar() {
    const t = this.searchTerm.toLowerCase();
    this.filtrados = t
      ? this.usuarios.filter(u =>
          u.nombre.toLowerCase().includes(t) ||
          u.apellido.toLowerCase().includes(t) ||
          u.usuario.toLowerCase().includes(t) ||
          u.rolDescripcion.toLowerCase().includes(t))
      : [...this.usuarios];
  }

  abrirNuevo() {
    this.form.reset({ nombre: '', apellido: '', usuario: '', password: '', rolId: null, activo: true });
    this.editId = null;
    this.modoEdicion.set(false);
    this.modalAbierto.set(true);
  }

  abrirEditar(u: Usuario) {
    this.form.setValue({ nombre: u.nombre, apellido: u.apellido, usuario: u.usuario, password: '', rolId: u.rolId, activo: u.activo });
    this.editId = u.id;
    this.modoEdicion.set(true);
    this.modalAbierto.set(true);
  }

  cerrarModal() { this.modalAbierto.set(false); }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload = {
      nombre: v.nombre!, apellido: v.apellido!, usuario: v.usuario!,
      password: v.password || null,
      rolId: Number(v.rolId), activo: v.activo!
    };

    const op = this.modoEdicion()
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    op.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', `Usuario ${this.modoEdicion() ? 'actualizado' : 'creado'} correctamente`);
        this.cargar();
      },
      error: () => { this.guardando.set(false); this.mostrarAlerta('err', 'Error al guardar el usuario'); }
    });
  }

  eliminar(u: Usuario) {
    if (!confirm(`¿Eliminar el usuario "${u.usuario}"?`)) return;
    this.svc.delete(u.id).subscribe({
      next: () => { this.mostrarAlerta('ok', 'Usuario eliminado'); this.cargar(); },
      error: () => this.mostrarAlerta('err', 'No se pudo eliminar el usuario')
    });
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
