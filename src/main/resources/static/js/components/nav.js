import { qsa } from '../shared/dom.js';

// Navigation submenus: click on a [data-collapse] link toggles its parent.
// A submenu holding the current page opens on load so the active item is visible.
export function initNav() {
  qsa('.nav-item > .nav-link[data-collapse]').forEach((link) => {
    const submenu = link.parentElement.querySelector('.nav-sub');
    if (submenu) {
      if (!submenu.id) submenu.id = `nav-sub-${qsa('.nav-sub').indexOf(submenu) + 1}`;
      link.setAttribute('aria-controls', submenu.id);
      const abierto = submenu.querySelector('.is-active') !== null;
      link.setAttribute('aria-expanded', String(abierto));
      if (abierto) link.parentElement.classList.add('open');
    }
    link.addEventListener('click', (event) => {
      event.preventDefault();
      const open = link.parentElement.classList.toggle('open');
      link.setAttribute('aria-expanded', String(open));
    });
  });
}
