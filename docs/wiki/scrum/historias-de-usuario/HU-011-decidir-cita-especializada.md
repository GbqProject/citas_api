---
id: HU-011
tipo: historia-de-usuario
titulo: "Decidir cita especializada"
estado: Pendiente de aprobación
epica: "[[EP-004-solicitud-y-gestion-de-citas]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 3 — Reserva de citas"
dependencias: ["[[HU-010-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-012-consultar-y-cancelar-citas]]"]
---
# HU-011 — Decidir cita especializada
## Historia de usuario
**COMO** ADMIN, **QUIERO** aprobar o rechazar solicitudes especializadas, **PARA** controlar las citas que requieren decisión.
## Alcance
- Bandeja filtrable y transición de `REQUESTED` a `APPROVED`/`REJECTED`.
## Fuera de alcance
- Reprogramaciones.
## Reglas de negocio
- Rechazo exige motivo y libera slots; toda transición se audita con actor, fuente, fecha y motivo opcional.
## Dependencias y relaciones
- Épica: [[EP-004-solicitud-y-gestion-de-citas]]
- Depende de: [[HU-010-solicitar-cita-especializada]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** autorización, cola administrativa, transiciones y liberación atómica.
## Tareas de desarrollo
- [ ] **T-01 — Construir bandeja y filtros.** Dificultad: Medio. Filtrar por sede, profesional, especialidad y fecha.
- [ ] **T-02 — Aplicar decisión transaccional.** Dificultad: Medio. Registrar estado/historial y liberar al rechazar.
- [ ] **T-03 — Probar transiciones.** Dificultad: Medio. Cubrir autorización, motivo requerido y slots liberados.
## Criterios de aceptación
### CA-01 — Bandeja administrativa
**Dado** un ADMIN, **cuando** consulta solicitudes `REQUESTED`, **entonces** puede filtrarlas por sede, profesional, especialidad y fecha.
### CA-02 — Aprobación
**Dado** una solicitud pendiente, **cuando** ADMIN aprueba, **entonces** pasa a `APPROVED` y conserva sus slots.
### CA-03 — Rechazo auditable
**Dado** una solicitud pendiente, **cuando** ADMIN rechaza con motivo, **entonces** pasa a `REJECTED`, guarda historial y libera los slots.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de rol/transición validadas.
- [ ] Interfaz y contrato de la bandeja actualizados cuando correspondan.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S3 — HU creada en estado `Pendiente de aprobación`.

