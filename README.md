# Marcador Mapa API

API REST desarrollada con Spring Boot para gestión de usuarios, autenticación JWT y marcadores.

> **Estado del proyecto:** en desarrollo.  
> Algunas rutas, contratos y configuraciones pueden cambiar mientras avanza la implementación.

## Stack

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Flyway
- PostgreSQL
- JWT
- SpringDoc OpenAPI
- Lombok
- MapStruct
- Maven Wrapper
- Docker y Docker Compose

## Archivos de entorno

El proyecto usa archivos de entorno separados por perfil:

- `.env.example`: plantilla base
- `.env.local`: configuración para desarrollo
- `.env.prod`: configuración para producción

Tomando como base `.env.example`, generá los archivos de entorno que necesites para cada perfil.

```bash
cp .env.example .env.local
cp .env.example .env.prod
```

## Levantar el entorno

### Desarrollo

En desarrollo, el `docker-compose.dev.yaml` levanta solo la base de datos.  
La API se ejecuta localmente con el perfil `dev`.

```bash
docker compose --env-file .env.local -f docker-compose.dev.yaml up -d
./mvnw spring-boot:run
```

### Producción

```bash
docker compose --env-file .env.prod -f docker-compose.prod.yaml up -d --build
```

Para detener los contenedores:

```bash
docker compose --env-file .env.local -f docker-compose.dev.yaml down
docker compose --env-file .env.prod -f docker-compose.prod.yaml down
```

## Endpoints activos actualmente

### Públicos

- `POST /api/auth/register`
- `POST /api/auth/login`

### Protegidos con JWT

- `GET /api/users/`

## Documentación API

Si la aplicación está levantada, la documentación generada por SpringDoc está disponible en:

- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

## Notas

- El perfil `dev` usa `spring.profiles.active=dev`.
- El perfil `prod` usa `spring.profiles.active=prod`.
- `docker-compose.prod.yaml` define backend y base de datos; `docker-compose.dev.yaml` solo la base de datos.
