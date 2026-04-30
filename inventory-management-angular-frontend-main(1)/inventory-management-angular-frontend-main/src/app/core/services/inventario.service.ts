import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Inventario, InventarioRequest } from '../models/inventario.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class InventarioService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/inventario`;

  getAll(): Observable<Inventario[]> {
    return this.http.get<ApiEnvelope<Inventario[]>>(this.url).pipe(map(r => r.data as Inventario[]));
  }

  create(data: InventarioRequest): Observable<Inventario> {
    return this.http.post<ApiEnvelope<Inventario>>(this.url, data).pipe(map(r => r.data as Inventario));
  }

  update(almacenId: number, productoId: number, data: InventarioRequest): Observable<Inventario> {
    return this.http.put<ApiEnvelope<Inventario>>(`${this.url}/${almacenId}/${productoId}`, data).pipe(map(r => r.data as Inventario));
  }

  delete(almacenId: number, productoId: number): Observable<void> {
    return this.http.delete<ApiEnvelope<null>>(`${this.url}/${almacenId}/${productoId}`).pipe(map(() => void 0));
  }
}
