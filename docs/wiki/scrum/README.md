# Mapa Scrum — Sistema de citas FCV

Estado del mapa: HU-001, HU-002 y HU-016 están **Aprobadas para S2**; las demás permanecen `Pendiente de aprobación`. Esta planificación se basa en `PRD.md`, `RESTRICCIONES_TECNICAS.md` y los requisitos de normalización; no contiene implementación.

## Stack detectado y supuestos

- Backend objetivo: Java 21, Spring Boot 3.5.x, Maven, arquitectura hexagonal, MySQL 8.4 y Flyway.
- Frontend: React o Angular aún no seleccionado; cada HU de interfaz conserva esta decisión como dependencia.
- La base de datos propia 3FN está documentada en `database/database_v1`; la aplicación deberá incorporarla mediante migraciones Flyway cuando se implemente.

## Épicas

- [[EP-001-identidad-y-acceso]]
- [[EP-002-perfiles-catalogos-y-profesionales]]
- [[EP-003-disponibilidad-y-agenda]]
- [[EP-004-solicitud-y-gestion-de-citas]]
- [[EP-005-reprogramacion-y-cierre-de-atencion]]
- [[EP-006-experiencia-web-e-integracion]]

## Incrementos sugeridos

1. **Fundación y autenticación:** HU-001, HU-002 y HU-015. S2 selecciona inicialmente HU-001 y HU-002.
2. **Preparación de agenda:** HU-003 a HU-006.
3. **Reserva de citas:** HU-007 a HU-010.
4. **Ciclo de vida y cierre:** HU-011 a HU-014.

## Decisiones o riesgos pendientes

- Elegir React o Angular tras el handoff de Stitch/AI Studio.
- Definir duraciones y rotación de secretos JWT mediante configuración de entorno, sin documentar valores secretos.
- Acordar qué transiciones de estado se autorizan desde cada rol; el PRD define las esenciales y las HU las acotan.
- Las validaciones de solapamiento y reserva concurrente requieren transacciones y pruebas de integración al implementarse.
