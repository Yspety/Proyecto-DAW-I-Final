export interface Almacen {
  id: number;
  nombre: string;
  tipo: 'PRINCIPAL' | 'SECUNDARIO' | 'TEMPORAL';
  direccion: string | null;
  activo: boolean;
  ultimaActualizacion: string;
}

export interface AlmacenRequest {
  nombre: string;
  tipo: string;
  direccion?: string | null;
  activo: boolean;
}
