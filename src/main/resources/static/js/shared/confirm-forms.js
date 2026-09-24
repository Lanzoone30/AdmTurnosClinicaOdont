// Generic confirmation for destructive forms. Any <form data-confirm="...">
// opens the shared dialog before submitting, replacing inline confirm().
import { qsa } from './dom.js';
import { confirmDialog } from './dialog.js';

export function initConfirmForms() {
  qsa('form[data-confirm]').forEach((form) => {
    form.addEventListener('submit', async (event) => {
      event.preventDefault();
      const { confirmed } = await confirmDialog({
        title: 'Confirmar',
        text: form.dataset.confirm,
      });
      if (confirmed) form.submit();
    });
  });
}
