---
id: HU-004
tipo: historia-de-usuario
titulo: "Gestionar perfil y afiliación"
estado: Pendiente de aprobación
epica: "[[EP-002-perfiles-catalogos-y-profesionales]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2 — Preparación de agenda"
dependencias: ["[[HU-001-registrar-usuario]]"]
relacionadas: ["[[HU-005-administrar-catalogos-configurables]]"]
---
# HU-004 — Gestionar perfil y afiliación
## Historia de usuario
**COMO** USER, **QUIERO** consultar/actualizar mi perfil y asociar mi afiliación, **PARA** mantener mis datos de atención ficticios vigentes.
## Alcance
- Perfil permitido y afiliación a un plan EPS.
## Fuera de alcance
- Repetir nombres de EPS/régimen/plan en el usuario o facturación.
## Reglas de negocio
- La afiliación apunta a un plan; desde él se deriva EPS y régimen; vigencia de fechas válida.
## Dependencias y relaciones
- Épica: [[EP-002-perfiles-catalogos-y-profesionales]]
- Depende de: [[HU-001-registrar-usuario]], [[HU-005-administrar-catalogos-configurables]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** ownership, relación normalizada y validaciones de vigencia.
## Tareas de desarrollo
- [ ] **T-01 — Definir perfil y afiliación.** Dificultad: Medio. Implementar puertos, validaciones y migración si es necesaria.
- [ ] **T-02 — Exponer operaciones con ownership.** Dificultad: Medio. Restringir cambios al usuario dueño.
- [ ] **T-03 — Probar derivación normalizada.** Dificultad: Medio. Verificar afiliación válida y no duplicación de datos de catálogo.
## Criterios de aceptación
### CA-01 — Perfil propio
**Dado** un USER autenticado, **cuando** consulta o actualiza campos permitidos, **entonces** solo accede a su propio perfil y los cambios válidos persisten.
### CA-02 — Afiliación normalizada
**Dado** un plan EPS activo, **cuando** el USER lo asocia, **entonces** la afiliación referencia el plan y permite obtener EPS/régimen sin duplicar sus nombres.
### CA-03 — Datos inválidos
**Dado** datos de afiliación inválidos o un plan inactivo, **cuando** se intentan asociar, **entonces** la operación se rechaza.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas de ownership validadas.
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

