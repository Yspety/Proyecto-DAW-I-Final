/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {
      colors: {
        'erp-navy':       '#1B2A4A',
        'erp-blue':       '#2D4A8A',
        'erp-accent':     '#5B9BD5',
        'erp-bg':         '#EEF2F8',
        'erp-card':       '#F5F7FA',
        'erp-border':     '#D3D1C7',
        'erp-text':       '#2C2C2A',
        'erp-muted':      '#888780',
        'erp-light-blue': '#8FA8D0',
        'erp-pale':       '#E8EEF8',
      },
      fontFamily: {
        sans: ['DM Sans', 'sans-serif'],
      },
      borderWidth: {
        '05': '0.5px',
      },
    },
  },
  plugins: [],
};
