export type TipoMovimiento = 'ENTRADA' | 'SALIDA' | 'AJUSTE';

export interface Movimiento {
  id: number;
  fechaMovimiento: string;
  almacenId: number;
  almacenNombre: string;
  productoId: number;
  productoNombre: string;
  productoSku: string;
  tipoMovimiento: TipoMovimiento;
  cantidad: number;
  costoUnitario: number | null;
  costoTotal: number | null;
  referencia: string | null;
  usuario: string;
}

export interface MovimientoRequest {
  almacenId: number;
  productoId: number;
  tipoMovimiento: TipoMovimiento;
  cantidad: number;
  costoUnitario?: number | null;
  referencia?: string | null;
}
