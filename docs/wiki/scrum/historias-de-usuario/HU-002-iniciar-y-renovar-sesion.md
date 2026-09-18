---
id: HU-002
tipo: historia-de-usuario
titulo: "Iniciar y renovar sesión"
estado: En validación
epica: "[[EP-001-identidad-y-acceso]]"
esfuerzo: Alto
sprint_sugerido: "Incremento 1 — Fundación y autenticación"
dependencias: ["[[HU-001-registrar-usuario]]"]
relacionadas: ["[[HU-016-importar-y-conectar-frontend]]"]
---
# HU-002 — Iniciar y renovar sesión
## Historia de usuario
**COMO** usuario registrado, **QUIERO** iniciar sesión, renovar mi sesión y cerrarla, **PARA** acceder de forma segura a las capacidades autorizadas.
## Alcance
- Login email/contraseña, access JWT, refresh token, refresh y revocación/logout.
## Fuera de alcance
- Recuperación de contraseña, OAuth y UI final.
## Reglas de negocio
- Access y refresh son distintos; refresh se persiste como hash, expira y puede revocarse; roles forman parte del contexto de autorización.
## Dependencias y relaciones
- Épica: [[EP-001-identidad-y-acceso]]
- Depende de: [[HU-001-registrar-usuario]]
- Relacionada: [[HU-016-importar-y-conectar-frontend]]
## Esfuerzo
**Nivel:** Alto. **Justificación:** seguridad, emisión/validación de tokens, persistencia y pruebas negativas coordinadas.
## Tareas de desarrollo
- [ ] **T-01 — Configurar autenticación JWT.** Dificultad: Alto. Separar tokens, cargar secretos desde entorno y construir contexto de roles.
- [ ] **T-02 — Implementar renovación y logout.** Dificultad: Alto. Persistir hashes, expiración y revocación de refresh.
- [ ] **T-03 — Probar seguridad del flujo.** Dificultad: Alto. Probar login, refresh válido, expirado/revocado y credenciales inválidas.
## Criterios de aceptación
### CA-01 — Login válido
**Dado** un usuario activo y credenciales correctas, **cuando** inicia sesión, **entonces** recibe access y refresh según el contrato sin exponer hashes.
### CA-02 — Renovación válida
**Dado** un refresh vigente y no revocado, **cuando** solicita renovación, **entonces** obtiene una sesión renovada según la política definida.
### CA-03 — Rechazo y logout
**Dado** credenciales erróneas o un refresh inválido/expirado/revocado, **cuando** se autentica o renueva, **entonces** el acceso se rechaza; al cerrar sesión, el refresh deja de ser utilizable.
## Definition of Done
- [ ] CA-01 a CA-03 validados con evidencia.
- [ ] No hay secretos embebidos ni tokens en claro persistidos.
- [ ] Pruebas de casos positivos y negativos pasan.
- [ ] La trazabilidad Scrum está actualizada.
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
