// Theme toggle. The actual data-theme is applied early by an inline script in
// the layout head (to avoid a flash); this only syncs the button and persists.
const KEY = 'theme';

function current() {
  return document.documentElement.dataset.theme || 'light';
}

function apply(theme) {
  document.documentElement.dataset.theme = theme;
  localStorage.setItem(KEY, theme);
  const button = document.getElementById('themeToggle');
  if (!button) return;
  const dark = theme === 'dark';
  button.setAttribute('aria-pressed', String(dark));
  button.setAttribute('aria-label', dark
    ? (button.dataset.temaClaro || 'Light theme')
    : (button.dataset.temaOscuro || 'Dark theme'));
  const icon = button.querySelector('i');
  if (icon) icon.className = dark ? 'fas fa-sun' : 'fas fa-moon';
}

export function initTheme() {
  const button = document.getElementById('themeToggle');
  if (!button) return;
  apply(current());
  button.addEventListener('click', () => {
    apply(current() === 'dark' ? 'light' : 'dark');
  });
}
