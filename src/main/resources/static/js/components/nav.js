import { qsa } from '../shared/dom.js';

// Navigation submenus: click on a [data-collapse] link toggles its parent.
export function initNav() {
  qsa('.nav-item > .nav-link[data-collapse]').forEach((link) => {
    link.addEventListener('click', (event) => {
      event.preventDefault();
      link.parentElement.classList.toggle('open');
    });
  });
}
