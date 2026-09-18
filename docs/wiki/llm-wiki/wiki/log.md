# Registro de la LLM Wiki

- S2 — **INGEST**: se leyeron PRD, restricciones técnicas, requisitos de normalización, diseño 3FN y especificación Scrum. Se crearon índice, contexto, arquitectura, decisiones y riesgos iniciales.
- S2 — **LEARN**: se inicializó `citas-api` con Spring Boot/Maven y se verificó `mvn test` en el contenedor de desarrollo. Se registró DEC-004.
- S2 — **LEARN**: Flyway validó y aplicó `V1__initial_schema.sql` a MySQL 8.4; Spring Boot inició conectado a ese esquema. Se registró DEC-005.
- S2 — **LEARN**: se implementó el vertical slice de autenticación y cinco pruebas unitarias/JWT pasaron. Las HU-001 y HU-002 siguen en desarrollo hasta validar HTTP y persistencia. Se registró DEC-006.
