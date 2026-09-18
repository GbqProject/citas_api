---
id: HU-016
tipo: historia-de-usuario
titulo: "Importar y conectar frontend"
estado: Aprobada
epica: "[[EP-006-experiencia-web-e-integracion]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1 — Fundación y autenticación"
dependencias: ["[[HU-002-iniciar-y-renovar-sesion]]"]
relacionadas: ["[[HU-001-registrar-usuario]]"]
---
# HU-016 — Importar y conectar frontend
## Historia de usuario
**COMO** visitante o usuario, **QUIERO** usar las pantallas aprobadas de registro y login, **PARA** acceder al sistema desde una interfaz web ejecutable.
## Alcance
- Diseño en Stitch, aprobación explícita, handoff AI Studio, importación a `citas-web`, ejecución y conexión REST directa de registro/login.
## Fuera de alcance
- Express/BFF, selección arbitraria de framework y pantallas posteriores sin diseño aprobado.
## Reglas de negocio
- React o Angular se decide según export de AI Studio; URL de API por entorno; frontend consume Spring Boot directamente.
## Dependencias y relaciones
- Épica: [[EP-006-experiencia-web-e-integracion]]
- Depende de: [[HU-002-iniciar-y-renovar-sesion]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** ciclo de diseño, selección condicionada de framework, integración de contrato y validación visual/ejecutable.
## Tareas de desarrollo
- [ ] **T-01 — Diseñar y aprobar pantallas.** Dificultad: Medio. Producir login/registro en Stitch y registrar aprobación explícita.
- [ ] **T-02 — Importar el proyecto real.** Dificultad: Alto. Hacer handoff de AI Studio, detectar React/Angular y configurar ejecución.
- [ ] **T-03 — Integrar autenticación.** Dificultad: Alto. Consumir registro/login de Spring Boot con URL por entorno y manejar resultados sin guardar secretos inseguros.
- [ ] **T-04 — Verificar frontend.** Dificultad: Medio. Ejecutar build/typecheck/pruebas aplicables y actualizar contrato.
## Criterios de aceptación
### CA-01 — Diseño aprobado
**Dado** el conjunto de pantallas iniciales, **cuando** se revisa en Stitch, **entonces** login y registro tienen aprobación explícita antes del handoff.
### CA-02 — Proyecto ejecutable
**Dado** el export de AI Studio, **cuando** se importa en `citas-web`, **entonces** el framework real ejecuta y las pantallas de login/registro se renderizan.
### CA-03 — Integración directa
**Dado** el backend de autenticación disponible, **cuando** se envía registro o login válido/erróneo desde la interfaz, **entonces** esta consume el contrato REST directo y refleja el resultado sin BFF.
## Definition of Done
- [ ] CA-01 a CA-03 validados con evidencia visual y de ejecución.
- [ ] Framework seleccionado y variables de entorno documentados sin secretos.
- [ ] Build/typecheck/pruebas aplicables pasan.
- [ ] Contrato, AGENTS de frontend y trazabilidad Scrum actualizados después de importar el proyecto real.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 | Pendiente | — | — |
| CA-02 | Pendiente | — | — |
| CA-03 | Pendiente | — | — |
| DoD | Pendiente | — | — |
## Historial de validación
- S2 — HU creada en estado `Pendiente de aprobación`.
- S2 — Aprobada explícitamente por el usuario para el baseline de frontend de S2.
