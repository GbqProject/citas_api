---
id: HU-006
tipo: historia-de-usuario
titulo: "Administrar profesionales"
estado: Pendiente de aprobación
epica: "[[EP-002-perfiles-catalogos-y-profesionales]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 2 — Preparación de agenda"
dependencias: ["[[HU-005-administrar-catalogos-configurables]]"]
relacionadas: ["[[HU-007-publicar-disponibilidad]]"]
---
# HU-006 — Administrar profesionales
## Historia de usuario
**COMO** ADMIN, **QUIERO** crear y configurar profesionales ficticios, **PARA** que puedan publicar agenda y recibir citas de sus especialidades/sedes.
## Alcance
- Usuario con rol PROFESSIONAL, código/matrícula, activación, especialidades (una primaria) y sedes.
## Fuera de alcance
- Auto-registro de profesionales y datos reales de FCV.
## Reglas de negocio
- Un profesional es un usuario especializado; puede tener varias especialidades y sedes, con máximo una primaria.
## Dependencias y relaciones
- Épica: [[EP-002-perfiles-catalogos-y-profesionales]]
- Depende de: [[HU-005-administrar-catalogos-configurables]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** combina identidad, roles, relación N:M, restricciones de catálogo y seguridad administrativa.
## Tareas de desarrollo
- [ ] **T-01 — Crear perfil profesional.** Dificultad: Alto. Aplicar rol, código/matrícula ficticios y estado.
- [ ] **T-02 — Gestionar asignaciones.** Dificultad: Alto. Mantener especialidades, única primaria y sedes.
- [ ] **T-03 — Probar autorización e integridad.** Dificultad: Medio. Validar roles, catálogos activos y restricciones N:M.
## Criterios de aceptación
### CA-01 — Creación administrativa
**Dado** un ADMIN, **cuando** crea un profesional con datos válidos, **entonces** se crea un usuario especializado con rol PROFESSIONAL y credenciales profesionales únicas.
### CA-02 — Asignaciones válidas
**Dado** un profesional, **cuando** ADMIN asigna especialidades y sedes activas, **entonces** puede conservar múltiples relaciones y solo una especialidad primaria.
### CA-03 — Activación controlada
**Dado** un profesional, **cuando** ADMIN lo desactiva, **entonces** no queda disponible para nueva agenda o reserva sin destruir sus referencias históricas.
## Definition of Done
- [ ] CA-01 a CA-03 validados con pruebas de autorización y relaciones N:M.
- [ ] Migración Flyway, si aplica, y trazabilidad actualizadas.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S2 — HU creada en estado `Pendiente de aprobación`.
