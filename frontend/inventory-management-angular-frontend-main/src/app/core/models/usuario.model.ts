export interface Usuario {
  id: number;
  nombre: string;
  apellido: string;
  usuario: string;
  activo: boolean;
  creadoEn: string;
  rolId: number;
  rolDescripcion: string;
}

export interface UsuarioRequest {
  nombre: string;
  apellido: string;
  usuario: string;
  password?: string | null;
  activo: boolean;
  rolId: number;
}

export interface Rol {
  id: number;
  descripcion: string;
}
