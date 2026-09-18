---
id: HU-015
tipo: historia-de-usuario
titulo: "Consultar agenda y cerrar atención"
estado: Pendiente de aprobación
epica: "[[EP-005-reprogramacion-y-cierre-de-atencion]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 4 — Ciclo de vida y cierre"
dependencias: ["[[HU-009-solicitar-cita-general]]", "[[HU-011-decidir-cita-especializada]]"]
relacionadas: []
---
# HU-015 — Consultar agenda y cerrar atención
## Historia de usuario
**COMO** PROFESSIONAL, **QUIERO** consultar mi agenda y marcar el resultado de atención, **PARA** mantener las citas aprobadas actualizadas.
## Alcance
- Agenda propia por día/semana/sede y transición a `COMPLETED` o `NO_SHOW` cuando aplique.
## Fuera de alcance
- Consulta de agendas ajenas, historia clínica y modificación de datos no propios.
## Reglas de negocio
- Solo muestra citas `APPROVED` del profesional; cambios se auditan y solo aplican a citas pasadas/aplicables.
## Dependencias y relaciones
- Épica: [[EP-005-reprogramacion-y-cierre-de-atencion]]
- Depende de: [[HU-009-solicitar-cita-general]], [[HU-011-decidir-cita-especializada]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** ownership profesional, filtros de agenda y transiciones de estado controladas.
## Tareas de desarrollo
- [ ] **T-01 — Consultar agenda propia.** Dificultad: Medio. Aplicar día/semana/sede y limitar datos a citas propias.
- [ ] **T-02 — Cerrar resultado.** Dificultad: Medio. Validar estado/fecha y auditar `COMPLETED`/`NO_SHOW`.
- [ ] **T-03 — Probar ownership y transición.** Dificultad: Medio. Cubrir acceso a otra agenda y cita no aplicable.
## Criterios de aceptación
### CA-01 — Agenda limitada
**Dado** un PROFESSIONAL autenticado, **cuando** consulta por día, semana o sede, **entonces** ve solo sus citas `APPROVED` correspondientes.
### CA-02 — Cierre válido
**Dado** una cita propia aplicable, **cuando** marca `COMPLETED` o `NO_SHOW`, **entonces** el estado cambia y se registra historial con actor/fuente/fecha.
### CA-03 — Protección de agenda
**Dado** una cita de otro profesional o no aplicable, **cuando** intenta consultarla/cerrarla, **entonces** se rechaza.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de ownership/transición validadas.
- [ ] Contrato, interfaz y trazabilidad aplicables actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S4 — HU creada en estado `Pendiente de aprobación`.

