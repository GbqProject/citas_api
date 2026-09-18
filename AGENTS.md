# AGENTS.md — `citas-api`

## Alcance y estado comprobado

Este repositorio contiene la API REST del laboratorio ficticio de citas FCV. La interfaz vive en el repositorio independiente `../citas-web`; esta API no se acopla a React ni Angular y no crea Express ni BFF.

El proyecto usa Java 21, Maven y Spring Boot 3.5.0. `pom.xml` incluye Web, Validation, Security, OAuth2 JOSE, Data JPA, MySQL, Flyway y Actuator. La configuración se encuentra en `src/main/resources/application.yml`; su conexión MySQL, CORS y parámetros de JWT se resuelven por variables de entorno.

El vertical existente corresponde a autenticación:

- `POST /api/v1/auth/register`, `/login`, `/refresh` y `/logout`;
- `AuthService` y los puertos `AuthPersistencePort` / `AccessTokenPort`;
- `JdbcAuthPersistenceAdapter`, `JwtAccessTokenAdapter`, `SecurityConfig` y validación HTTP;
- `V1__initial_schema.sql`, con tablas de identidad, refresh tokens y el modelo inicial 3FN;
- pruebas unitarias de servicio de autenticación y emisión JWT.

Las HU-001 y HU-002 están **En validación**: sus criterios y Definition of Done no se consideran cerrados sin nueva evidencia. HU-016 es cross-repo y no autoriza cambios en `citas-web` desde aquí.

## Límites arquitectónicos

- `domain/` contiene conceptos e invariantes del negocio: no debe depender de Spring, JPA, HTTP, JDBC ni DTOs.
- `application/` contiene casos de uso y puertos. La lógica de reglas del PRD se implementa aquí o en dominio, nunca en un controlador ni en SQL de un adaptador.
- `infrastructure/adapters/in/rest/` traduce HTTP/JSON, validación y respuestas de error; no concentra reglas de negocio.
- `infrastructure/adapters/out/` implementa puertos de persistencia o integraciones. El adaptador actual de autenticación usa `JdbcTemplate`; nuevas decisiones de persistencia deben respetar la restricción de Spring Data JPA o dejar su justificación explícita en el cambio.
- Los contratos REST se diseñan con la HU aprobada, sus criterios de aceptación y las reglas aplicables del `../PRD.md`. Un cambio observable de contrato requiere coordinación y evidencia en el consumidor cuando exista.

## Datos, seguridad y operaciones

- Toda evolución del esquema se realiza con una migración Flyway nueva en `src/main/resources/db/migration/`; no editar una migración aplicada ni sustituirla con SQL manual.
- Mantener 3FN, seeds solo para catálogos fijos y datos exclusivamente sintéticos. Para reservas futuras, preservar las reglas de transacción y no doble reserva del PRD.
- Passwords usan hash adaptativo. Los refresh tokens se persisten únicamente como hash; access y refresh son distintos.
- Secretos, contraseñas y tokens proceden exclusivamente del entorno. No abrir, imprimir ni versionar `.env`; solo se puede actualizar `.env.example` con valores no sensibles cuando sea necesario.
- No registrar passwords, tokens, hashes sensibles ni datos personales innecesarios. Mantener CORS explícito, validación server-side, autorización por rol y ownership conforme se incorporen endpoints.

## Flujo de cambio

1. Localizar la HU aprobada en `docs/wiki/scrum/historias-de-usuario/`, revisar criterios, DoD y el PRD/restricciones aplicables.
2. Identificar los contratos, reglas, puertos, adaptadores, migraciones y pruebas afectados; proponer el plan antes de editar.
3. Implementar el mínimo coherente dentro de este repositorio, sin modificar `../citas-web`.
4. Ejecutar `mvn test` y las pruebas de integración REST/persistencia que correspondan al alcance. Informar explícitamente lo que no se haya podido verificar.
5. Contrastar el resultado con la DoD. No alterar el estado de HU ni mantener una wiki paralela: `docs/wiki/llm-wiki/` es memoria global del orquestador.

## Git y automatizaciones

- `main` es estable y `develop` es la rama de trabajo definida por el workspace; no reescribir historial.
- Los workflows n8n futuros se versionan como JSON en `automations/n8n/`; no incluir credenciales.
