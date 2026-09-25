---
id: HU-025
tipo: historia-de-usuario
titulo: "Consultar mis citas"
estado: Aprobada
epica: "[[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 5"
dependencias: ["[[HU-022-reservar-cita-general]]", "[[HU-023-solicitar-cita-especializada]]"]
relacionadas: ["[[HU-026-cancelar-cita]]", "[[HU-027-solicitar-reprogramacion]]"]
---
# HU-025 — Consultar mis citas
## Historia de usuario
**COMO** USER  
**QUIERO** consultar y filtrar mis citas por estado y fecha  
**PARA** conocer su detalle y las acciones disponibles.
## Contexto y descripción
Debe mostrar sede, profesional, especialidad, fecha/hora, duración, estado y motivo de rechazo cuando exista.
## Alcance
- Listado/detalle de citas propias y filtros de estado/fecha.
## Fuera de alcance
- Citas de otro USER, información clínica o acciones de ADMIN.
## Reglas de negocio
- Ownership; el motivo de rechazo solo se visualiza si existe.
## Dependencias y relaciones
- Épica: [[EP-006-ciclo-de-vida-de-citas-y-reprogramaciones]]
- Dependencias: [[HU-022-reservar-cita-general]], [[HU-023-solicitar-cita-especializada]].
- Relacionadas: [[HU-026-cancelar-cita]], [[HU-027-solicitar-reprogramacion]].
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** combina filtros, ownership y representación de estados/auditoría.
## Tareas de desarrollo
- [ ] **T-01 — Definir consulta/detalle.** Dificultad: Medio. Acordar filtros y campos obligatorios.
- [ ] **T-02 — Aplicar ownership y composición.** Dificultad: Medio. Obtener relaciones sin exponer citas ajenas.
- [ ] **T-03 — Entregar pantalla/pruebas.** Dificultad: Medio. Cubrir filtros, rechazo y aislamiento.
## Criterios de aceptación
### CA-01 — Datos mínimos
**Dado** citas propias, **cuando** USER las consulta, **entonces** ve sede, profesional, especialidad, fecha/hora, duración y estado.
### CA-02 — Filtros y motivo
**Dado** varias citas, **cuando** filtra por estado/fecha, **entonces** obtiene coincidencias y ve motivo de rechazo cuando exista.
### CA-03 — Ownership
**Dado** un USER, **cuando** intenta consultar detalle de cita ajena, **entonces** no recibe sus datos.
## Definition of Done
- [ ] CA-01 a CA-03 probados en REST/ownership y cliente aplicable.
- [ ] Contrato no expone información fuera del PRD; trazabilidad actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Implementado | `GET /api/v1/appointments/me`; `UserHome` | Lista datos mínimos de citas propias. |
| CA-02 | Implementado | filtros `status`, `from`, `to`; `UserHome` | Muestra motivo solo para citas rechazadas. |
| CA-03 / DoD | Implementado | filtro `patient_user_id=?`; `appointmentsApi.mine` | La consulta se limita al usuario autenticado. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-25 — Aprobada por solicitud del usuario; contrato REST y cliente implementados. Pendiente ejecución de pruebas con Maven/Docker en el entorno actual.
## Notas y decisiones
- Las pantallas se incorporan al cliente sin prescribir framework.
