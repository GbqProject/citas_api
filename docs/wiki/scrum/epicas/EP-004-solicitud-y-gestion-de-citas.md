---
id: EP-004
tipo: epica
titulo: "Solicitud y gestión de citas"
estado: Pendiente de aprobación
historias: ["[[HU-009-solicitar-cita-general]]", "[[HU-010-solicitar-cita-especializada]]", "[[HU-011-decidir-cita-especializada]]", "[[HU-012-consultar-y-cancelar-citas]]"]
dependencias: ["[[HU-008-consultar-disponibilidad]]"]
---
# EP-004 — Solicitud y gestión de citas
## Objetivo
Permitir reservas generales y especializadas sin doble ocupación.
## Valor esperado
El paciente gestiona sus citas y ADMIN decide solicitudes especializadas.
## Actores
- USER, ADMIN.
## Alcance
- Crear, aprobar/rechazar, consultar y cancelar citas; auditoría de cambios.
## Fuera de alcance
- Reprogramación y cierre profesional.
## Reglas de negocio
- General se aprueba automáticamente; especializada inicia `REQUESTED`; rechazo exige motivo.
## Dependencias
- [[HU-008-consultar-disponibilidad]]
## Historias de usuario
- [[HU-009-solicitar-cita-general]]
- [[HU-010-solicitar-cita-especializada]]
- [[HU-011-decidir-cita-especializada]]
- [[HU-012-consultar-y-cancelar-citas]]
## Criterio de completitud de la épica
- [ ] Todas las HU obligatorias están `Completada` con evidencia.
## Riesgos e incógnitas
- La reserva de slots y el cambio de estado deben ser atómicos.
