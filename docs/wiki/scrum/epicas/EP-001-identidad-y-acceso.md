---
id: EP-001
tipo: epica
titulo: "Identidad y acceso"
estado: Pendiente de aprobación
historias: ["[[HU-001-registrar-usuario]]", "[[HU-002-iniciar-y-renovar-sesion]]", "[[HU-003-recuperar-contrasena]]"]
dependencias: []
---
# EP-001 — Identidad y acceso
## Objetivo
Permitir que actores ficticios creen y usen cuentas de forma segura.
## Valor esperado
Acceso autenticado y autorizable para los flujos del producto.
## Actores
- USER, PROFESSIONAL, ADMIN.
## Alcance
- Registro, sesión JWT con refresh/revocación y recuperación de contraseña.
## Fuera de alcance
- Proveedores externos de identidad y envío SMTP obligatorio.
## Reglas de negocio
- Email y documento son únicos; passwords y tokens nunca se almacenan en claro.
## Dependencias
- Ninguna.
## Historias de usuario
- [[HU-001-registrar-usuario]]
- [[HU-002-iniciar-y-renovar-sesion]]
- [[HU-003-recuperar-contrasena]]
## Criterio de completitud de la épica
- [ ] Todas las HU obligatorias están `Completada` con evidencia.
## Riesgos e incógnitas
- Definir expiración y rotación de tokens por configuración segura.
