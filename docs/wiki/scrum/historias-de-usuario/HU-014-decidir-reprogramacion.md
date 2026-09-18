---
id: HU-014
tipo: historia-de-usuario
titulo: "Decidir reprogramación"
estado: Pendiente de aprobación
epica: "[[EP-005-reprogramacion-y-cierre-de-atencion]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 4 — Ciclo de vida y cierre"
dependencias: ["[[HU-013-solicitar-reprogramacion]]"]
relacionadas: ["[[HU-012-consultar-y-cancelar-citas]]"]
---
# HU-014 — Decidir reprogramación
## Historia de usuario
**COMO** ADMIN, **QUIERO** aprobar o rechazar una reprogramación pendiente, **PARA** actualizar la cita sin inconsistencias de agenda.
## Alcance
- Bandeja pendiente, decisión con motivo aplicable, transferencia/liberación de slots y auditoría.
## Fuera de alcance
- Crear una cita nueva con otro profesional.
## Reglas de negocio
- Aprobación libera antigua, aplica nueva y actualiza cita; rechazo libera provisional y conserva original; usuario puede luego conservar/cancelar.
## Dependencias y relaciones
- Épica: [[EP-005-reprogramacion-y-cierre-de-atencion]]
- Depende de: [[HU-013-solicitar-reprogramacion]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** transición coordinada de múltiples reservas y consistencia bajo concurrencia.
## Tareas de desarrollo
- [ ] **T-01 — Implementar bandeja y autorización.** Dificultad: Medio. Listar y filtrar solicitudes `PENDING`.
- [ ] **T-02 — Aplicar decisión atómica.** Dificultad: Alto. Transferir/liberar slots y registrar auditoría.
- [ ] **T-03 — Probar ambos desenlaces.** Dificultad: Alto. Verificar horarios, estado y motivo cuando corresponda.
## Criterios de aceptación
### CA-01 — Aprobación
**Dado** una reprogramación pendiente, **cuando** ADMIN aprueba, **entonces** la cita usa la nueva franja, la antigua se libera y queda auditoría.
### CA-02 — Rechazo
**Dado** una reprogramación pendiente, **cuando** ADMIN rechaza con motivo aplicable, **entonces** se libera solo la franja provisional y la cita original se conserva.
### CA-03 — Bandeja y seguridad
**Dado** solicitudes pendientes, **cuando** un ADMIN las consulta, **entonces** las ve en su bandeja; otros roles no pueden decidirlas.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de ambos desenlaces validadas.
- [ ] Auditoría, contrato e interfaz aplicables actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S4 — HU creada en estado `Pendiente de aprobación`.
