# Log de operaciones

| Fecha | Operación | Fuentes/páginas | Resultado |
|---|---|---|---|
| 2026-09-17 | INGEST inicial | SRC-PRD-001, SRC-TECH-001, SRC-DB-001, SRC-TRACE-001 | Estructura y páginas iniciales creadas tras aprobación del usuario |
| 2026-09-17 | DECISIÓN | HU-001 a HU-007; `contracts.md`, `decisions.md` | Contrato y corte backend de identidad aprobados; HU-033 conserva integración web |
| 2026-09-17 | LEARN/LINT | Flyway V1, pruebas MySQL 8.4; `data-integrity.md`, `traceability.md` | Corte 3FN y evidencia backend añadidos; RAW intacto, sin nuevos enlaces estructurales |
| 2026-09-22 | LEARN/DECISIÓN | `docs/FCV Dev/subagents/`, orquestador, `index.md`, `subagents.md`, arquitectura, decisiones, riesgos y trazabilidad | Ocho subagentes versionados; protocolo de delegación y nueva ubicación documental registrados; frontend React/Vite reconocido como trabajo pendiente de verificación |
| 2026-09-22 | LINT | Catálogo de subagentes y LLM Wiki | Enlaces Markdown relativos verificados; referencias operativas del orquestador actualizadas; se conserva como riesgo explícito la allowlist histórica de la Skill Scrum |
| 2026-09-22 | LEARN/DECISIÓN | HU-033, `contracts.md`, `traceability.md` | Corte React de autenticación integrado y validado contra API/MySQL; CORS explícito y compatibilidad Flyway con el esquema 3FN existente documentados; HU-033 queda en progreso. |
| 2026-09-22 | LINT | `data-integrity.md`, HU-005 a HU-007 | Evidencia actualizada a 9/9 pruebas Maven y modelo persistente `refresh_tokens`; integración web USER confirmada sin cerrar HU posteriores. |
| 2026-09-22 | DECISIÓN | HU-001, HU-002, HU-011, HU-014 a HU-024 y `contracts.md` | Cierre S2 confirmado para fundación y modelo 3FN; corte S3 aprobado con afiliación opcional, agenda real y contrato REST compartido. |
| 2026-09-23 | DECISIÓN/IMPLEMENTACIÓN | HU-011, `contracts.md`, `citas-api`, `citas-web` | Se aprueba y entrega `GET /api/v1/public/insurance-plans` para cargar planes activos antes del registro; el registro conserva `insurancePlanId` opcional, FK y error 400 controlado para planes inválidos/inactivos. |
| 2026-09-23 | IMPLEMENTACIÓN/VERIFICACIÓN | S4, HU-004, HU-021, `citas-api`, `citas-web` | Disponibilidad agrupada, payload de bloques alineado y UI ADMIN de profesionales/asignaciones implementados; frontend 13/13, lint y build pasan; backend unitario 9/9 pasa y la integración completa queda bloqueada por Docker inaccesible desde Testcontainers. |
| 2026-09-25 | LEARN/LINT | `README.md`, `docker-compose.yml`, `application.yml`, migraciones Flyway, arranque Docker y Vite | Procedimiento de despliegue local verificado; `deployment.md` añadido, enlazado desde `index.md`, sin secretos ni credenciales. |
| 2026-09-25 | LEARN/IMPLEMENTACIÓN | `.local/docker-compose.fq.yml`, `fq_mysql_data`, `fq-users.md` | Stack Docker aislado `fq` verificado en puertos 8081/5174; migraciones V1/V2 aplicadas desde cero y tres cuentas sintéticas creadas sin versionar credenciales. |
| 2026-09-25 | IMPLEMENTACIÓN | HU-025, `citas-api`, `citas-web` | Se añadió `GET /api/v1/appointments/me` con ownership y filtros de estado/fecha; el frontend muestra y filtra las citas del USER. Verificación de ejecución pendiente por Maven/Docker no disponibles en la sesión. |
