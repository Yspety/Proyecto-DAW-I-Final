# Despliegue en Azure con PostgreSQL

## Cambios realizados

- Backend migrado de MySQL a PostgreSQL.
- Agregado driver `org.postgresql:postgresql`.
- `application.properties` ahora lee variables de entorno para conexión PostgreSQL y CORS.
- Agregados `schema-postgresql.sql` y `data-postgresql.sql` para inicializar la base.
- CORS configurable con `CORS_ALLOWED_ORIGINS`.
- Docker y `docker-compose.yml` local actualizados a PostgreSQL.
- Frontend Angular ahora usa `environment.apiUrl` para llamar al backend desplegado.
- Agregado `staticwebapp.config.json` para rutas de Angular en Azure Static Web Apps.
- Agregados workflows de GitHub Actions para backend y frontend.

## Recursos recomendados de bajo costo

1. Frontend: Azure Static Web Apps Free.
2. Backend: Azure App Service Free F1 o crédito de Azure for Students.
3. Base de datos: Azure Database for PostgreSQL Flexible Server si tu cuenta tiene prueba gratuita/crédito disponible. Si no aparece gratis, usa el crédito de Azure for Students o una alternativa externa gratuita para PostgreSQL.

## Variables del backend en Azure App Service

Configura en **App Service > Configuration > Application settings**:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://<postgres-server>.postgres.database.azure.com:5432/erp_productos?sslmode=require
SPRING_DATASOURCE_USERNAME=<usuario-postgres>
SPRING_DATASOURCE_PASSWORD=<password-postgres>
CORS_ALLOWED_ORIGINS=https://<tu-frontend>.azurestaticapps.net
SPRING_SQL_INIT_MODE=always
```

Después del primer arranque exitoso puedes cambiar `SPRING_SQL_INIT_MODE=never` para no ejecutar scripts en cada reinicio. Los inserts ya son idempotentes.

## Secrets de GitHub necesarios

```text
AZURE_BACKEND_APP_NAME=<nombre-del-app-service>
AZURE_BACKEND_PUBLISH_PROFILE=<publish-profile-descargado-del-app-service>
AZURE_BACKEND_URL=https://<tu-backend-app-service>.azurewebsites.net
AZURE_STATIC_WEB_APPS_API_TOKEN=<deployment-token-de-static-web-app>
```

## Prueba local

Backend con PostgreSQL local:

```bash
cd inventory-management-api-springboot-main(2)/inventory-management-api-springboot-main
docker compose up --build
```

Frontend local:

```bash
cd inventory-management-angular-frontend-main(1)/inventory-management-angular-frontend-main
npm install
npm start
```

Usuarios iniciales:

```text
admin / 1234
almacenero / abcd
```
