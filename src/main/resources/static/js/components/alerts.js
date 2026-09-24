import { qsa } from '../shared/dom.js';

// Manual alert dismissal.
export function initAlerts() {
  qsa('.alert-close').forEach((btn) => {
    btn.addEventListener('click', () => {
      btn.closest('.alert')?.remove();
    });
  });
}
