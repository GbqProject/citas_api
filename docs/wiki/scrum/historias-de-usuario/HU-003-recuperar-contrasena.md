---
id: HU-003
tipo: historia-de-usuario
titulo: "Recuperar contraseña"
estado: Pendiente de aprobación
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 4 — Ciclo de vida y cierre"
dependencias: ["[[HU-001-registrar-usuario]]"]
relacionadas: ["[[HU-002-iniciar-y-renovar-sesion]]"]
---
# HU-003 — Recuperar contraseña
## Historia de usuario
**COMO** usuario, **QUIERO** restablecer mi contraseña mediante un token temporal, **PARA** recuperar acceso sin exponer mi cuenta.
## Alcance
- Solicitud, token de un solo uso, cambio de password e invalidación/consumo.
## Fuera de alcance
- Integración SMTP obligatoria.
## Reglas de negocio
- El token expira, se usa una sola vez y se almacena como hash; el cambio invalida/consume el token.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]
- Depende de: [[HU-001-registrar-usuario]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** token seguro, flujo de recuperación y controles de expiración.
## Tareas de desarrollo
- [ ] **T-01 — Modelar tokens de recuperación.** Dificultad: Medio. Crear migración y persistencia hash con uso/expiración.
- [ ] **T-02 — Exponer solicitud y cambio.** Dificultad: Medio. Evitar filtración de cuentas y aplicar nuevo hash.
- [ ] **T-03 — Probar un solo uso.** Dificultad: Medio. Cubrir token válido, usado y vencido.
## Criterios de aceptación
### CA-01 — Solicitud controlada
**Dado** una solicitud de recuperación, **cuando** se procesa, **entonces** se genera un token temporal sin exponer datos sensibles.
### CA-02 — Cambio válido
**Dado** un token vigente sin usar, **cuando** se establece una contraseña válida, **entonces** el password cambia y el token queda consumido.
### CA-03 — Token no reutilizable
**Dado** un token vencido o consumido, **cuando** se intenta usar, **entonces** el cambio se rechaza.
## Definition of Done
- [ ] CA-01 a CA-03 y pruebas relevantes validadas.
- [ ] Token y password no se persisten en claro.
- [ ] Migración Flyway y trazabilidad Scrum actualizadas.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S2 — HU creada en estado `Pendiente de aprobación`.
