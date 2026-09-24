// Shared DOM helpers. No UI logic.
export function qsa(selector, scope = document) {
  return Array.from(scope.querySelectorAll(selector));
}
