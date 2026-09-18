---
id: EP-006
tipo: epica
titulo: "Experiencia web e integración"
estado: Pendiente de aprobación
historias: ["[[HU-016-importar-y-conectar-frontend]]"]
dependencias: ["[[HU-002-iniciar-y-renovar-sesion]]"]
---
# EP-006 — Experiencia web e integración
## Objetivo
Entregar la interfaz importada y conectada a la API REST.
## Valor esperado
Los actores pueden usar los flujos aprobados desde las pantallas requeridas.
## Actores
- USER, PROFESSIONAL, ADMIN.
## Alcance
- Ciclo Stitch, aprobación, AI Studio, importación y consumo REST directo.
## Fuera de alcance
- Express, BFF y cambios no aprobados de diseño.
## Reglas de negocio
- El frontend consume Spring Boot directamente y la URL se configura por entorno.
## Dependencias
- [[HU-002-iniciar-y-renovar-sesion]]
## Historias de usuario
- [[HU-016-importar-y-conectar-frontend]]
## Criterio de completitud de la épica
- [ ] La HU obligatoria está `Completada` con evidencia.
## Riesgos e incógnitas
- Falta elegir el framework real tras el handoff.
