# Arquitectura y límites

## HECHO — Backend objetivo

Java 21 LTS, Spring Boot 3.5.x, Maven, arquitectura hexagonal, Spring Data JPA, MySQL 8.4, Flyway, Spring Security y REST/JSON.

## HECHO — Frontend objetivo

TypeScript con React o Angular después de la importación desde AI Studio. No hay Express ni BFF: el frontend consume REST directamente contra `citas-api` y configura su URL por entorno.

## DECISIÓN — Datos y seguridad

La propuesta propia normalizada se mantiene en `database/database_v1`. La aplicación deberá expresar el esquema mediante migraciones Flyway. Passwords requieren hash adaptativo; access y refresh JWT son separados; refresh y reset se almacenan como hash.

## HECHO — Límites de repositorio

`citas-api` y `citas-web` son repositorios Git independientes. La raíz coordina y no se convierte en un tercer repositorio.

Fuentes: `README.md`, `RESTRICCIONES_TECNICAS.md`, `database/REQUISITOS_NORMALIZACION_3FN.md` y `database/database_v1/`.
