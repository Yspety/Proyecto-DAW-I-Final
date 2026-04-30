import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'home',
    canActivate: [authGuard],
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'almacenes',
    canActivate: [authGuard],
    loadComponent: () => import('./features/almacenes/almacenes.component').then(m => m.AlmacenesComponent)
  },
  {
    path: 'categorias',
    canActivate: [authGuard],
    loadComponent: () => import('./features/categorias/categorias.component').then(m => m.CategoriasComponent)
  },
  {
    path: 'productos',
    canActivate: [authGuard],
    loadComponent: () => import('./features/productos/productos.component').then(m => m.ProductosComponent)
  },
  {
    path: 'unidades',
    canActivate: [authGuard],
    loadComponent: () => import('./features/unidades/unidades.component').then(m => m.UnidadesComponent)
  },
  {
    path: 'inventario',
    canActivate: [authGuard],
    loadComponent: () => import('./features/inventario/inventario.component').then(m => m.InventarioComponent)
  },
  {
    path: 'movimientos',
    canActivate: [authGuard],
    loadComponent: () => import('./features/movimientos/movimientos.component').then(m => m.MovimientosComponent)
  },
  {
    path: 'usuarios',
    canActivate: [authGuard],
    loadComponent: () => import('./features/usuarios/usuarios.component').then(m => m.UsuariosComponent)
  },
  { path: '**', redirectTo: '/login' }
];
