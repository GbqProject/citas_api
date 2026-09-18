---
id: HU-010
tipo: historia-de-usuario
titulo: "Solicitar cita especializada"
estado: Pendiente de aprobación
epica: "[[EP-004-solicitud-y-gestion-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 3 — Reserva de citas"
dependencias: ["[[HU-008-consultar-disponibilidad]]"]
relacionadas: ["[[HU-011-decidir-cita-especializada]]"]
---
# HU-010 — Solicitar cita especializada
## Historia de usuario
**COMO** USER, **QUIERO** solicitar una cita especializada disponible, **PARA** que ADMIN la evalúe conservando el horario.
## Alcance
- Selección de especialidad/sede/profesional/horario, estado `REQUESTED` y retención de slots.
## Fuera de alcance
- Decisión administrativa y reprogramación.
## Reglas de negocio
- Especialidad activa y asignada al profesional; la solicitud retiene los slots y evita doble reserva.
## Dependencias y relaciones
- Épica: [[EP-004-solicitud-y-gestion-de-citas]]
- Depende de: [[HU-008-consultar-disponibilidad]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** validación de asociaciones, transacción concurrente y estado pendiente.
## Tareas de desarrollo
- [ ] **T-01 — Validar selección especializada.** Dificultad: Alto. Verificar asociaciones y duración.
- [ ] **T-02 — Retener slots y crear solicitud.** Dificultad: Alto. Ejecutar reserva y estado `REQUESTED` de forma atómica.
- [ ] **T-03 — Probar rechazo de disponibilidad inválida.** Dificultad: Alto. Cubrir especialidad/sede/slot no elegibles.
## Criterios de aceptación
### CA-01 — Solicitud retenida
**Dado** una selección especializada válida y disponible, **cuando** USER confirma, **entonces** se crea `REQUESTED` y sus slots quedan retenidos.
### CA-02 — Asociaciones válidas
**Dado** una especialidad inactiva o no asignada al profesional/sede, **cuando** USER intenta solicitarla, **entonces** se rechaza.
### CA-03 — Exclusividad
**Dado** una solicitud especializada retenida, **cuando** otro usuario consulta o reserva, **entonces** sus slots no pueden ocuparlos otra cita/solicitud.
## Definition of Done
- [ ] CA-01 a CA-03 y prueba de doble reserva validadas.
- [ ] Historial inicial, contrato e interfaz relevantes actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S3 — HU creada en estado `Pendiente de aprobación`.
