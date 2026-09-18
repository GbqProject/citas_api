# Riesgos y preguntas abiertas

## PREGUNTA ABIERTA — Framework frontend

React o Angular se decidirá después del diseño aprobado en Stitch y la importación desde AI Studio. No se debe crear configuración ni AGENTS final de frontend antes de esa evidencia.

## PREGUNTA ABIERTA — Política operativa JWT

Las expiraciones, rotación de refresh y mecanismo exacto de entrega al navegador deben decidirse al diseñar el contrato de autenticación. Los secretos no se documentan en la wiki.

## RIESGO — Concurrencia de agenda

Las reglas de no doble reserva, slots consecutivos y reprogramación requieren transacciones, restricciones de unicidad y pruebas de integración. No basta la validación de interfaz.

## RIESGO — Consistencia del esquema

`database/database_v1` es un diseño propio; al crear la aplicación debe convertirse/ajustarse a migraciones Flyway sin aplicar SQL manual directamente a entornos persistentes.
