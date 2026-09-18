---
id: HU-009
tipo: historia-de-usuario
titulo: "Solicitar cita general"
estado: Pendiente de aprobación
epica: "[[EP-004-solicitud-y-gestion-de-citas]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 3 — Reserva de citas"
dependencias: ["[[HU-008-consultar-disponibilidad]]"]
relacionadas: ["[[HU-010-solicitar-cita-especializada]]"]
---
# HU-009 — Solicitar cita general
## Historia de usuario
**COMO** USER, **QUIERO** confirmar una cita de Medicina General disponible, **PARA** obtener atención sin aprobación administrativa.
## Alcance
- Confirmación, reserva de slots y estado `APPROVED` automático.
## Fuera de alcance
- Especialidades y reprogramación.
## Reglas de negocio
- Debe elegirse Medicina General y un profesional disponible; no puede existir doble reserva.
## Dependencias y relaciones
- Épica: [[EP-004-solicitud-y-gestion-de-citas]]
- Depende de: [[HU-008-consultar-disponibilidad]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** reserva transaccional, concurrencia y auditoría de estado.
## Tareas de desarrollo
- [ ] **T-01 — Crear reserva atómica.** Dificultad: Alto. Bloquear/verificar slots e insertar cita/reservas.
- [ ] **T-02 — Registrar aprobación y auditoría.** Dificultad: Medio. Asignar `APPROVED` y su historial.
- [ ] **T-03 — Probar concurrencia.** Dificultad: Alto. Demostrar que dos solicitudes no ocupan el mismo slot.
## Criterios de aceptación
### CA-01 — Aprobación automática
**Dado** un horario general disponible, **cuando** USER lo confirma, **entonces** se crea una cita `APPROVED` con sus slots reservados.
### CA-02 — Prevención de doble reserva
**Dado** dos intentos sobre el mismo horario, **cuando** se procesan concurrentemente, **entonces** como máximo uno obtiene la reserva.
### CA-03 — Auditoría
**Dado** una cita general creada, **cuando** se consulta su historial, **entonces** existe el cambio inicial con fuente y fecha.
## Definition of Done
- [ ] CA-01 a CA-03 y prueba de doble reserva validadas.
- [ ] Persistencia, migración si aplica, contrato e interfaz relevantes actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S3 — HU creada en estado `Pendiente de aprobación`.

