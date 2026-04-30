import { Component } from '@angular/core';

@Component({
  selector: 'app-footer',
  standalone: true,
  styles: [`
    :host { display: block; margin-top: auto; }
    .footer {
      background: #0F172A;
    }
    .footer-border {
      height: 1px;
      background: linear-gradient(90deg,
        transparent 0%, rgba(59,130,246,0.35) 25%,
        rgba(96,165,250,0.6) 50%, rgba(59,130,246,0.35) 75%, transparent 100%);
    }
    .footer-inner {
      max-width: 1400px; margin: 0 auto;
      padding: 16px 28px;
      display: flex; align-items: center;
      justify-content: space-between; gap: 20px;
      flex-wrap: wrap;
    }
    .footer-left {
      display: flex; align-items: center; gap: 10px;
    }
    .footer-mark {
      width: 22px; height: 22px; border-radius: 5px;
      background: linear-gradient(135deg, #1D4ED8, #3B82F6);
      display: flex; align-items: center; justify-content: center;
      font-size: 8.5px; font-weight: 800; color: #fff;
      flex-shrink: 0;
    }
    .footer-copy { font-size: 12px; color: #475569; }
    .footer-copy strong { color: #64748B; font-weight: 500; }
    .footer-center {
      font-size: 11px; color: rgba(71,85,105,0.5);
      text-transform: uppercase; letter-spacing: 0.08em;
    }
    .footer-right {
      display: flex; align-items: center; gap: 6px;
      font-size: 11px; font-weight: 600;
      color: #3B82F6; letter-spacing: 0.08em; text-transform: uppercase;
    }
    @media (max-width: 639px) {
      .footer-center { display: none; }
      .footer-inner  { justify-content: space-between; }
    }
  `],
  template: `
    <footer class="footer">
      <div class="footer-border"></div>
      <div class="footer-inner">
        <div class="footer-left">
          <div class="footer-mark">DA</div>
          <span class="footer-copy">
            &copy; {{ year }} <strong>Distribuidora Andina S.A.C.</strong>
          </span>
        </div>
        <span class="footer-center">Sistema de Gestión de Inventario</span>
        <div class="footer-right">
          <svg width="10" height="10" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"
               stroke-linecap="round" stroke-linejoin="round">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
          </svg>
          v1.0
        </div>
      </div>
    </footer>
  `
})
export class FooterComponent {
  year = new Date().getFullYear();
}
