// Entry point. Each behavior lives in its own module; this file only wires them.
import { initSidebar } from './components/sidebar.js';
import { initNav } from './components/nav.js';
import { initAlerts } from './components/alerts.js';
import { initTurnoEstado } from './components/turno-estado.js';
import { initTurnoForm } from './components/turno-form.js';
import { initConfirmForms } from './shared/confirm-forms.js';
import { initSubmitFeedback } from './shared/submit-feedback.js';
import { initTheme } from './shared/theme.js';
initTheme();
initSidebar();
initNav();
initAlerts();
initTurnoEstado();
initTurnoForm();
initConfirmForms();
initSubmitFeedback();
