import { Component, inject, signal, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { SlicePipe } from '@angular/common';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { MovimientoService } from '../../core/services/movimiento.service';
import { AlmacenService } from '../../core/services/almacen.service';
import { ProductoService } from '../../core/services/producto.service';
import { Movimiento, TipoMovimiento } from '../../core/models/movimiento.model';
import { Almacen } from '../../core/models/almacen.model';
import { Producto } from '../../core/models/producto.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, SlicePipe, NavbarComponent, FooterComponent],
  templateUrl: './movimientos.component.html'
})
export class MovimientosComponent implements OnInit {
  private svc = inject(MovimientoService);
  private almacenSvc = inject(AlmacenService);
  private productoSvc = inject(ProductoService);
  private fb = inject(FormBuilder);

  movimientos: Movimiento[] = [];
  filtrados: Movimiento[] = [];
  almacenes: Almacen[] = [];
  productos: Producto[] = [];
  searchTerm = '';
  filtroTipo: TipoMovimiento | '' = '';
  cargando = signal(false);
  modalAbierto = signal(false);
  guardando = signal(false);
  alerta = signal<{ tipo: 'ok' | 'err'; msg: string } | null>(null);

  tiposMovimiento: TipoMovimiento[] = ['ENTRADA', 'SALIDA', 'AJUSTE'];

  form = this.fb.group({
    almacenId: [null as number | null, Validators.required],
    productoId: [null as number | null, Validators.required],
    tipoMovimiento: ['ENTRADA' as TipoMovimiento, Validators.required],
    cantidad: [null as number | null, [Validators.required, Validators.min(0.001)]],
    costoUnitario: [null as number | null],
    referencia: ['']
  });

  ngOnInit() { this.cargar(); }

  cargar() {
    this.cargando.set(true);
    forkJoin({
      movimientos: this.svc.getAll(),
      almacenes: this.almacenSvc.getAll(),
      productos: this.productoSvc.getAll()
    }).subscribe({
      next: ({ movimientos, almacenes, productos }) => {
        this.movimientos = movimientos.sort((a, b) => b.id - a.id);
        this.almacenes = almacenes.filter(a => a.activo);
        this.productos = productos.filter(p => p.activo);
        this.filtrar();
        this.cargando.set(false);
      },
      error: () => { this.mostrarAlerta('err', 'Error al cargar movimientos'); this.cargando.set(false); }
    });
  }

  filtrar() {
    let lista = [...this.movimientos];
    if (this.filtroTipo) lista = lista.filter(m => m.tipoMovimiento === this.filtroTipo);
    const t = this.searchTerm.toLowerCase();
    if (t) {
      lista = lista.filter(m =>
        m.productoNombre.toLowerCase().includes(t) ||
        m.productoSku.toLowerCase().includes(t) ||
        m.almacenNombre.toLowerCase().includes(t) ||
        (m.referencia ?? '').toLowerCase().includes(t));
    }
    this.filtrados = lista;
  }

  abrirNuevo() {
    this.form.reset({ almacenId: null, productoId: null, tipoMovimiento: 'ENTRADA', cantidad: null, costoUnitario: null, referencia: '' });
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
      tipoMovimiento: v.tipoMovimiento as TipoMovimiento,
      cantidad: Number(v.cantidad),
      costoUnitario: v.costoUnitario ? Number(v.costoUnitario) : null,
      referencia: v.referencia || null
    };

    this.svc.create(payload).subscribe({
      next: () => {
        this.guardando.set(false);
        this.cerrarModal();
        this.mostrarAlerta('ok', 'Movimiento registrado correctamente');
        this.cargar();
      },
      error: () => { this.guardando.set(false); this.mostrarAlerta('err', 'Error al registrar el movimiento'); }
    });
  }

  badgeTipo(tipo: TipoMovimiento): string {
    if (tipo === 'ENTRADA') return 'erp-badge bg-[#EAF6EE] text-[#1A5C2E]';
    if (tipo === 'SALIDA') return 'erp-badge bg-[#FCEBEB] text-[#A32D2D]';
    return 'erp-badge bg-[#FFF8E6] text-[#854F0B]';
  }

  private mostrarAlerta(tipo: 'ok' | 'err', msg: string) {
    this.alerta.set({ tipo, msg });
    setTimeout(() => this.alerta.set(null), 5000);
  }
}
