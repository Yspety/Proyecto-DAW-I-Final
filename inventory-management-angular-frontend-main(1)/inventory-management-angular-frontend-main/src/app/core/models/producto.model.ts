export interface Producto {
  id: number;
  sku: string;
  nombre: string;
  descripcion: string | null;
  categoriaId: number;
  categoriaNombre: string;
  unidadId: number;
  unidadNombre: string;
  codigoBarras: string | null;
  precioLista: number | null;
  activo: boolean;
  creadoEn: string;
  ultimaActualizacion: string;
}

export interface ProductoRequest {
  sku: string;
  nombre: string;
  descripcion?: string | null;
  categoriaId: number;
  unidadId: number;
  codigoBarras?: string | null;
  precioLista?: number | null;
  activo: boolean;
}
