-- ============================================================================
-- ClinicaOdontologica - Datos iniciales
-- ============================================================================
-- Ejecutar:  mysql -u root -p < bd/seed.sql
-- Crea el usuario administrador por defecto.
--
-- Credenciales:  usuario = admin  |  contrasenia = admin
-- La contrasenia esta hasheada con BCrypt (NUNCA guardar en texto plano).
-- Cambie la contrasenia luego del primer ingreso.
-- ============================================================================

USE clinica_odontologica;

INSERT IGNORE INTO usuario (contrasenia, nombre_usuario, rol)
VALUES ('$2y$10$SM1wDfQFiKnDEGTEJFvjROvRtwUtI1sgm25P.sjMB4pht6oPiL94G', 'admin', 'ADMIN');
