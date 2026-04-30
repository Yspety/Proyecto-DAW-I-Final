export interface Unidad {
  id: number;
  codigo: string;
  nombre: string;
  factorBase: number;
  esBase: boolean;
  ultimaActualizacion: string;
}

export interface UnidadRequest {
  codigo: string;
  nombre: string;
  factorBase: number;
  esBase: boolean;
}
