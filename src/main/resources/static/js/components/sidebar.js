// Sidebar collapse, toggled from the topbar button.
export function initSidebar() {
  const toggle = document.getElementById('sidebarToggle');
  if (!toggle) return;

  toggle.addEventListener('click', () => {
    document.body.classList.toggle('sidebar-collapsed');
  });
}
