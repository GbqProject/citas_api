# Contexto del proyecto

## HECHO — Propósito

Sistema ficticio de agendamiento de citas de laboratorio FCV. No representa procesos ni datos internos reales; pacientes, profesionales, EPS, planes, horarios y citas son sintéticos.

## HECHO — Actores

- `USER`: se registra, mantiene perfil/afiliación y gestiona citas.
- `PROFESSIONAL`: usuario especializado que administra disponibilidad y consulta su agenda.
- `ADMIN`: administra profesionales/catálogos y decide solicitudes especializadas y reprogramaciones.

## HECHO — Capacidades principales

Autenticación JWT; perfil y afiliación; catálogos; profesionales; disponibilidad en slots de 30 minutos; citas generales y especializadas; cancelación; reprogramación; cierre de atención; auditoría de estados y UI REST.

## HECHO — Alcance excluido

Historia clínica, facturación, pagos, datos reales, integraciones clínicas y SMTP obligatorio.

Fuente: `PRD.md`.
