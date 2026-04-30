import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Unidad, UnidadRequest } from '../models/unidad.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class UnidadService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/unidades`;

  getAll(): Observable<Unidad[]> {
    return this.http.get<ApiEnvelope<Unidad[]>>(this.url).pipe(map(r => r.data as Unidad[]));
  }

  create(data: UnidadRequest): Observable<Unidad> {
    return this.http.post<ApiEnvelope<Unidad>>(this.url, data).pipe(map(r => r.data as Unidad));
  }

  update(id: number, data: UnidadRequest): Observable<Unidad> {
    return this.http.put<ApiEnvelope<Unidad>>(`${this.url}/${id}`, data).pipe(map(r => r.data as Unidad));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiEnvelope<null>>(`${this.url}/${id}`).pipe(map(() => void 0));
  }
}
