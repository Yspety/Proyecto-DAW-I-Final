export interface Inventario {
  almacenId: number;
  almacenNombre: string;
  productoId: number;
  productoNombre: string;
  productoSku: string;
  cantidad: number;
  stockMin: number;
  stockMax: number | null;
  bajStock: boolean;
  stockCritico: boolean;
  ultimaActualizacion: string;
}

export interface InventarioRequest {
  almacenId: number;
  productoId: number;
  cantidad: number;
  stockMin: number;
  stockMax?: number | null;
}
