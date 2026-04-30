import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export const basicAuthInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('erp_token');
  const apiBase = environment.apiUrl || '';
  const isApiRequest = req.url.startsWith('/api') || (apiBase !== '' && req.url.startsWith(`${apiBase}/api`));

  if (token && isApiRequest) {
    const authReq = req.clone({
      setHeaders: { Authorization: `Basic ${token}` }
    });
    return next(authReq);
  }
  return next(req);
};
