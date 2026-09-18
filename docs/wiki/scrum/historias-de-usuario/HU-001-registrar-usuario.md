---
id: HU-001
tipo: historia-de-usuario
titulo: "Registrar usuario"
estado: En validación
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 1 — Fundación y autenticación"
dependencias: []
relacionadas: ["[[HU-002-iniciar-y-renovar-sesion]]"]
---
# HU-001 — Registrar usuario
## Historia de usuario
**COMO** visitante, **QUIERO** crear una cuenta USER con mis datos mínimos, **PARA** poder acceder y solicitar citas ficticias.
## Alcance
- Registro de USER con nombres, apellidos, tipo/número de documento, email, teléfono y contraseña.
- Validación de unicidad y almacenamiento seguro de contraseña.
## Fuera de alcance
- Registro de profesionales, afiliación y recuperación de contraseña.
## Reglas de negocio
- Email y combinación tipo/número de documento son únicos; el password no se guarda ni expone en claro.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]
- Relacionada: [[HU-002-iniciar-y-renovar-sesion]]
## Esfuerzo
**Nivel:** Medio. **Justificación:** abarca validación, persistencia, seguridad y contrato observable.
## Tareas de desarrollo
- [ ] **T-01 — Modelar el registro y la persistencia.** Dificultad: Medio. Crear capas hexagonales, validaciones y migración Flyway coherente.
- [ ] **T-02 — Exponer el registro protegido.** Dificultad: Medio. Definir contrato REST, hash adaptativo y asignación del rol USER.
- [ ] **T-03 — Probar flujos válidos e inválidos.** Dificultad: Medio. Cubrir unicidad y ausencia de password en respuestas/logs.
## Criterios de aceptación
### CA-01 — Registro válido
**Dado** un visitante con datos válidos y únicos, **cuando** se registra, **entonces** queda creada una cuenta activa con rol USER sin devolver su password.
### CA-02 — Unicidad
**Dado** un email o documento ya registrado, **cuando** se intenta registrar otra cuenta, **entonces** la operación se rechaza sin crear un usuario adicional.
### CA-03 — Protección de contraseña
**Dado** un registro exitoso, **cuando** se inspecciona la persistencia, **entonces** la contraseña está almacenada como hash adaptativo, no en texto plano.
## Definition of Done
- [ ] CA-01 a CA-03 validados con evidencia.
- [ ] La migración Flyway requerida es coherente con el modelo 3FN.
- [ ] Existen pruebas relevantes de registro y unicidad.
- [ ] Contrato y trazabilidad Scrum actualizados.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S2 — HU creada en estado `Pendiente de aprobación`.
- S2 — Aprobada explícitamente por el usuario para el incremento de autenticación.
