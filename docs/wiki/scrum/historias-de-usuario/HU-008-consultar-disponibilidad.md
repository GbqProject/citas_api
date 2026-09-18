---
id: HU-008
tipo: historia-de-usuario
titulo: "Consultar disponibilidad"
estado: Pendiente de aprobación
epica: "[[EP-003-disponibilidad-y-agenda]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 3 — Reserva de citas"
dependencias: ["[[HU-007-publicar-disponibilidad]]"]
relacionadas: ["[[HU-009-solicitar-cita-general]]", "[[HU-010-solicitar-cita-especializada]]"]
---
# HU-008 — Consultar disponibilidad
## Historia de usuario
**COMO** USER, **QUIERO** filtrar horarios disponibles, **PARA** seleccionar una franja que complete mi cita.
## Alcance
- Filtros por sede, tipo, especialidad, profesional y fecha; resultado según duración necesaria.
## Fuera de alcance
- Confirmación de una reserva.
## Reglas de negocio
- Solo se muestran especialistas activos/asignados y slots libres consecutivos necesarios para 30/60 minutos.
## Dependencias y relaciones
- Épica: [[EP-003-disponibilidad-y-agenda]]
- Depende de: [[HU-007-publicar-disponibilidad]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** filtros combinados y cálculo correcto de secuencia/disponibilidad.
## Tareas de desarrollo
- [ ] **T-01 — Diseñar consulta de agenda.** Dificultad: Alto. Aplicar filtros y asociaciones válidas.
- [ ] **T-02 — Calcular franjas reservables.** Dificultad: Alto. Considerar duración y reservas vivas.
- [ ] **T-03 — Probar filtros y consecutividad.** Dificultad: Alto. Incluir 30/60 y horarios parciales.
## Criterios de aceptación
### CA-01 — Filtros aplicados
**Dado** filtros válidos, **cuando** USER busca, **entonces** solo recibe horarios de profesionales/sedes/especialidades que coinciden.
### CA-02 — Duración completa
**Dado** una especialidad de 30 o 60 minutos, **cuando** se muestran resultados, **entonces** cada horario tiene uno o dos slots consecutivos libres respectivamente.
### CA-03 — Exclusión de ocupados
**Dado** slots reservados o retenidos, **cuando** se consulta, **entonces** no se presentan como disponibles.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de slots 30/60 validadas.
- [ ] Contrato REST, interfaz aplicable y trazabilidad actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S3 — HU creada en estado `Pendiente de aprobación`.

