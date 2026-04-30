import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { InventarioService } from '../../core/services/inventario.service';
import { AlmacenService } from '../../core/services/almacen.service';
import { ProductoService } from '../../core/services/producto.service';
import { Inventario } from '../../core/models/inventario.model';
import { Almacen } from '../../core/models/almacen.model';
import { Producto } from '../../core/models/producto.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NavbarComponent, FooterComponent],
  templateUrl: './inventario.component.html'
})
export class InventarioComponent implements OnInit {
  private svc = inject(InventarioService);
  private almacenSvc = inject(AlmacenService);
  private productoSvc = inject(ProductoService);
  private fb = inject(FormBuilder);
  mostrarCritico = signal(false);
  mostrarBajo = signal(false);

  inventario: Inventario[] = [];
  filtrados: Inventario[] = [];
  almacenes: Almacen[] = [];
  productos: Producto[] = [];
  searchTerm = '';
  filtroCritico = false;
  cargando = signal(false);
  modalAbierto = signal(false);
  modoEdicion = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);
  editAlmacenId: number | null = null;
  editProductoId: number | null = null;

  form = this.fb.group({
    almacenId: [null as number | null, Validators.required],
    productoId: [null as number | null, Validators.required],
    cantidad: [0, [Validators.required, Validators.min(0)]],
    stockMin: [0, [Validators.required, Validators.min(0)]],
    stockMax: [null as number | null]
  });

  get stocksCriticos() { return this.inventario.filter(i => i.stockCritico).length; }
  get stocksBajos() { return this.inventario.filter(i => i.bajStock && !i.stockCritico).length; }

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando.set(true);
    forkJoin({
      inventario: this.svc.getAll(),
      almacenes: this.almacenSvc.getAll(),
      productos: this.productoSvc.getAll()
    }).subscribe({
      next: ({ inventario, almacenes, productos }) => {
        this.inventario = inventario;
        this.almacenes = almacenes.filter(a => a.activo);
        this.productos = productos.filter(p => p.activo);
        this.filtrar();
        this.cargando.set(false);

      // Lógica de visibilidad temporal
      if (this.stocksCriticos > 0) {
        this.mostrarCritico.set(true);
        /*setTimeout(() => this.mostrarCritico.set(false), 5000);*/
      }
      
      if (this.stocksBajos > 0) {
        this.mostrarBajo.set(true);
        /*setTimeout(() => this.mostrarBajo.set(false), 5000);*/
      }

      },  
      error: () => { this.mostrarAlerta('err', 'Error al cargar inventario'); this.cargando.set(false); }
    });
  }

  filtrar() {
    let lista = this.filtroCritico
      ? this.inventario.filter(i => i.bajStock || i.stockCritico)
      : [...this.inventario];

    const t = this.searchTerm.toLowerCase();
    if (t) {
      lista = lista.filter(i =>
        i.productoNombre.toLowerCase().includes(t) ||
        i.productoSku.toLowerCase().includes(t) ||
        i.almacenNombre.toLowerCase().includes(t));
    }
    this.filtrados = lista;
  }

  abrirNuevo() {
    this.form.reset({ almacenId: null, productoId: null, cantidad: 0, stockMin: 0, stockMax: null });
    this.editAlmacenId = null;
    this.editProductoId = null;
    this.modoEdicion.set(false);
    this.modalAbierto.set(true);
  }

  abrirEditar(i: Inventario) {
    this.form.setValue({ almacenId: i.almacenId, productoId: i.productoId, cantidad: i.cantidad, stockMin: i.stockMin, stockMax: i.stockMax ?? null });
    this.editAlmacenId = i.almacenId;
    this.editProductoId = i.productoId;
    this.modoEdicion.set(true);
    this.modalAbierto.set(true);
  }

  cerrarModal() { this.modalAbierto.set(false); }

  guardar() {
    if (this.form.invalid || this.guardando()) return;
    this.guardando.set(true);
    const v = this.form.value;
    const payload = {
      almacenId: Number(v.almacenId),
      productoId: Number(v.productoId),
      cantidad: Number(v.cantidad),
      stockMin: Number(v.stockMin),
      stockMax: v.stockMax ? Number(v.stockMax) : null
    };

    const op = this.modoEdicion()
      ? this.svc.update(this.editAlmacenId!, this.editProductoId!, payload)
      : this.svc.create(payload);

    op.subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', `Registro de inventario ${this.modoEdicion() ? 'actualizado' : 'creado'} correctamente`);
        this.cargar();
      },
      error: () => { this.guardando.set(false); this.mostrarAlerta('err', 'Error al guardar el registro'); }
    });
  }

  eliminar(i: Inventario) {
    if (!confirm(`¿Eliminar el registro de "${i.productoNombre}" en "${i.almacenNombre}"?`)) return;
    this.svc.delete(i.almacenId, i.productoId).subscribe({
      next: () => { this.mostrarAlerta('ok', 'Registro eliminado'); this.cargar(); },
      error: () => this.mostrarAlerta('err', 'No se pudo eliminar el registro')
    });
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
