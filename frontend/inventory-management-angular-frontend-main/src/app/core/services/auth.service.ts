import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap, map, catchError } from 'rxjs';
import { ApiEnvelope } from '../models/api-response.model';
import { Usuario } from '../models/usuario.model';
import { environment } from '../../../environments/environment';

interface UserInfo {
  nombreCompleto: string;
  rol: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private http = inject(HttpClient);

  login(usuario: string, password: string): Observable<void> {
    const token = btoa(`${usuario}:${password}`);
    localStorage.setItem('erp_token', token);

    return this.http.get<ApiEnvelope<Usuario>>(`${environment.apiUrl}/api/usuarios/me`).pipe(
      tap(res => {
        const u = res.data as Usuario;
        const info: UserInfo = {
          nombreCompleto: `${u.nombre} ${u.apellido}`,
          rol: u.rolDescripcion
        };
        localStorage.setItem('erp_user', JSON.stringify(info));
      }),
      map(() => void 0),
      catchError(err => {
        localStorage.removeItem('erp_token');
        localStorage.removeItem('erp_user');
        throw new Error(err.status === 401 ? 'Credenciales inválidas' : 'Error de conexión al servidor');
      })
    );
  }

  logout(): void {
    localStorage.removeItem('erp_token');
    localStorage.removeItem('erp_user');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('erp_token');
  }

  getToken(): string | null {
    return localStorage.getItem('erp_token');
  }

  getUserInfo(): UserInfo | null {
    const raw = localStorage.getItem('erp_user');
    return raw ? JSON.parse(raw) : null;
  }

  getNombreCompleto(): string {
    return this.getUserInfo()?.nombreCompleto ?? '';
  }

  getRol(): string {
    return this.getUserInfo()?.rol ?? '';
  }

  isAdmin(): boolean {
    return this.getRol() === 'ADMIN';
  }
}
