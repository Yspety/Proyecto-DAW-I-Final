import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Producto, ProductoRequest } from '../models/producto.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ProductoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/productos`;

  getAll(): Observable<Producto[]> {
    return this.http.get<ApiEnvelope<Producto[]>>(this.url).pipe(map(r => r.data as Producto[]));
  }

  create(data: ProductoRequest): Observable<Producto> {
    return this.http.post<ApiEnvelope<Producto>>(this.url, data).pipe(map(r => r.data as Producto));
  }

  update(id: number, data: ProductoRequest): Observable<Producto> {
    return this.http.put<ApiEnvelope<Producto>>(`${this.url}/${id}`, data).pipe(map(r => r.data as Producto));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiEnvelope<null>>(`${this.url}/${id}`).pipe(map(() => void 0));
  }
}
