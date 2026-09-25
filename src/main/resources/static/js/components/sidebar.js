// Sidebar toggling. Desktop: collapse/expand. Mobile: off-canvas panel
// with an overlay, Escape to close and focus moved in/out.
export function initSidebar() {
  const toggle = document.getElementById('sidebarToggle');
  const sidebar = document.getElementById('sidebar');
  const overlay = document.getElementById('sidebarOverlay');
  if (!toggle || !sidebar) return;

  const isMobile = () => window.matchMedia('(max-width: 768px)').matches;
  const abrir = toggle.dataset.abrir || 'Abrir menú';
  const cerrar = toggle.dataset.cerrar || 'Cerrar menú';

  const close = () => {
    document.body.classList.remove('sidebar-open');
    toggle.setAttribute('aria-expanded', 'false');
    toggle.setAttribute('aria-label', abrir);
    toggle.focus();
  };

  const open = () => {
    document.body.classList.add('sidebar-open');
    toggle.setAttribute('aria-expanded', 'true');
    toggle.setAttribute('aria-label', cerrar);
    const firstLink = sidebar.querySelector('.sidebar-brand');
    if (firstLink) firstLink.focus();
  };

  toggle.addEventListener('click', () => {
    if (isMobile()) {
      document.body.classList.contains('sidebar-open') ? close() : open();
      return;
    }
    const collapsed = document.body.classList.toggle('sidebar-collapsed');
    toggle.setAttribute('aria-expanded', String(!collapsed));
  });

  overlay?.addEventListener('click', close);

  document.addEventListener('keydown', (event) => {
    if (event.key === 'Escape' && document.body.classList.contains('sidebar-open')) {
      close();
    }
  });
}
