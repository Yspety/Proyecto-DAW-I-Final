import { Component, inject, signal, HostListener } from '@angular/core';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styles: [`
    :host { display: block; position: sticky; top: 0; z-index: 50; }

    .nav-accent {
      height: 2px;
      background: linear-gradient(90deg,
        #0F172A 0%, #1D4ED8 20%, #60A5FA 50%, #1D4ED8 80%, #0F172A 100%);
    }
    .nav-bar {
      background: #0F172A;
      border-bottom: 1px solid rgba(255,255,255,0.06);
    }
    .nav-container {
      max-width: 1400px; margin: 0 auto;
      padding: 0 28px; height: 60px;
      display: flex; align-items: center; gap: 0;
    }

    /* ─ Brand ─ */
    .nav-brand {
      display: flex; align-items: center; gap: 10px;
      text-decoration: none; flex-shrink: 0; margin-right: 32px;
    }
    .brand-mark {
      width: 32px; height: 32px; border-radius: 8px;
      background: linear-gradient(135deg, #1D4ED8 0%, #3B82F6 100%);
      display: flex; align-items: center; justify-content: center;
      font-size: 11px; font-weight: 800; color: #fff; letter-spacing: 0.5px;
      flex-shrink: 0;
      box-shadow: 0 0 0 1px rgba(59,130,246,0.3), 0 2px 8px rgba(29,78,216,0.4);
    }
    .brand-text { display: flex; flex-direction: column; gap: 1px; }
    .brand-name { font-size: 13px; font-weight: 600; color: #F1F5F9; line-height: 1; }
    .brand-sub  { font-size: 9.5px; color: #3B82F6; letter-spacing: 0.14em; text-transform: uppercase; line-height: 1; }

    /* ─ Desktop Links ─ */
    .nav-links {
      display: flex; align-items: stretch;
      list-style: none; padding: 0; margin: 0; flex: 1; gap: 0;
    }
    .nav-links li { display: flex; }
    .nav-item {
      display: flex; align-items: center;
      padding: 0 14px; font-size: 13px; font-weight: 500; color: #94A3B8;
      text-decoration: none; border-bottom: 2px solid transparent;
      transition: color 0.15s, border-color 0.15s; white-space: nowrap;
    }
    .nav-item:hover { color: #F1F5F9; }
    .nav-item.active-link { color: #F1F5F9; border-bottom-color: #3B82F6; }

    /* ─ Dropdown trigger ─ */
    .nav-drop-li { display: flex; position: relative; }
    .nav-drop-btn {
      display: flex; align-items: center; gap: 5px;
      padding: 0 14px; height: 100%;
      font-size: 13px; font-weight: 500; color: #94A3B8;
      background: none; border: none; border-bottom: 2px solid transparent;
      cursor: pointer; white-space: nowrap;
      transition: color 0.15s;
    }
    .nav-drop-btn:hover { color: #F1F5F9; }
    .nav-dropdown {
      position: absolute; top: calc(100% + 4px); left: 0;
      background: #1E293B; border: 1px solid rgba(255,255,255,0.1);
      border-radius: 10px; padding: 5px; min-width: 175px;
      list-style: none; margin: 0;
      box-shadow: 0 12px 32px rgba(0,0,0,0.45);
      animation: fadeDown 0.14s cubic-bezier(0.16,1,0.3,1) both;
    }
    @keyframes fadeDown {
      from { opacity: 0; transform: translateY(-6px); }
      to   { opacity: 1; transform: translateY(0); }
    }
    .drop-link {
      display: flex; align-items: center;
      padding: 8px 12px; border-radius: 7px;
      font-size: 13px; color: #94A3B8; text-decoration: none;
      transition: background 0.12s, color 0.12s;
    }
    .drop-link:hover { background: rgba(59,130,246,0.12); color: #F1F5F9; }
    .drop-link.drop-danger:hover { background: rgba(239,68,68,0.12); color: #FCA5A5; }
    .drop-hr { border: none; border-top: 1px solid rgba(255,255,255,0.07); margin: 4px 0; }

    /* ─ Divider ─ */
    .nav-sep { width: 1px; background: rgba(255,255,255,0.08); margin: 14px 20px; flex-shrink: 0; }

    /* ─ User section ─ */
    .nav-user { display: flex; align-items: center; gap: 10px; flex-shrink: 0; }
    .user-avatar {
      width: 32px; height: 32px; border-radius: 50%;
      background: linear-gradient(135deg, #1D4ED8, #3B82F6);
      border: 2px solid rgba(59,130,246,0.35);
      display: flex; align-items: center; justify-content: center;
      font-size: 11px; font-weight: 700; color: #fff; flex-shrink: 0;
    }
    .user-info { display: flex; flex-direction: column; }
    .user-name { font-size: 12.5px; font-weight: 500; color: #E2E8F0; line-height: 1.3; }
    .user-role { font-size: 9.5px; color: #3B82F6; text-transform: uppercase; letter-spacing: 0.1em; }
    .btn-logout {
      width: 32px; height: 32px; border-radius: 8px;
      border: 1px solid rgba(255,255,255,0.1); background: transparent;
      color: #64748B; cursor: pointer; display: flex; align-items: center; justify-content: center;
      transition: all 0.15s; flex-shrink: 0;
    }
    .btn-logout:hover { color: #F1F5F9; border-color: rgba(255,255,255,0.22); background: rgba(255,255,255,0.07); }

    /* ─ Hamburger ─ */
    .hamburger {
      display: none; align-items: center; justify-content: center;
      width: 36px; height: 36px; border-radius: 8px;
      border: 1px solid rgba(255,255,255,0.1); background: transparent;
      color: #94A3B8; cursor: pointer; margin-left: auto;
      transition: all 0.15s; flex-shrink: 0;
    }
    .hamburger:hover { color: #F1F5F9; border-color: rgba(255,255,255,0.22); background: rgba(255,255,255,0.06); }

    /* ─ Mobile panel ─ */
    .mobile-panel {
      background: #0F172A;
      border-top: 1px solid rgba(255,255,255,0.06);
      padding: 8px 0 16px;
      animation: slideDown 0.2s ease both;
    }
    @keyframes slideDown {
      from { opacity: 0; transform: translateY(-8px); }
      to   { opacity: 1; transform: translateY(0); }
    }
    .mobile-inner { max-width: 1400px; margin: 0 auto; padding: 0 20px; }
    .mobile-link {
      display: flex; align-items: center;
      padding: 11px 14px; border-radius: 8px;
      font-size: 14px; font-weight: 500; color: #94A3B8;
      text-decoration: none; transition: background 0.12s, color 0.12s;
      gap: 10px;
    }
    .mobile-link:hover { background: rgba(255,255,255,0.06); color: #F1F5F9; }
    .mobile-link.mobile-danger:hover { background: rgba(239,68,68,0.1); color: #FCA5A5; }
    .mobile-divider { height: 1px; background: rgba(255,255,255,0.06); margin: 8px 14px; }
    .mobile-user-row {
      display: flex; align-items: center; justify-content: space-between;
      padding: 12px 14px; margin-top: 4px;
    }
    .mobile-user-info { display: flex; align-items: center; gap: 10px; }
    .mobile-user-name { font-size: 13px; font-weight: 500; color: #CBD5E1; }
    .mobile-user-role { font-size: 10px; color: #3B82F6; text-transform: uppercase; letter-spacing: 0.1em; }
    .mobile-logout-btn {
      display: flex; align-items: center; gap: 6px;
      padding: 8px 14px; border-radius: 8px;
      border: 1px solid rgba(239,68,68,0.3); background: transparent;
      font-size: 12.5px; font-weight: 500; color: #F87171;
      cursor: pointer; transition: all 0.15s;
    }
    .mobile-logout-btn:hover { background: rgba(239,68,68,0.1); border-color: rgba(239,68,68,0.5); }

    /* ─ Responsive ─ */
    @media (max-width: 1023px) {
      .nav-links  { display: none; }
      .nav-sep    { display: none; }
      .user-info  { display: none; }
      .hamburger  { display: flex; }
    }
    @media (max-width: 479px) {
      .nav-brand  { margin-right: 0; }
      .brand-text { display: none; }
    }
  `]
})
export class NavbarComponent {
  private auth   = inject(AuthService);
  private router = inject(Router);

  menuAbierto = signal(false);
  menuMovil   = signal(false);

  get nombreCompleto() { return this.auth.getNombreCompleto(); }
  get rol()            { return this.auth.getRol(); }
  get iniciales() {
    return this.nombreCompleto.split(' ').slice(0, 2).map(p => p[0]).join('').toUpperCase();
  }

  isAdmin() { return this.auth.isAdmin(); }

  toggleDrop(e: Event) { e.stopPropagation(); this.menuAbierto.update(v => !v); }
  toggleMovil(e: Event) { e.stopPropagation(); this.menuMovil.update(v => !v); }

  @HostListener('document:click')
  onDocClick() { this.menuAbierto.set(false); }

  closeMovil() { this.menuMovil.set(false); }

  logout() { this.auth.logout(); this.router.navigate(['/login']); }
}
