# Decisiones vigentes

## DEC-001 — Alcance aprobado para S2

**Estado:** vigente.  
Las HU aprobadas son [[HU-001-registrar-usuario]], [[HU-002-iniciar-y-renovar-sesion]] y [[HU-016-importar-y-conectar-frontend]]. Las demás HU siguen pendientes de aprobación.

## DEC-002 — Modelo inicial de reservas

**Estado:** vigente como diseño de referencia propio.  
La propuesta `database/database_v1` discretiza disponibilidad en slots de 30 minutos y usa una reserva viva única por slot; una cita de 60 minutos requiere dos slots consecutivos. La implementación futura debe preservar esa propiedad mediante Flyway y transacciones.

## DEC-003 — Agentes específicos diferidos

**Estado:** vigente.  
El AGENTS de API se elaborará al existir Spring Boot real. El AGENTS web se elaborará al existir el proyecto AI Studio y haberse detectado React o Angular.

## DEC-004 — Bootstrap de API

**Estado:** vigente.  
`citas-api` se inicializó con Spring Boot 3.5.0 y Maven para Java 21. Define los límites `domain`, `application` e `infrastructure/adapters`, y configura MySQL, Flyway, JPA, Security y Actuator mediante variables de entorno. Aún no contiene una migración o endpoints de autenticación: esos cambios pertenecen a los siguientes pasos de HU-001/HU-002.

## DEC-005 — Migración inicial Flyway

**Estado:** vigente y verificada.  
El esquema 3FN se incorporó como `V1__initial_schema.sql` en el backend. La migración no contiene `CREATE DATABASE` ni `USE`; Flyway la aplica sobre la base configurada por entorno. La aplicación de v1 se verificó contra MySQL 8.4 mediante el arranque de Spring Boot.

## DEC-006 — Diseño de autenticación S2

**Estado:** vigente, pendiente de validación HTTP/integración.  
El registro crea únicamente cuentas `USER`, cifra el password con BCrypt y asigna el rol dentro de la misma transacción. El access token es un JWT HS256 con roles y expiración; el refresh token es aleatorio, se persiste únicamente como SHA-256 y se rota al renovar. Logout revoca el refresh token. Los endpoints iniciales son `POST /api/v1/auth/register`, `/login`, `/refresh` y `/logout`.
