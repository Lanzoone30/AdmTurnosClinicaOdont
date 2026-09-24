-- ============================================================================
-- ClinicaOdontologica - Esquema de base de datos MySQL
-- ============================================================================
-- Ejecutar:  mysql -u root -p < bd/schema.sql
-- Crea la base de datos y las 5 tablas del sistema. Replica el esquema
-- que genera Hibernate (Spring Boot con ddl-auto=update), listo para usar
-- sin dependencia del generador de esquemas.
-- ============================================================================

CREATE DATABASE IF NOT EXISTS clinica_odontologica
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE clinica_odontologica;

-- ----------------------------------------------------------------------------
-- Tabla horario: franja de atencion semanal de un odontologo
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS horario (
  id             INT          NOT NULL AUTO_INCREMENT,
  dia_semana     VARCHAR(20)  NULL,
  horario_fin    TIME(0)      NULL,
  horario_inicio TIME(0)      NULL,
  -- No FK to persona: horario is created first, so persona does not exist yet.
  odontologo_id  INT          NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

-- ----------------------------------------------------------------------------
-- Tabla usuario: credenciales de acceso (contrasenia hasheada con BCrypt)
-- rol: ADMIN | ODONTOLOGO | PACIENTE | SECRETARIO
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
  id             INT          NOT NULL AUTO_INCREMENT,
  contrasenia    VARCHAR(255) NULL,
  nombre_usuario VARCHAR(255) NULL,
  rol            VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_usuario_nombre (nombre_usuario)
) ENGINE = InnoDB;

-- ----------------------------------------------------------------------------
-- Tabla persona: herencia SINGLE_TABLE (Paciente, Odontologo, Responsable,
-- Secretario). La columna "tipo" discrimina el subtipo.
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS persona (
  tipo             VARCHAR(31)  NOT NULL,
  id               INT          NOT NULL AUTO_INCREMENT,
  apellido         VARCHAR(255) NULL,
  direccion        VARCHAR(255) NULL,
  dni              VARCHAR(255) NULL,
  fecha_nac        DATE         NULL,
  nombre           VARCHAR(255) NULL,
  telefono         VARCHAR(255) NULL,
  fecha_alta       DATE         NULL,
  -- Campos de los subtipos (null segun el tipo):
  especialidad     VARCHAR(255) NULL,  -- Odontologo
  tieneos          BIT          NULL,  -- Paciente
  nombre_os        VARCHAR(255) NULL,  -- Paciente
  numero_afiliado  VARCHAR(255) NULL,  -- Paciente
  tipo_sangre      VARCHAR(255) NULL,  -- Paciente
  alergias         VARCHAR(1000) NULL, -- Paciente
  antecedentes     VARCHAR(1000) NULL, -- Paciente
  medicacion       VARCHAR(1000) NULL, -- Paciente
  tipo_resp        VARCHAR(255) NULL,  -- Responsable
  sector           VARCHAR(255) NULL,  -- Secretario
  -- Relaciones:
  usuario_id       INT          NULL,  -- Odontologo/Secretario/Paciente -> usuario
  responsable_id   INT          NULL,  -- Paciente -> persona (Responsable)
  PRIMARY KEY (id),
  UNIQUE KEY UK_persona_dni (dni),
  UNIQUE KEY UK_persona_usuario (usuario_id),
  UNIQUE KEY UK_persona_responsable (responsable_id),
  CONSTRAINT FK_persona_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id),
  CONSTRAINT FK_persona_responsable FOREIGN KEY (responsable_id) REFERENCES persona (id)
) ENGINE = InnoDB;

-- ----------------------------------------------------------------------------
-- Tabla turno: cita entre un paciente y un odontologo
-- estado: PENDIENTE | CONFIRMADO | CANCELADO | REALIZADO | NO_ASISTIO
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS turno (
  id                 INT           NOT NULL AUTO_INCREMENT,
  afeccion           VARCHAR(255)  NULL,
  fecha_turno        DATE          NULL,
  hora_turno         TIME(0)       NULL,
  estado             VARCHAR(255)  NULL,
  nota_clinica       VARCHAR(2000) NULL,
  motivo_cancelacion VARCHAR(500)  NULL,
  id_odonto          INT           NULL,
  id_pacien          INT           NULL,
  PRIMARY KEY (id),
  CONSTRAINT FK_turno_odonto FOREIGN KEY (id_odonto) REFERENCES persona (id),
  CONSTRAINT FK_turno_paciente FOREIGN KEY (id_pacien) REFERENCES persona (id)
) ENGINE = InnoDB;

-- ----------------------------------------------------------------------------
-- Tabla auditoria: registro de operaciones del sistema
-- accion: CREAR | MODIFICAR | ELIMINAR
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS auditoria (
  id       INT          NOT NULL AUTO_INCREMENT,
  fecha    DATETIME(6)  NULL,
  usuario  VARCHAR(255) NULL,
  entidad  VARCHAR(255) NULL,
  accion   VARCHAR(255) NULL,
  detalle  VARCHAR(255) NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;
