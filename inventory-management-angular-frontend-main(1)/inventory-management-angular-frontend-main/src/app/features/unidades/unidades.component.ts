import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { UnidadService } from '../../core/services/unidad.service';
import { Unidad } from '../../core/models/unidad.model';

@Component({
  selector: 'app-unidades',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NavbarComponent, FooterComponent],
  templateUrl: './unidades.component.html'
})
export class UnidadesComponent implements OnInit {
  private svc = inject(UnidadService);
  private fb = inject(FormBuilder);

  unidades: Unidad[] = [];
  filtrados: Unidad[] = [];
  searchTerm = '';
  cargando = signal(false);
  modalAbierto = signal(false);
  modoEdicion = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);
  editId: number | null = null;

  form = this.fb.group({
    codigo: ['', Validators.required],
    nombre: ['', Validators.required],
    factorBase: [1, [Validators.required, Validators.min(0.0001)]],
    esBase: [false]
  });

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando.set(true);
    this.svc.getAll().subscribe({
      next: data => { this.unidades = data; this.filtrar(); this.cargando.set(false); },
      error: () => { this.mostrarAlerta('err', 'Error al cargar unidades'); this.cargando.set(false); }
    });
  }

  filtrar() {
    const t = this.searchTerm.toLowerCase();
    this.filtrados = t
      ? this.unidades.filter(u => u.codigo.toLowerCase().includes(t) || u.nombre.toLowerCase().includes(t))
      : [...this.unidades];
  }

  abrirNuevo() {
    this.form.reset({ codigo: '', nombre: '', factorBase: 1, esBase: false });
    this.editId = null;
    this.modoEdicion.set(false);
    this.modalAbierto.set(true);
  }

  abrirEditar(u: Unidad) {
    this.form.setValue({ codigo: u.codigo, nombre: u.nombre, factorBase: u.factorBase, esBase: u.esBase });
    this.editId = u.id;
    this.modoEdicion.set(true);
    this.modalAbierto.set(true);
  }

  cerrarModal() { this.modalAbierto.set(false); }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload = { codigo: v.codigo!, nombre: v.nombre!, factorBase: Number(v.factorBase), esBase: v.esBase! };

    const op = this.modoEdicion()
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    op.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', `Unidad ${this.modoEdicion() ? 'actualizada' : 'creada'} correctamente`);
        this.cargar();
      },
      error: () => { this.guardando.set(false); this.mostrarAlerta('err', 'Error al guardar la unidad'); }
    });
  }

  eliminar(u: Unidad) {
    if (!confirm(`¿Eliminar la unidad "${u.nombre}"?`)) return;
    this.svc.delete(u.id).subscribe({
      next: () => { this.mostrarAlerta('ok', 'Unidad eliminada'); this.cargar(); },
      error: () => this.mostrarAlerta('err', 'No se pudo eliminar la unidad')
    });
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
