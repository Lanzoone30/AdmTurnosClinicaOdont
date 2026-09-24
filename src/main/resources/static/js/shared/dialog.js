// Native <dialog> confirm primitive, shared by section behaviors.
// A dialog gives focus management, Esc-to-close and a backdrop for free.
// Resolves to { confirmed, motivo }.
export function confirmDialog({ title, text, requireMotivo = false }) {
  const dialog = document.createElement('dialog');
  dialog.className = 'modal';
  dialog.innerHTML = `
    <div class="modal-form">
      <h2 class="modal-title">${title}</h2>
      <p class="modal-text">${text}</p>
      <div class="field modal-motivo" hidden>
        <label class="field-label" for="modal-motivo">Motivo</label>
        <textarea class="textarea" id="modal-motivo" rows="3"></textarea>
        <p class="modal-error" hidden>El motivo es obligatorio.</p>
      </div>
      <div class="modal-actions">
        <button type="button" class="btn btn-ghost" data-action="cancel">Volver</button>
        <button type="button" class="btn btn-danger" data-action="confirm">Confirmar</button>
      </div>
    </div>`;
  document.body.appendChild(dialog);

  const motivoWrap = dialog.querySelector('.modal-motivo');
  const motivoInput = dialog.querySelector('#modal-motivo');
  const error = dialog.querySelector('.modal-error');
  motivoWrap.hidden = !requireMotivo;

  return new Promise((resolve) => {
    dialog.addEventListener('close', () => {
      resolve({
        confirmed: dialog.returnValue === 'confirm',
        motivo: motivoInput.value.trim(),
      });
      dialog.remove();
    });

    dialog.querySelector('[data-action="cancel"]').addEventListener('click', () => {
      dialog.close('cancel');
    });

    dialog.querySelector('[data-action="confirm"]').addEventListener('click', () => {
      if (requireMotivo && motivoInput.value.trim() === '') {
        error.hidden = false;
        motivoInput.focus();
        return;
      }
      dialog.close('confirm');
    });

    dialog.showModal();
    if (requireMotivo) motivoInput.focus();
  });
}
