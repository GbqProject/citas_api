---
id: HU-012
tipo: historia-de-usuario
titulo: "Consultar y cancelar citas"
estado: Pendiente de aprobación
epica: "[[EP-004-solicitud-y-gestion-de-citas]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 4 — Ciclo de vida y cierre"
dependencias: ["[[HU-009-solicitar-cita-general]]", "[[HU-010-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-013-solicitar-reprogramacion]]"]
---
# HU-012 — Consultar y cancelar citas
## Historia de usuario
**COMO** USER, **QUIERO** ver y cancelar mis citas futuras permitidas, **PARA** administrar mis compromisos.
## Alcance
- Listado/filtros, detalle mínimo, cancelación y auditoría.
## Fuera de alcance
- Reactivación y reprogramación.
## Reglas de negocio
- Mostrar sede, profesional, especialidad, fecha/hora, duración, estado y motivo; cancelación futura no terminal libera slots y no reactiva directamente.
## Dependencias y relaciones
- Épica: [[EP-004-solicitud-y-gestion-de-citas]]
- Depende de: [[HU-009-solicitar-cita-general]], [[HU-010-solicitar-cita-especializada]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** ownership, reglas de estado y sincronía con reservas/auditoría.
## Tareas de desarrollo
- [ ] **T-01 — Consultar citas propias.** Dificultad: Medio. Exponer filtros por estado/fecha y datos mínimos.
- [ ] **T-02 — Cancelar con reglas.** Dificultad: Medio. Validar futuro/no terminal y liberar slots.
- [ ] **T-03 — Probar ownership e historial.** Dificultad: Medio. Cubrir citas de otro usuario y estados no cancelables.
## Criterios de aceptación
### CA-01 — Mis citas
**Dado** un USER autenticado, **cuando** consulta sus citas, **entonces** visualiza solo las propias con los datos mínimos y filtros solicitados.
### CA-02 — Cancelación válida
**Dado** una cita propia futura no terminal, **cuando** USER la cancela, **entonces** queda `CANCELLED`, se liberan slots y se audita el cambio.
### CA-03 — Cancelación inválida
**Dado** una cita ajena, pasada o terminal, **cuando** se intenta cancelar, **entonces** se rechaza sin cambiar la cita.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de ownership/estados validadas.
- [ ] Contrato e interfaz aplicables actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S4 — HU creada en estado `Pendiente de aprobación`.

