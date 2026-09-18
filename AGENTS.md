# AGENTS.md — `citas-api`

## Evidencia del repositorio

El backend usa Java 21, Spring Boot 3.5.0, Maven, Spring Web/Validation/Security/Data JPA, MySQL, Flyway y Actuator. La configuración está en `src/main/resources/application.yml` y toma secretos/configuración de variables de entorno.

## Arquitectura

- `domain/`: conceptos e invariantes; sin Spring, JPA, HTTP ni adaptadores.
- `application/`: casos de uso y puertos; orquesta el dominio.
- `infrastructure/adapters/in/rest/`: adaptadores HTTP y DTOs; no reglas de negocio.
- `infrastructure/adapters/out/`: JPA, MySQL e integraciones externas.

No acoplar este backend a un framework de `citas-web` ni crear Express/BFF.

## Flujo de cambios

1. Leer la HU aprobada, CA y DoD en `docs/wiki/scrum/`.
2. Identificar reglas del PRD, contrato, persistencia y pruebas; proponer plan antes de editar.
3. Todo cambio de esquema usa una nueva migración Flyway en `src/main/resources/db/migration/`; no alterar esquemas manualmente.
4. Ejecutar pruebas relevantes y registrar evidencia; no declarar una HU completada sin ella.

## Seguridad

- Passwords con hash adaptativo; access/refresh/reset nunca se persisten ni registran en claro.
- Secretos solo por entorno o `.env` no versionado: no leer ni imprimir `.env`.
- Aplicar autenticación, autorización por rol/ownership y validación server-side.
- Mantener CORS explícito y no registrar datos sensibles.

La LLM Wiki global vive en `docs/wiki/llm-wiki/`; no crear una wiki paralela.
