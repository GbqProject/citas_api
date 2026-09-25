# Despliegue local con Docker

## Propósito

Documentar el procedimiento verificado para levantar MySQL, `citas-api` y `citas-web` en el laboratorio local.

## Estado de conocimiento

HECHO — verificado el 2026-09-25 con Docker Desktop, MySQL 8.4, Java 21, Spring Boot 3.5.0, Maven y Node 24.

## Evidencia

- `README.md` y `docker-compose.yml` del workspace.
- `citas-api/src/main/resources/application.yml`.
- Migraciones Flyway `V1__identity.sql` y `V2__scheduling_core.sql`.
- Arranque verificado de `co.com.fcv.training.citas.CitasApplication`.
- Frontend Vite verificado en `http://localhost:5173/`.

## Procedimiento

Desde la raíz del workspace:

1. Crear `.env` local a partir de `.env.example`. No versionar `.env`.
2. Levantar la infraestructura y los contenedores de desarrollo:

   ```powershell
   docker compose up -d mysql citas-api-dev citas-web-dev
   docker compose ps
   ```

3. En el contenedor backend, limpiar clases compiladas cuando se hayan cambiado migraciones y arrancar Spring Boot con la clase principal explícita:

   ```powershell
   docker compose exec -T citas-api-dev mvn -q clean
   docker compose exec -T citas-api-dev mvn -q `
     '-DskipTests' `
     '-Dspring-boot.run.main-class=co.com.fcv.training.citas.CitasApplication' `
     spring-boot:run
   ```

   Flyway debe validar y aplicar `V1` y `V2`. La clase principal explícita es necesaria porque el proyecto contiene más de una clase `main`.

4. En otra terminal, instalar dependencias y arrancar Vite:

   ```powershell
   docker compose exec -T citas-web-dev npm ci
   docker compose exec -T citas-web-dev npm run dev -- --host 0.0.0.0
   ```

5. Verificar:

   - API: `http://localhost:8080/`
   - Frontend: `http://localhost:5173/`
   - Un endpoint protegido sin token debe responder `401`.
   - La pantalla de login debe cargar después de que el frontend termine de comprobar la sesión.

## Reinicio limpio opcional

DECISIÓN — Solo si se desea borrar los datos sintéticos persistidos del laboratorio:

```powershell
docker compose down -v
docker compose up -d mysql citas-api-dev citas-web-dev
```

Esto elimina el volumen MySQL del proyecto. Después se debe repetir el arranque de la API para que Flyway cree el esquema y ejecute los seeds.

## Precauciones conocidas

- No guardar contraseñas, JWT ni valores reales de `.env` en esta Wiki.
- Si se elimina o cambia una migración, ejecutar `mvn clean` antes de reiniciar para evitar clases SQL obsoletas en `target/classes`.
- Flyway muestra una advertencia de compatibilidad porque MySQL 8.4 es más reciente que la versión máxima probada por esa dependencia; el arranque verificado continúa correctamente.
- `citas-api/src/main/resources/db/migration` debe contener una sola migración por versión.

## Stack aislado `fq`

HECHO — Para evitar interferencia con otra ejecución local, el workspace también soporta un stack aislado usando el override ignorado `.local/docker-compose.fq.yml`:

```powershell
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml up -d mysql citas-api-dev citas-web-dev
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-api-dev mvn -q clean
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-api-dev mvn -q `
  '-DskipTests' `
  '-Dspring-boot.run.main-class=co.com.fcv.training.citas.CitasApplication' `
  spring-boot:run
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-web-dev npm ci
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-web-dev npm run dev -- --host 0.0.0.0
```

El perfil publica la API en `http://localhost:8081/`, el frontend en `http://localhost:5174/` y persiste MySQL en `fq_mysql_data`. Sus credenciales locales se documentan en `.local/fq-users.md`, que no se versiona.

### Arranque verificado del stack `fq`

El override `.local/docker-compose.fq.yml` monta el código de ambos repositorios y deja los contenedores de desarrollo en espera (`tail -f /dev/null`). Después de levantar el stack, iniciar los procesos explícitamente:

```powershell
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml up -d mysql citas-api-dev citas-web-dev
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-api-dev mvn -q clean
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-api-dev sh -lc "nohup mvn -q -DskipTests '-Dspring-boot.run.main-class=co.com.fcv.training.citas.CitasApplication' spring-boot:run >/tmp/citas-api.log 2>&1 </dev/null &"
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-web-dev npm ci
docker compose -p fq -f docker-compose.yml -f .local/docker-compose.fq.yml exec -T citas-web-dev sh -lc "nohup npm run dev -- --host 0.0.0.0 >/tmp/vite.log 2>&1 </dev/null &"
```

Verificar con `docker compose ... ps` y abrir `http://localhost:5174/`. La API se expone en `http://localhost:8081/`; una respuesta `401` en una ruta protegida confirma que Spring Boot está atendiendo.

El override local define `SPRING_DATASOURCE_URL` con `useSSL=false&allowPublicKeyRetrieval=true`. Esta decisión aplica solo al laboratorio `fq`: el certificado TLS local de MySQL tenía una fecha `NotBefore` posterior a la hora del host, lo que impedía arrancar la API. No cambiar la configuración base ni usar este ajuste como política de producción.

Si el navegador muestra `ERR_EMPTY_RESPONSE`, comprobar primero que los procesos `java` y `vite` estén activos dentro de los contenedores; `docker compose up` por sí solo deja los servicios de desarrollo esperando.
