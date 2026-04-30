import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Usuario, UsuarioRequest, Rol } from '../models/usuario.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/usuarios`;

  getAll(): Observable<Usuario[]> {
    return this.http.get<ApiEnvelope<Usuario[]>>(this.url).pipe(map(r => r.data as Usuario[]));
  }

  create(data: UsuarioRequest): Observable<Usuario> {
    return this.http.post<ApiEnvelope<Usuario>>(this.url, data).pipe(map(r => r.data as Usuario));
  }

  update(id: number, data: UsuarioRequest): Observable<Usuario> {
    return this.http.put<ApiEnvelope<Usuario>>(`${this.url}/${id}`, data).pipe(map(r => r.data as Usuario));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiEnvelope<null>>(`${this.url}/${id}`).pipe(map(() => void 0));
  }

  getRoles(): Observable<Rol[]> {
    return this.http.get<ApiEnvelope<Rol[]>>(`${environment.apiUrl}/api/roles`).pipe(map(r => r.data as Rol[]));
  }
}
