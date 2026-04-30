import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { AlmacenService } from '../../core/services/almacen.service';
import { Almacen } from '../../core/models/almacen.model';

@Component({
  selector: 'app-almacenes',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NavbarComponent, FooterComponent],
  templateUrl: './almacenes.component.html'
})
export class AlmacenesComponent implements OnInit {
  private svc = inject(AlmacenService);
  private fb = inject(FormBuilder);

  almacenes: Almacen[] = [];
  filtrados: Almacen[] = [];
  searchTerm = '';
  cargando = signal(false);
  modalAbierto = signal(false);
  modoEdicion = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);
  editId: number | null = null;

  form = this.fb.group({
    nombre: ['', Validators.required],
    tipo: ['PRINCIPAL', Validators.required],
    direccion: [''],
    activo: [true]
  });

  tiposAlmacen = ['PRINCIPAL', 'SECUNDARIO', 'TEMPORAL'];

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando.set(true);
    this.svc.getAll().subscribe({
      next: data => {
        this.almacenes = data;
        this.filtrar();
        this.cargando.set(false);
      },
      error: () => {
        this.mostrarAlerta('err', 'Error al cargar almacenes');
        this.cargando.set(false);
      }
    });
  }

  filtrar() {
    const t = this.searchTerm.toLowerCase();
    this.filtrados = t
      ? this.almacenes.filter(a =>
          a.nombre.toLowerCase().includes(t) ||
          a.tipo.toLowerCase().includes(t) ||
          (a.direccion ?? '').toLowerCase().includes(t))
      : [...this.almacenes];
  }

  abrirNuevo() {
    this.form.reset({ nombre: '', tipo: 'PRINCIPAL', direccion: '', activo: true });
    this.editId = null;
    this.modoEdicion.set(false);
    this.modalAbierto.set(true);
  }

  abrirEditar(a: Almacen) {
    this.form.setValue({ nombre: a.nombre, tipo: a.tipo, direccion: a.direccion ?? '', activo: a.activo });
    this.editId = a.id;
    this.modoEdicion.set(true);
    this.modalAbierto.set(true);
  }

  cerrarModal() { this.modalAbierto.set(false); }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload = { nombre: v.nombre!, tipo: v.tipo!, direccion: v.direccion || null, activo: v.activo! };

    const op = this.modoEdicion()
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    op.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', `Almacén ${this.modoEdicion() ? 'actualizado' : 'creado'} correctamente`);
        this.cargar();
      },
      error: () => {
        this.guardando.set(false);
        this.mostrarAlerta('err', 'Error al guardar el almacén');
      }
    });
  }

  eliminar(a: Almacen) {
    if (!confirm(`¿Eliminar el almacén "${a.nombre}"?`)) return;
    this.svc.delete(a.id).subscribe({
      next: () => { this.mostrarAlerta('ok', 'Almacén eliminado'); this.cargar(); },
      error: () => this.mostrarAlerta('err', 'No se pudo eliminar el almacén')
    });
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
