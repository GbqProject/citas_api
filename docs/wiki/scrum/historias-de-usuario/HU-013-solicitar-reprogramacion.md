---
id: HU-013
tipo: historia-de-usuario
titulo: "Solicitar reprogramación"
estado: Pendiente de aprobación
epica: "[[EP-005-reprogramacion-y-cierre-de-atencion]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4 — Ciclo de vida y cierre"
dependencias: ["[[HU-012-consultar-y-cancelar-citas]]"]
relacionadas: ["[[HU-014-decidir-reprogramacion]]"]
---
# HU-013 — Solicitar reprogramación
## Historia de usuario
**COMO** USER, **QUIERO** proponer un nuevo horario para una cita aprobada, **PARA** cambiarla sin perder la cita original mientras se decide.
## Alcance
- Solicitud pendiente, misma especialidad/profesional, nueva franja retenida y original conservada.
## Fuera de alcance
- Cambio de profesional y decisión administrativa.
## Reglas de negocio
- Solo cita aprobada/futura; una reprogramación conserva profesional/especialidad; nueva franja se retiene y original permanece.
## Dependencias y relaciones
- Épica: [[EP-005-reprogramacion-y-cierre-de-atencion]]
- Depende de: [[HU-012-consultar-y-cancelar-citas]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** dos juegos de slots, restricción de estado y consistencia transaccional.
## Tareas de desarrollo
- [ ] **T-01 — Validar elegibilidad y propuesta.** Dificultad: Alto. Comprobar cita, profesional/especialidad y nuevos slots.
- [ ] **T-02 — Retener propuesta.** Dificultad: Alto. Crear solicitud `PENDING` sin liberar reserva original.
- [ ] **T-03 — Probar conservación.** Dificultad: Alto. Verificar que ambas franjas permanecen ocupadas mientras está pendiente.
## Criterios de aceptación
### CA-01 — Solicitud elegible
**Dado** una cita propia aprobada y futura, **cuando** USER propone una franja disponible del mismo profesional/especialidad, **entonces** se crea una reprogramación `PENDING`.
### CA-02 — Conservación de cita original
**Dado** una solicitud pendiente, **cuando** se consulta agenda, **entonces** los slots originales y propuestos están retenidos.
### CA-03 — Restricciones
**Dado** cita no aprobada/pasada, profesional distinto o franja no disponible, **cuando** USER solicita, **entonces** se rechaza.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de retención validadas.
- [ ] Migración, contrato e interfaz aplicables actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S4 — HU creada en estado `Pendiente de aprobación`.

