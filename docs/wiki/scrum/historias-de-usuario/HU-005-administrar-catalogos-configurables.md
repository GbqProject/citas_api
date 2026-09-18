---
id: HU-005
tipo: historia-de-usuario
titulo: "Administrar catálogos configurables"
estado: Pendiente de aprobación
epica: "[[EP-002-perfiles-catalogos-y-profesionales]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2 — Preparación de agenda"
dependencias: ["[[HU-002-iniciar-y-renovar-sesion]]"]
relacionadas: ["[[HU-004-gestionar-perfil-y-afiliacion]]", "[[HU-006-administrar-profesionales]]"]
---
# HU-005 — Administrar catálogos configurables
## Historia de usuario
**COMO** ADMIN, **QUIERO** gestionar EPS, planes y especialidades, **PARA** mantener opciones disponibles para afiliación y agenda.
## Alcance
- CRUD de EPS, planes y especialidades, incluidas duración 30/60 y activación.
## Fuera de alcance
- CRUD de roles, estados, regímenes y sedes fijas; borrado físico de catálogos referenciados.
## Reglas de negocio
- EPS se asocia a régimen; plan a EPS; especialidad define 30 o 60 minutos; referencias se preservan desactivando.
## Dependencias y relaciones
- Épica: [[EP-002-perfiles-catalogos-y-profesionales]]
- Depende de: [[HU-002-iniciar-y-renovar-sesion]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** autorizaciones, integridad referencial y reglas de configurabilidad.
## Tareas de desarrollo
- [ ] **T-01 — Modelar operaciones administrativas.** Dificultad: Medio. Validar relaciones EPS-plan y duración.
- [ ] **T-02 — Implementar activación segura.** Dificultad: Medio. Evitar borrar catálogos referenciados.
- [ ] **T-03 — Probar restricciones de rol e integridad.** Dificultad: Medio. Cubrir ADMIN/no ADMIN y referencias.
## Criterios de aceptación
### CA-01 — Gestión autorizada
**Dado** un ADMIN, **cuando** crea o actualiza EPS, plan o especialidad válida, **entonces** el catálogo queda disponible según su estado.
### CA-02 — Duración y relaciones válidas
**Dado** una especialidad o plan, **cuando** se guarda, **entonces** su duración es 30/60 y el plan pertenece a una EPS con régimen.
### CA-03 — Conservación de referencias
**Dado** un catálogo usado por datos transaccionales, **cuando** deja de ofrecerse, **entonces** se desactiva sin borrar la referencia histórica.
## Definition of Done
- [ ] CA-01 a CA-03 validados con pruebas de autorización e integridad.
- [ ] Migración Flyway, si aplica, y documentación actualizadas.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S2 — HU creada en estado `Pendiente de aprobación`.

