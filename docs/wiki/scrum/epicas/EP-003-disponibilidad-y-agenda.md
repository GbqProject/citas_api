---
id: EP-003
tipo: epica
titulo: "Disponibilidad y agenda"
estado: Pendiente de aprobación
historias: ["[[HU-007-publicar-disponibilidad]]", "[[HU-008-consultar-disponibilidad]]"]
dependencias: ["[[HU-006-administrar-profesionales]]"]
---
# EP-003 — Disponibilidad y agenda
## Objetivo
Publicar y consultar disponibilidad real de profesionales por sede.
## Valor esperado
Los usuarios solo ven horarios que pueden reservarse completamente.
## Actores
- PROFESSIONAL, USER.
## Alcance
- Bloques futuros, slots atómicos de 30 minutos y filtros de búsqueda.
## Fuera de alcance
- Citas y decisiones administrativas.
## Reglas de negocio
- Sin bloques pasados/solapados; 60 minutos requiere dos slots consecutivos.
## Dependencias
- [[HU-006-administrar-profesionales]]
## Historias de usuario
- [[HU-007-publicar-disponibilidad]]
- [[HU-008-consultar-disponibilidad]]
## Criterio de completitud de la épica
- [ ] Todas las HU obligatorias están `Completada` con evidencia.
## Riesgos e incógnitas
- Validaciones entre filas requieren control transaccional.
