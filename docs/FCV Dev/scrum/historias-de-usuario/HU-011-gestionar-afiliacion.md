---
id: HU-011
tipo: historia-de-usuario
titulo: "Gestionar afiliación"
estado: Aprobada
epica: "[[EP-002-identidad-y-perfil-del-usuario]]"
esfuerzo: Medio
sprint_sugerido: "Incremento 2"
dependencias: ["[[HU-003-publicar-catalogos-fijos]]"]
relacionadas: []
---
# HU-011 — Gestionar afiliación
## Historia de usuario
**COMO** visitante que se registra  
**QUIERO** seleccionar opcionalmente un plan activo  
**PARA** guardar una afiliación normalizada como dato administrativo de mi cuenta.
## Contexto y descripción
EPS y planes son configurables; régimen es catálogo fijo.
## Alcance
- Crear una afiliación inicial opcional desde el registro usando un plan activo.
## Fuera de alcance
- CRUD ADMIN de EPS/planes, gestión posterior de la afiliación y uso de la afiliación en reglas de agenda.
## Reglas de negocio
- La afiliación usa FKs; no afecta disponibilidad, precio, aprobación ni reserva.
## Dependencias y relaciones
- Épica: [[EP-002-identidad-y-perfil-del-usuario]]
- Dependencias: [[HU-003-publicar-catalogos-fijos]].
- Relacionadas: Ninguna.
## Esfuerzo
**Nivel:** Medio. **Justificación de dificultad:** enlaza catálogos configurables/fijos, integridad y ownership.
## Tareas de desarrollo
- [x] **T-01 — Publicar planes activos.** Dificultad: Medio. Endpoint público de solo lectura para el formulario previo a autenticación.
- [x] **T-02 — Extender registro opcional.** Dificultad: Alto. Validar plan activo y crear la afiliación sin duplicar textos de catálogo.
- [x] **T-03 — Probar selección u omisión.** Dificultad: Medio. La omisión no impide el registro ni afecta agenda.
## Criterios de aceptación
### CA-01 — Asociación válida
**Dado** catálogos activos y una combinación válida, **cuando** USER guarda afiliación, **entonces** queda asociada a su perfil.
### CA-02 — Sin duplicidad
**Dado** una afiliación existente, **cuando** USER repite EPS, régimen o plan dentro de su afiliación, **entonces** la aplicación evita la duplicación definida.
### CA-03 — Aislamiento por usuario
**Dado** un USER autenticado, **cuando** consulta o modifica afiliación, **entonces** solo opera sobre su propia afiliación.
## Definition of Done
- [ ] CA-01 a CA-03 probados en dominio/REST y cliente aplicable.
- [ ] Persistencia 3FN y migración aplicable verificadas; no hay textos de catálogo duplicados.
- [ ] Trazabilidad Scrum actualizada.
## Evidencia de validación
| Elemento | Resultado | Evidencia | Observación |
|---|---|---|---|
| CA-01 / objetivo | Cumple | `AuthIntegrationTest.registrationCreatesOptionalCurrentInsuranceAffiliation`; `AuthService`; `InsuranceJpaAdapter` | Plan activo asociado por FK en `user_insurance_affiliations`. |
| Registro sin plan / objetivo | Cumple | `AuthIntegrationTest.activePlansArePublicAndRegistrationWithoutPlanHasNoAffiliation` | Registro 201 sin fila de afiliación. |
| Plan inválido/inactivo / objetivo | Cumple | `AuthIntegrationTest.registrationRejectsMissingOrInactivePlanWithoutPartialAccount` | 400 Problem Details y sin cuenta parcial. |
| Cliente web / objetivo | Implementado | `RegisterScreen`, `authScreens.test.tsx`, `schedulingApi.ts` | Carga endpoint público y conserva opción sin afiliación. |
| Persistencia 3FN / objetivo | Cumple | `V2__scheduling_core.sql`; consulta de columnas en prueba | Usuario referencia afiliación; no almacena nombres EPS/plan. |
## Historial de validación
- 2026-09-17 — HU creada en estado `Pendiente de aprobación`.
- 2026-09-23 — Contrato público separado aprobado e implementación cross-repo realizada; CRUD ADMIN y perfil posterior permanecen fuera de alcance.
## Notas y decisiones
- La regla de vigencia de una EPS/plan se abordará con sus HU administrativas.
