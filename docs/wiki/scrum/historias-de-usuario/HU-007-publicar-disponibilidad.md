---
id: HU-007
tipo: historia-de-usuario
titulo: "Publicar disponibilidad"
estado: Pendiente de aprobación
epica: "[[EP-003-disponibilidad-y-agenda]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2 — Preparación de agenda"
dependencias: ["[[HU-006-administrar-profesionales]]"]
relacionadas: ["[[HU-008-consultar-disponibilidad]]"]
---
# HU-007 — Publicar disponibilidad
## Historia de usuario
**COMO** PROFESSIONAL, **QUIERO** crear y administrar mis bloques futuros por sede, **PARA** ofrecer horarios reservables.
## Alcance
- Crear múltiples bloques futuros y generar slots de 30 minutos; editar/eliminar futuros sin citas comprometidas.
## Fuera de alcance
- Reserva de pacientes y modificación de bloques históricos/comprometidos.
## Reglas de negocio
- No pasado ni solapamiento; la sede debe estar asignada al profesional; bloque se discretiza en slots de 30 minutos.
## Dependencias y relaciones
- Épica: [[EP-003-disponibilidad-y-agenda]]
- Depende de: [[HU-006-administrar-profesionales]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** reglas de calendario entre filas, generación de slots e integridad ante citas.
## Tareas de desarrollo
- [ ] **T-01 — Modelar bloques y slots.** Dificultad: Alto. Generar slots atómicos en transacción.
- [ ] **T-02 — Aplicar reglas de agenda.** Dificultad: Alto. Comprobar futuro, sede asignada y solapamiento.
- [ ] **T-03 — Probar operaciones protegidas.** Dificultad: Alto. Cubrir solapes y bloqueo de modificaciones comprometidas.
## Criterios de aceptación
### CA-01 — Bloque válido
**Dado** un profesional activo habilitado en una sede, **cuando** crea un bloque futuro válido, **entonces** quedan disponibles sus slots de 30 minutos.
### CA-02 — Restricciones de agenda
**Dado** un bloque pasado, solapado o en sede no asignada, **cuando** se intenta crear, **entonces** se rechaza.
### CA-03 — Protección de compromiso
**Dado** un bloque futuro con slots comprometidos, **cuando** se intenta eliminar o editar de forma incompatible, **entonces** la operación se rechaza.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de solape/slots validadas.
- [ ] Migración Flyway, si aplica, y trazabilidad actualizadas.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S3 — HU creada en estado `Pendiente de aprobación`.

