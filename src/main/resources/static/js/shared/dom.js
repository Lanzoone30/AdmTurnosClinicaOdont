// Shared DOM helpers. No UI logic.
export function qsa(selector, scope = document) {
  return Array.from(scope.querySelectorAll(selector));
}

// Locks a form's submit button while the request is in flight, so a slow
// response cannot be double-submitted. Progressive enhancement only.
export function markPending(form) {
  const button = form.querySelector('button[type="submit"], button:not([type])');
  if (!button || button.disabled) return;
  button.setAttribute('aria-busy', 'true');
  button.disabled = true;
}
