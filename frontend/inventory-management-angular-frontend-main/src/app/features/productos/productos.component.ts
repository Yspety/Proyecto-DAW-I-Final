import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { ProductoService } from '../../core/services/producto.service';
import { CategoriaService } from '../../core/services/categoria.service';
import { UnidadService } from '../../core/services/unidad.service';
import { Producto } from '../../core/models/producto.model';
import { Categoria } from '../../core/models/categoria.model';
import { Unidad } from '../../core/models/unidad.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-productos',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NavbarComponent, FooterComponent],
  templateUrl: './productos.component.html'
})
export class ProductosComponent implements OnInit {
  private svc = inject(ProductoService);
  private catSvc = inject(CategoriaService);
  private unidadSvc = inject(UnidadService);
  private fb = inject(FormBuilder);

  productos: Producto[] = [];
  filtrados: Producto[] = [];
  categorias: Categoria[] = [];
  unidades: Unidad[] = [];
  searchTerm = '';
  cargando = signal(false);
  modalAbierto = signal(false);
  modoEdicion = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);
  editId: number | null = null;

  form = this.fb.group({
    sku: ['', Validators.required],
    nombre: ['', Validators.required],
    descripcion: [''],
    categoriaId: [null as number | null, Validators.required],
    unidadId: [null as number | null, Validators.required],
    codigoBarras: [''],
    precioLista: [null as number | null],
    activo: [true]
  });

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando.set(true);
    forkJoin({
      productos: this.svc.getAll(),
      categorias: this.catSvc.getAll(),
      unidades: this.unidadSvc.getAll()
    }).subscribe({
      next: ({ productos, categorias, unidades }) => {
        this.productos = productos;
        this.categorias = categorias.filter(c => c.activo);
        this.unidades = unidades;
        this.filtrar();
        this.cargando.set(false);
      },
      error: () => { this.mostrarAlerta('err', 'Error al cargar datos'); this.cargando.set(false); }
    });
  }

  filtrar() {
    const t = this.searchTerm.toLowerCase();
    this.filtrados = t
      ? this.productos.filter(p =>
          p.nombre.toLowerCase().includes(t) ||
          p.sku.toLowerCase().includes(t) ||
          p.categoriaNombre.toLowerCase().includes(t))
      : [...this.productos];
  }

  abrirNuevo() {
    this.form.reset({ sku: '', nombre: '', descripcion: '', categoriaId: null, unidadId: null, codigoBarras: '', precioLista: null, activo: true });
    this.editId = null;
    this.modoEdicion.set(false);
    this.modalAbierto.set(true);
  }

  abrirEditar(p: Producto) {
    this.form.setValue({
      sku: p.sku, nombre: p.nombre, descripcion: p.descripcion ?? '',
      categoriaId: p.categoriaId, unidadId: p.unidadId,
      codigoBarras: p.codigoBarras ?? '', precioLista: p.precioLista ?? null, activo: p.activo
    });
    this.editId = p.id;
    this.modoEdicion.set(true);
    this.modalAbierto.set(true);
  }

  cerrarModal() { this.modalAbierto.set(false); }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload = {
      sku: v.sku!, nombre: v.nombre!,
      descripcion: v.descripcion || null,
      categoriaId: Number(v.categoriaId),
      unidadId: Number(v.unidadId),
      codigoBarras: v.codigoBarras || null,
      precioLista: v.precioLista ? Number(v.precioLista) : null,
      activo: v.activo!
    };

    const op = this.modoEdicion()
      ? this.svc.update(this.editId!, payload)
      : this.svc.create(payload);

    op.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', `Producto ${this.modoEdicion() ? 'actualizado' : 'creado'} correctamente`);
        this.cargar();
      },
      error: () => { this.guardando.set(false); this.mostrarAlerta('err', 'Error al guardar el producto'); }
    });
  }

  eliminar(p: Producto) {
    if (!confirm(`¿Eliminar el producto "${p.nombre}"?`)) return;
    this.svc.delete(p.id).subscribe({
      next: () => { this.mostrarAlerta('ok', 'Producto eliminado'); this.cargar(); },
      error: () => this.mostrarAlerta('err', 'No se pudo eliminar el producto')
    });
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
