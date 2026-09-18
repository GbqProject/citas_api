---
id: EP-005
tipo: epica
titulo: "Reprogramación y cierre de atención"
estado: Pendiente de aprobación
historias: ["[[HU-013-solicitar-reprogramacion]]", "[[HU-014-decidir-reprogramacion]]", "[[HU-015-consultar-agenda-y-cerrar-atencion]]"]
dependencias: ["[[HU-012-consultar-y-cancelar-citas]]"]
---
# EP-005 — Reprogramación y cierre de atención
## Objetivo
Completar el ciclo de vida de una cita aprobada.
## Valor esperado
Cambios de cita seguros y agenda operativa para el profesional.
## Actores
- USER, ADMIN, PROFESSIONAL.
## Alcance
- Reprogramación retenida, decisión administrativa, agenda y estados finales.
## Fuera de alcance
- Historia clínica y tratamientos.
## Reglas de negocio
- La cita original permanece hasta aprobar la reprogramación; solo profesional puede cerrar atención.
## Dependencias
- [[HU-012-consultar-y-cancelar-citas]]
## Historias de usuario
- [[HU-013-solicitar-reprogramacion]]
- [[HU-014-decidir-reprogramacion]]
- [[HU-015-consultar-agenda-y-cerrar-atencion]]
## Criterio de completitud de la épica
- [ ] Todas las HU obligatorias están `Completada` con evidencia.
## Riesgos e incógnitas
- Debe conservarse la auditoría sin permitir edición CRUD normal.
