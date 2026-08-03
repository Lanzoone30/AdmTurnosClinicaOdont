/**
 * ClinicaOdontologica - interacciones de la interfaz.
 * Vanilla JS: reemplaza las dependencias de jQuery/Bootstrap del template
 * anterior (collapse del sidebar y submenu de navegacion).
 */
(function () {
  'use strict';

  // Colapso de la sidebar (boton del topbar)
  var toggle = document.getElementById('sidebarToggle');
  if (toggle) {
    toggle.addEventListener('click', function () {
      document.body.classList.toggle('sidebar-collapsed');
    });
  }

  // Submenus de la navegacion (Odontologos, Pacientes, Turnos, Usuarios)
  document.querySelectorAll('.nav-item > .nav-link[data-collapse]').forEach(function (link) {
    link.addEventListener('click', function (event) {
      event.preventDefault();
      link.parentElement.classList.toggle('open');
    });
  });

  // Cierre manual de alertas
  document.querySelectorAll('.alert-close').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var alert = btn.closest('.alert');
      if (alert) {
        alert.remove();
      }
    });
  });
})();
