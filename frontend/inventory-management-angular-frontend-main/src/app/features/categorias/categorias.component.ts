import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { CategoriaService } from '../../core/services/categoria.service';
import { Categoria } from '../../core/models/categoria.model';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NavbarComponent, FooterComponent],
  templateUrl: './categorias.component.html'
})
export class CategoriasComponent implements OnInit {
  private svc = inject(CategoriaService);
  private fb = inject(FormBuilder);

  categorias: Categoria[] = [];
  filtrados: Categoria[] = [];
  searchTerm = '';
  cargando = signal(false);
  modalAbierto = signal(false);
  modoEdicion = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);
  editId: number | null = null;

  form = this.fb.group({
    nombre: ['', Validators.required],
    descripcion: [''],
    activo: [true]
  });

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando.set(true);
    this.svc.getAll().subscribe({
      next: data => { this.categorias = data; this.filtrar(); this.cargando.set(false); },
      error: () => { this.mostrarAlerta('err', 'Error al cargar categorías'); this.cargando.set(false); }
    });
  }

  filtrar() {
    const t = this.searchTerm.toLowerCase();
    this.filtrados = t
      ? this.categorias.filter(c => c.nombre.toLowerCase().includes(t) || (c.descripcion ?? '').toLowerCase().includes(t))
      : [...this.categorias];
  }

  abrirNuevo() {
    this.form.reset({ nombre: '', descripcion: '', activo: true });
    this.editId = null;
    this.modoEdicion.set(false);
    this.modalAbierto.set(true);
  }

  abrirEditar(c: Categoria) {
    this.form.setValue({ nombre: c.nombre, descripcion: c.descripcion ?? '', activo: c.activo });
    this.editId = c.id;
    this.modoEdicion.set(true);
    this.modalAbierto.set(true);
  }

  cerrarModal() { this.modalAbierto.set(false); }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload = { nombre: v.nombre!, descripcion: v.descripcion || null, activo: v.activo! };

    const op = this.modoEdicion()
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    op.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', `Categoría ${this.modoEdicion() ? 'actualizada' : 'creada'} correctamente`);
        this.cargar();
      },
      error: () => { this.guardando.set(false); this.mostrarAlerta('err', 'Error al guardar la categoría'); }
    });
  }

  eliminar(c: Categoria) {
    if (!confirm(`¿Eliminar la categoría "${c.nombre}"?`)) return;
    this.svc.delete(c.id).subscribe({
      next: () => { this.mostrarAlerta('ok', 'Categoría eliminada'); this.cargar(); },
      error: () => this.mostrarAlerta('err', 'No se pudo eliminar la categoría')
    });
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
