export interface Categoria {
  id: number;
  nombre: string;
  descripcion: string | null;
  activo: boolean;
  ultimaActualizacion: string;
}

export interface CategoriaRequest {
  nombre: string;
  descripcion?: string | null;
  activo: boolean;
}
