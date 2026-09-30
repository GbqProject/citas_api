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
2. Levantar la infraestructura, backend y frontend:

   ```powershell
   docker compose up -d
   docker compose ps
   ```

3. Compose inicia Spring Boot y Vite automáticamente. La primera ejecución puede tardar mientras Maven y npm descargan dependencias. Si se modifican migraciones, se puede ejecutar `docker compose exec -T citas-api-dev mvn -q clean` y reiniciar el servicio.

4. Verificar:

   - API: `http://localhost:8080/`
   - Frontend: `http://localhost:5173/`
   - Un endpoint protegido sin token debe responder `401`.
   - La pantalla de login debe cargar después de que el frontend termine de comprobar la sesión.

## Reinicio limpio opcional

DECISIÓN — Solo si se desea borrar los datos sintéticos persistidos del laboratorio:

```powershell
docker compose down -v
docker compose up -d
```

Esto elimina el volumen MySQL del proyecto. Compose vuelve a iniciar automáticamente la API y el frontend para que Flyway cree el esquema y ejecute los seeds.

## Precauciones conocidas

- No guardar contraseñas, JWT ni valores reales de `.env` en esta Wiki.
- Si se elimina o cambia una migración, ejecutar `mvn clean` antes de reiniciar para evitar clases SQL obsoletas en `target/classes`.
- Flyway muestra una advertencia de compatibilidad porque MySQL 8.4 es más reciente que la versión máxima probada por esa dependencia; el arranque verificado continúa correctamente.
- `citas-api/src/main/resources/db/migration` debe contener una sola migración por versión.

## Stack aislado `fq`

HECHO — Para evitar interferencia con otra ejecución local, el workspace también soporta un stack aislado usando el override versionado `fq-config/docker-compose.yml`:

```powershell
docker compose -p fq -f docker-compose.yml -f fq-config/docker-compose.yml up -d
```

El perfil publica la API en `http://localhost:8081/`, el frontend en `http://localhost:5174/` y persiste MySQL en `fq_mysql_data`. Sus credenciales locales se documentan en `.local/fq-users.md`, que no se versiona.

### Arranque verificado del stack `fq`

El override `fq-config/docker-compose.yml` monta el código de ambos repositorios y hereda los comandos automáticos de Spring Boot y Vite del Compose base:

```powershell
docker compose -p fq -f docker-compose.yml -f fq-config/docker-compose.yml up -d
```

Verificar con `docker compose ... ps` y abrir `http://localhost:5174/`. La API se expone en `http://localhost:8081/`; una respuesta `401` en una ruta protegida confirma que Spring Boot está atendiendo.

El override local define `SPRING_DATASOURCE_URL` con `useSSL=false&allowPublicKeyRetrieval=true`. Esta decisión aplica solo al laboratorio `fq`: el certificado TLS local de MySQL tenía una fecha `NotBefore` posterior a la hora del host, lo que impedía arrancar la API. No cambiar la configuración base ni usar este ajuste como política de producción.

### Seed sintético del mes siguiente

La migración Flyway `V4__future_month_availability_seed.sql` crea, al aplicarse, datos sintéticos para el mes calendario siguiente: días hábiles, dos bloques diarios en HIC/ICV y slots de 30 minutos para el profesional demo `FQ-MED-*`. También habilita `CARDIOLOGIA_DEMO` de 60 minutos para probar slots consecutivos. Es idempotente respecto de bloques y slots existentes.

Para aplicarla en el stack `fq`, reinicia la API después de levantar los contenedores; Flyway la ejecuta automáticamente:

```powershell
docker compose -p fq -f docker-compose.yml -f fq-config/docker-compose.yml exec -T citas-api-dev mvn -q clean
docker compose -p fq -f docker-compose.yml -f fq-config/docker-compose.yml exec -T citas-api-dev sh -lc "nohup mvn -q -DskipTests '-Dspring-boot.run.main-class=co.com.fcv.training.citas.CitasApplication' spring-boot:run >/tmp/citas-api.log 2>&1 </dev/null &"
```

La migración no crea credenciales ni contiene secretos.

Si el navegador muestra `ERR_EMPTY_RESPONSE`, comprobar `docker compose ... ps` y los logs con `docker compose ... logs citas-api-dev citas-web-dev`.
