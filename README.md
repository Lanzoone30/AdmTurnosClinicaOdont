# ClinicaOdontologica

Sistema web de administración de turnos para una clínica odontológica. Registra pacientes y odontólogos, asigna turnos y gestiona los usuarios del sistema.

## Funcionalidades
- **Historia clínica mínima del paciente**: alergias, antecedentes y medicación. Las alergias se destacan en el listado de pacientes y al asignar un turno.
- **Registro clínico del turno**: nota clínica (diagnóstico y tratamiento). Obligatoria al marcar el turno como `REALIZADO`.
- **Ciclo de vida del turno**: `PENDIENTE → CONFIRMADO → REALIZADO`, con `CANCELADO` y `NO_ASISTIO` como estados finales. Cancelar exige un motivo.
- **Horario de atención del odontólogo**: una franja por día de la semana; los turnos fuera del horario se rechazan.
- **Validación de formularios**: los errores se muestran junto al campo sin perder lo cargado.
- **Integridad al borrar**: no se elimina un paciente con turnos, un odontólogo con turnos o usuario, ni un usuario referenciado por un odontólogo.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Lenguaje | Java 25 |
| Framework | Spring Boot 4.1.0 |
| Persistencia | Spring Data JPA + Hibernate 7 |
| Vistas | Thymeleaf 3.1 |
| Seguridad | Spring Security (BCrypt) |
| Base de datos | MySQL 8 |
| Build | Maven |

## Estructura del proyecto

```
├── bd/                        # Scripts SQL listos para ejecutar
│   ├── schema.sql             # Crear base de datos y tablas
│   └── seed.sql               # Usuario admin por defecto
├── src/main/java/com/clinicaodontologica/
│   ├── config/                # Seguridad e inicialización de datos
│   ├── controller/            # Controladores MVC
│   ├── model/                 # Entidades JPA
│   ├── repository/            # Acceso a datos
│   └── service/               # Lógica de negocio
└── src/main/resources/
    ├── static/                # CSS, JS e íconos
    └── templates/             # Vistas Thymeleaf
```

## Requisitos

- Java 25
- Maven 3.9+
- MySQL 8

## Puesta en marcha

### 1. Crear la base de datos

Ejecute los scripts de la carpeta `bd/` en orden:

```bash
mysql -u root -p < bd/schema.sql
mysql -u root -p < bd/seed.sql
```

El seed crea el usuario `admin` con contraseña `admin`.

### 2. Configurar la conexión

Edite `src/main/resources/application.properties` con sus datos de MySQL:

```properties
spring.datasource.username=root
spring.datasource.password=su_password
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

Abra `http://localhost:8080` e ingrese con las credenciales del paso 1. Cambie la contraseña del admin luego del primer ingreso.

## Datos de acceso

| Campo | Valor |
|---|---|
| Usuario | `admin` |
| Contraseña | `admin` |

## Contacto

correo: lanzoone.adrian30@gmail.com
