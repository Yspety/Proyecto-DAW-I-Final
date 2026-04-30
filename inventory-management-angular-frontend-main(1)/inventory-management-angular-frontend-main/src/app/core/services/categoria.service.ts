import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Categoria, CategoriaRequest } from '../models/categoria.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CategoriaService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/categorias`;

  getAll(): Observable<Categoria[]> {
    return this.http.get<ApiEnvelope<Categoria[]>>(this.url).pipe(map(r => r.data as Categoria[]));
  }

  create(data: CategoriaRequest): Observable<Categoria> {
    return this.http.post<ApiEnvelope<Categoria>>(this.url, data).pipe(map(r => r.data as Categoria));
  }

  update(id: number, data: CategoriaRequest): Observable<Categoria> {
    return this.http.put<ApiEnvelope<Categoria>>(`${this.url}/${id}`, data).pipe(map(r => r.data as Categoria));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiEnvelope<null>>(`${this.url}/${id}`).pipe(map(() => void 0));
  }
}
