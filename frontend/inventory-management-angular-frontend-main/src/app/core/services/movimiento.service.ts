import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Movimiento, MovimientoRequest } from '../models/movimiento.model';
import { ApiEnvelope } from '../models/api-response.model';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class MovimientoService {
  private http = inject(HttpClient);
  private url = `${environment.apiUrl}/api/movimientos`;

  getAll(): Observable<Movimiento[]> {
    return this.http.get<ApiEnvelope<Movimiento[]>>(this.url).pipe(map(r => r.data as Movimiento[]));
  }

  create(data: MovimientoRequest): Observable<Movimiento> {
    return this.http.post<ApiEnvelope<Movimiento>>(this.url, data).pipe(map(r => r.data as Movimiento));
  }
}
