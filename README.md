# 🎲 La Guarida Lúdica - Gestión de Torneos y Juegos de Mesa

> **Estado del Proyecto:** Módulo independiente concebido para la gestión integral de torneos y competiciones, integrado dentro del ecosistema de "La Guarida Lúdica". Actualmente se encuentra **desplegado y operativo en Render** en [https://guaridaludica-torneos.onrender.com/](https://guaridaludica-torneos.onrender.com/) (conectado con la tienda principal en [https://guaridaludica.onrender.com/](https://guaridaludica.onrender.com/)).

Aplicación web desarrollada en **Java y Spring Boot** diseñada para administrar catálogos de juegos, inscripciones, torneos, aforos, gestión de recursos gráficos en la nube y un sistema robusto de autenticación y control de accesos basado en roles.

---

## 🚀 Tecnologías Principales

- **Backend:** Java 21, Spring Boot, Spring Data JPA, Hibernate ORM, Spring Security, Spring MVC, Validation.
- **Frontend:** Thymeleaf, Thymeleaf Extras Spring Security 6, Bootstrap, HTML5, CSS.
- **Base de Datos:** PostgreSQL (Soporte para entorno local y Supabase con pool de conexiones Hikari optimizado).
- **Almacenamiento en la Nube:** Cloudinary (gestión y subida de imágenes y carteles para los torneos).
- **Seguridad:** BCrypt Password Encoder para el cifrado de credenciales.
- **Contenedorización y Despliegue:** Docker, Docker Compose, Docker Hub (`jmmoraj`), Render.
- **Gestión de Dependencias:** Maven.

---

## 📂 Arquitectura y Estructura del Proyecto

El proyecto sigue una arquitectura limpia orientada a servicios y controladores web tradicionales (`Spring MVC`), aplicando inyección por constructor en todos sus componentes:

- **`model/`**: Entidades JPA (`Torneo`, `Juego`, `Jugador`) gestionadas mediante Hibernate.
- **`repository/`**: Interfaces de Spring Data JPA para la persistencia y consultas personalizadas.
- **`service/`**: Lógica de negocio, gestión de usuarios (`JugadorDetailsService`) y pasarela de integración con Cloudinary.
- **`security/` / `config/`**: Configuración de seguridad (`SecurityConfig`), filtros de autorización y carga inicial de datos mediante `DataInitializer`.
- **`controller/`**: Controladores web orientados a vistas Thymeleaf.
- **`templates/`**: Vistas HTML para la interfaz de usuario con diseño modular de layouts.

---

## 🔒 Sistema de Seguridad y Autenticación

La aplicación implementa **Spring Security** para garantizar un control estricto de los accesos:
- **Roles:** Dos perfiles diferenciados (`ROLE_ADMIN` y `ROLE_USER`).
- **Protección de Rutas:** Las acciones de administración y gestión de torneos están restringidas exclusivamente a administradores.
- **Credenciales por Defecto:** Al arrancar por primera vez con la base de datos vacía, el sistema genera automáticamente una cuenta de administrador:
    - **Email:** `admin@guarida.com`
    - **Contraseña:** `admin123`

---

## 🐳 Despliegue con Docker y Docker Compose

El proyecto está preparado para ejecutarse de forma aislada mediante contenedores Docker, conviviendo de forma simultánea con el módulo de la tienda en los puertos definidos:

### 1. Variables de Entorno (Perfil Producción)
Crea o configura tus variables de entorno para que apunten a Supabase y Cloudinary (puedes basarte en un archivo `application-prod.properties` o inyectarlas directamente desde el entorno):
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`, `CLOUDINARY_FOLDER`

### 2. Ejecución local mediante Docker Compose
El servicio está configurado para mapearse en el puerto **`8084`** (evitando conflictos con la tienda que corre en el `8083`):
```yaml
services:
  app:
    image: jmmoraj/guaridaludica_torneos:latest
    container_name: guaridaludica_torneos
    restart: always
    ports:
      - "8084:8084"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      # ... (variables de entorno de Supabase y Cloudinary)
```

Para levantar el contenedor en segundo plano:
```cmd
docker compose up --no-build -d
```

---

## ⚙️ Configuración y Ejecución Local (Desarrollo)

### 1. Requisitos previos
- Java 21 instalado.
- Maven.
- Base de datos PostgreSQL local.

### 2. Configuración de propiedades (`application-dev.properties`)
Configura tus credenciales locales en `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

cloudinary.cloud-name=tu_cloud_name
cloudinary.api-key=tu_api_key
cloudinary.api-secret=tu_api_secret
cloudinary.folder=tu_folder
```

### 3. Compilación y Ejecución con Maven
```cmd
mvn clean spring-boot:run
```
Una vez arrancado, accede a la aplicación localmente en:
`http://localhost:8084`