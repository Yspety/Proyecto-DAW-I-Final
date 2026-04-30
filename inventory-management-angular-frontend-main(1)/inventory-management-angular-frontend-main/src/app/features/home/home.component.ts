import { Component, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { NavbarComponent } from '../../shared/components/navbar/navbar.component';
import { FooterComponent } from '../../shared/components/footer/footer.component';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, NavbarComponent, FooterComponent],
  templateUrl: './home.component.html',
  styles: [`
    :host { display: flex; flex-direction: column; min-height: 100vh; background: #F0F4F8; }

    /* ═══ HERO ═══════════════════════════════════════════ */
    .hero {
      background: #0F172A;
      background-image: radial-gradient(circle, rgba(59,130,246,0.14) 1px, transparent 1px);
      background-size: 22px 22px;
      padding: 40px 0 52px;
      position: relative;
      overflow: hidden;
    }
    .hero::before {
      content: '';
      position: absolute; inset: 0;
      background: linear-gradient(135deg, #0F172A 0%, rgba(15,23,42,0.7) 60%, transparent 100%);
      pointer-events: none;
    }
    .hero-inner {
      max-width: 1400px; margin: 0 auto; padding: 0 28px;
      display: flex; align-items: center;
      justify-content: space-between; gap: 24px;
      flex-wrap: wrap; position: relative; z-index: 1;
    }
    .hero-left { display: flex; align-items: center; gap: 18px; }
    .hero-avatar {
      width: 56px; height: 56px; border-radius: 14px;
      background: linear-gradient(135deg, #1D4ED8 0%, #3B82F6 100%);
      border: 2px solid rgba(59,130,246,0.4);
      display: flex; align-items: center; justify-content: center;
      font-size: 18px; font-weight: 700; color: #fff; flex-shrink: 0;
      box-shadow: 0 4px 20px rgba(29,78,216,0.4);
    }
    .hero-role-chip {
      display: inline-flex; align-items: center; gap: 6px;
      background: rgba(59,130,246,0.12);
      border: 1px solid rgba(59,130,246,0.25);
      border-radius: 100px; padding: 3px 10px 3px 7px;
      margin-bottom: 6px;
    }
    .hero-role-dot {
      width: 6px; height: 6px; border-radius: 50%; background: #3B82F6; flex-shrink: 0;
    }
    .hero-role-text {
      font-size: 10px; font-weight: 700; color: #60A5FA;
      text-transform: uppercase; letter-spacing: 0.12em;
    }
    .hero-greeting {
      font-size: 26px; font-weight: 700; color: #F1F5F9;
      line-height: 1.2; margin: 0 0 4px;
    }
    .hero-greeting strong { color: #fff; font-weight: 800; }
    .hero-date { font-size: 12px; color: #64748B; margin: 0; }

    /* Status pill */
    .hero-status {
      display: flex; align-items: center; gap: 10px;
      background: rgba(255,255,255,0.04);
      border: 1px solid rgba(255,255,255,0.09);
      border-radius: 100px; padding: 10px 18px;
      flex-shrink: 0;
    }
    .pulse-ring {
      position: relative; width: 10px; height: 10px; flex-shrink: 0;
      display: flex; align-items: center; justify-content: center;
    }
    .pulse-ring::before {
      content: '';
      position: absolute; inset: 0; border-radius: 50%;
      background: rgba(34,197,94,0.4);
      animation: ripple 2s cubic-bezier(0,0,0.2,1) infinite;
    }
    .pulse-core {
      width: 8px; height: 8px; border-radius: 50%;
      background: #22C55E; display: block; position: relative;
    }
    @keyframes ripple {
      0%   { transform: scale(1); opacity: 0.6; }
      70%  { transform: scale(2.4); opacity: 0; }
      100% { transform: scale(2.4); opacity: 0; }
    }
    .hero-status-text { font-size: 12.5px; font-weight: 500; color: #94A3B8; }

    /* ═══ KPI STRIP ══════════════════════════════════════ */
    .kpi-strip {
      background: #fff;
      border-bottom: 1px solid #E2E8F0;
      box-shadow: 0 4px 20px rgba(15,23,42,0.06);
    }
    .kpi-grid {
      max-width: 1400px; margin: 0 auto;
      display: grid; grid-template-columns: repeat(4,1fr);
      divide-x: 1px;
    }
    .kpi-cell {
      padding: 18px 28px;
      border-right: 1px solid #E2E8F0;
      cursor: default; user-select: none;
      transition: background 0.15s;
    }
    .kpi-cell:last-child { border-right: none; }
    .kpi-cell:hover { background: #F8FAFC; }
    .kpi-label {
      font-size: 10px; font-weight: 700; letter-spacing: 0.12em;
      text-transform: uppercase; color: #94A3B8; margin-bottom: 5px;
    }
    .kpi-value {
      font-size: 26px; font-weight: 800; line-height: 1;
      margin-bottom: 3px; transition: color 0.2s;
    }
    .kpi-value--navy  { color: #0F172A; }
    .kpi-value--green { color: #15803D; }
    .kpi-cell:hover .kpi-value { color: #1D4ED8; }
    .kpi-name {
      font-size: 14px; font-weight: 600; color: #0F172A;
      line-height: 1.3; margin-bottom: 3px; transition: color 0.2s;
    }
    .kpi-cell:hover .kpi-name { color: #1D4ED8; }
    .kpi-sub { font-size: 11px; color: #94A3B8; }

    /* ═══ MODULES ════════════════════════════════════════ */
    .modules-wrap {
      max-width: 1400px; margin: 0 auto;
      padding: 28px 28px 44px; flex: 1;
    }

    /* Section separator */
    .section-sep {
      display: flex; align-items: center; gap: 14px;
      margin-bottom: 24px;
    }
    .section-sep-label {
      font-size: 10px; font-weight: 700;
      color: #94A3B8; text-transform: uppercase; letter-spacing: 0.15em;
      white-space: nowrap;
    }
    .section-sep-line { flex: 1; height: 1px; background: #E2E8F0; }

    /* Group */
    .group { margin-bottom: 28px; }
    .group-head {
      display: flex; align-items: center; gap: 8px;
      margin-bottom: 14px;
    }
    .group-dot { width: 8px; height: 8px; border-radius: 2px; flex-shrink: 0; }
    .group-label-text {
      font-size: 11px; font-weight: 700;
      text-transform: uppercase; letter-spacing: 0.12em;
    }

    /* Card grids */
    .cards-4 { display: grid; grid-template-columns: repeat(4,1fr); gap: 12px; }
    .cards-2 { display: grid; grid-template-columns: repeat(2,1fr); gap: 12px; }
    .cards-3 { display: grid; grid-template-columns: repeat(3,1fr); gap: 12px; }

    /* Module card */
    .mod-card {
      display: flex; align-items: center; gap: 14px;
      background: #fff;
      border: 1px solid #E8ECF2;
      border-radius: 10px;
      padding: 16px 18px;
      text-decoration: none;
      transition: transform 0.18s, box-shadow 0.18s, border-color 0.18s;
      animation: fadeUp 0.3s ease both;
      position: relative; overflow: hidden;
    }
    .mod-card:hover { transform: translateY(-2px); box-shadow: 0 6px 24px rgba(15,23,42,0.09); }
    @keyframes fadeUp {
      from { opacity: 0; transform: translateY(8px); }
      to   { opacity: 1; transform: translateY(0); }
    }

    /* Left accent stripe */
    .mod-card::before {
      content: '';
      position: absolute; left: 0; top: 0; bottom: 0; width: 3px;
      transform: scaleY(0); transform-origin: bottom;
      transition: transform 0.2s cubic-bezier(0.16,1,0.3,1);
      border-radius: 0 2px 2px 0;
    }
    .mod-card:hover::before { transform: scaleY(1); }
    .mod-card--g::before { background: #1E4ED8; }
    .mod-card--o::before { background: #0EA5E9; }
    .mod-card--a::before { background: #DC2626; }
    .mod-card--g:hover { border-color: #BFDBFE; }
    .mod-card--o:hover { border-color: #BAE6FD; }
    .mod-card--a:hover { border-color: #FECACA; box-shadow: 0 6px 24px rgba(220,38,38,0.08); }

    /* Card icon */
    .mod-icon {
      width: 40px; height: 40px; border-radius: 9px;
      display: flex; align-items: center; justify-content: center;
      flex-shrink: 0; transition: transform 0.18s;
    }
    .mod-card:hover .mod-icon { transform: scale(1.06); }
    .mod-icon--g { background: #EFF6FF; }
    .mod-icon--o { background: #F0F9FF; }
    .mod-icon--a { background: #FEF2F2; }

    /* Card body */
    .mod-body { flex: 1; min-width: 0; }
    .mod-title {
      font-size: 13.5px; font-weight: 600; color: #1E293B;
      display: block; line-height: 1.3;
    }
    .mod-desc {
      font-size: 11.5px; color: #94A3B8;
      display: block; margin-top: 2px; line-height: 1.4;
      white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
    }

    /* Arrow */
    .mod-arrow {
      color: #CBD5E1; flex-shrink: 0;
      transition: color 0.15s, transform 0.15s;
    }
    .mod-card:hover .mod-arrow { transform: translateX(3px); }
    .mod-card--g:hover .mod-arrow { color: #1E4ED8; }
    .mod-card--o:hover .mod-arrow { color: #0EA5E9; }
    .mod-card--a:hover .mod-arrow { color: #DC2626; }

    /* Admin badge */
    .admin-badge {
      position: absolute; top: 0; right: 12px;
      background: #FEE2E2; color: #B91C1C;
      font-size: 8.5px; font-weight: 700;
      padding: 2px 7px; border-radius: 0 0 5px 5px;
      text-transform: uppercase; letter-spacing: 0.08em;
    }

    /* ═══ RESPONSIVE ════════════════════════════════════ */
    @media (max-width: 1023px) {
      .cards-4 { grid-template-columns: repeat(2,1fr); }
      .cards-3 { grid-template-columns: repeat(2,1fr); }
      .kpi-grid { grid-template-columns: repeat(2,1fr); }
      .kpi-cell:nth-child(2) { border-right: none; }
      .kpi-cell:nth-child(3) { border-right: 1px solid #E2E8F0; }
    }
    @media (max-width: 767px) {
      .hero { padding: 28px 0 36px; }
      .modules-wrap { padding: 20px 18px 32px; }
    }
    @media (max-width: 639px) {
      .cards-4, .cards-2, .cards-3 { grid-template-columns: 1fr; }
      .hero-inner { flex-direction: column; align-items: flex-start; }
      .hero-status { align-self: flex-start; }
      .hero-greeting { font-size: 22px; }
    }
    @media (max-width: 479px) {
      .kpi-grid { grid-template-columns: 1fr 1fr; }
      .kpi-cell:nth-child(2) { border-right: none; }
      .kpi-cell:nth-child(3) { border-right: 1px solid #E2E8F0; }
      .kpi-cell:nth-child(2n) { border-right: none; }
    }
  `]
})
export class HomeComponent {
  private auth = inject(AuthService);

  get nombreCompleto() { return this.auth.getNombreCompleto(); }
  get primerNombre()   { return this.nombreCompleto.split(' ')[0]; }
  get iniciales()      { return this.nombreCompleto.split(' ').slice(0, 2).map(p => p[0] ?? '').join('').toUpperCase(); }
  get rol()            { return this.auth.getRol(); }
  get saludo(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Buenos días';
    if (h < 19) return 'Buenas tardes';
    return 'Buenas noches';
  }
  get fechaFormateada(): string {
    const s = new Date().toLocaleDateString('es-PE', {
      weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'
    });
    return s.charAt(0).toUpperCase() + s.slice(1);
  }
  get moduleCount() { return this.isAdmin() ? 7 : 6; }
  isAdmin()         { return this.auth.isAdmin(); }
}
