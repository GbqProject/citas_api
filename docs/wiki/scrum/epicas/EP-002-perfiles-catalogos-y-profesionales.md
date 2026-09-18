---
id: EP-002
tipo: epica
titulo: "Perfiles, catálogos y profesionales"
estado: Pendiente de aprobación
historias: ["[[HU-004-gestionar-perfil-y-afiliacion]]", "[[HU-005-administrar-catalogos-configurables]]", "[[HU-006-administrar-profesionales]]"]
dependencias: ["[[HU-001-registrar-usuario]]"]
---
# EP-002 — Perfiles, catálogos y profesionales
## Objetivo
Mantener los datos y asignaciones necesarios para ofrecer atención.
## Valor esperado
Perfiles consistentes, catálogos administrables y profesionales habilitados.
## Actores
- USER, ADMIN.
## Alcance
- Perfil/afiliación, EPS/planes/especialidades y profesionales con sus asignaciones.
## Fuera de alcance
- Datos clínicos, facturación y eliminación física de catálogos referenciados.
## Reglas de negocio
- La afiliación referencia un plan; sedes, roles y regímenes son catálogos fijos.
## Dependencias
- [[HU-001-registrar-usuario]]
## Historias de usuario
- [[HU-004-gestionar-perfil-y-afiliacion]]
- [[HU-005-administrar-catalogos-configurables]]
- [[HU-006-administrar-profesionales]]
## Criterio de completitud de la épica
- [ ] Todas las HU obligatorias están `Completada` con evidencia.
## Riesgos e incógnitas
- Se debe asegurar que solo ADMIN cree profesionales y modifique catálogos.
