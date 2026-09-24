// Turno state-change forms: cancel (asks for motivo) and no-show (confirm only).
// The former inline prompt()/confirm() now go through the shared dialog.
import { qsa } from '../shared/dom.js';
import { confirmDialog } from '../shared/dialog.js';

const CONFIG = {
  CANCELADO: {
    title: 'Cancelar turno',
    text: 'Indicá el motivo de cancelación.',
    requireMotivo: true,
  },
  NO_ASISTIO: {
    title: 'Marcar como no asistió',
    text: '¿Confirmás que el paciente no asistió?',
    requireMotivo: false,
  },
};

export function initTurnoEstado() {
  qsa('form[data-estado-dialog]').forEach((form) => {
    form.addEventListener('submit', async (event) => {
      event.preventDefault();
      const estado = form.querySelector('[name="estado"]')?.value;
      const { confirmed, motivo } = await confirmDialog(CONFIG[estado] ?? CONFIG.NO_ASISTIO);
      if (!confirmed) return;

      const motivoField = form.querySelector('[name="motivo"]');
      if (motivoField) motivoField.value = motivo;
      form.submit();
    });
  });
}
