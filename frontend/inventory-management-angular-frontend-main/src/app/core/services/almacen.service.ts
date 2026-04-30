import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Almacen, AlmacenRequest } from '../models/almacen.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AlmacenService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/almacenes`;

  getAll(): Observable<Almacen[]> {
    return this.http.get<ApiEnvelope<Almacen[]>>(this.url).pipe(map(r => r.data as Almacen[]));
  }

  create(data: AlmacenRequest): Observable<Almacen> {
    return this.http.post<ApiEnvelope<Almacen>>(this.url, data).pipe(map(r => r.data as Almacen));
  }

  update(id: number, data: AlmacenRequest): Observable<Almacen> {
    return this.http.put<ApiEnvelope<Almacen>>(`${this.url}/${id}`, data).pipe(map(r => r.data as Almacen));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiEnvelope<null>>(`${this.url}/${id}`).pipe(map(() => void 0));
  }
}
