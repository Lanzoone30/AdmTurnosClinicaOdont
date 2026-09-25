import { qsa } from '../shared/dom.js';

// Manual alert dismissal. Alerts announce themselves via role=alert (errors)
// or role=status (success), so no extra live region is needed here.
export function initAlerts() {
  qsa('.alert-close').forEach((btn) => {
    btn.addEventListener('click', () => {
      btn.closest('.alert')?.remove();
    });
  });
}
