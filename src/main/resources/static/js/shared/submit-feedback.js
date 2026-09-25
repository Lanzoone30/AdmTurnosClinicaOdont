import { qsa, markPending } from './dom.js';

// Shows a pending state on submit and blocks double submission. Forms with
// [data-confirm] are handled by confirm-forms.js, which marks them pending
// only after the user confirms.
export function initSubmitFeedback() {
  qsa('form').forEach((form) => {
    if (form.hasAttribute('data-confirm')) return;
    form.addEventListener('submit', () => {
      if (!form.checkValidity || form.checkValidity()) markPending(form);
    });
  });
}
