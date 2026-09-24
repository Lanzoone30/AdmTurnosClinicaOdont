// Turno form: preview the selected patient's allergies.
export function initTurnoForm() {
  const select = document.getElementById('pacienteId');
  const out = document.getElementById('alergiasPaciente');
  if (!select || !out) return;

  select.addEventListener('change', () => {
    const alergias = select.selectedOptions[0]?.dataset.alergias ?? '';
    out.textContent = alergias;
    out.hidden = alergias === '';
  });
}
