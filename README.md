# 🎲 La Guarida Lúdica - Gestión de Torneos y Juegos de Mesa

> **Nota sobre el proyecto:** Esta aplicación es un módulo independiente dedicado exclusivamente a la gestión de torneos, concebido como parte del ecosistema de la tienda principal que ya se encuentra en funcionamiento en [https://guaridaludica.onrender.com/](https://guaridaludica.onrender.com/). En un futuro, este sistema de torneos se integrará directamente como una sección nativa dentro de la propia tienda.

Aplicación web desarrollada en **Java y Spring Boot** diseñada para la gestión integral de un establecimiento o comunidad de juegos de mesa. Permite administrar catálogos de juegos, torneos, aforos, material gráfico en la nube y un sistema completo de autenticación y control de accesos basado en roles.

---

## 🚀 Tecnologías Principales

- **Backend:** Java 21, Spring Boot, Spring Data JPA, Hibernate ORM, Spring Security, Spring MVC, Validation.
- **Frontend:** Thymeleaf, Thymeleaf Extras Spring Security 6, Bootstrap, HTML5, CSS.
- **Base de Datos:** PostgreSQL (compatible con entorno local y Supabase).
- **Almacenamiento en la Nube:** Cloudinary (gestión de imágenes y carteles de juegos y torneos).
- **Seguridad:** BCrypt Password Encoder para el cifrado de credenciales.
- **Gestión de Dependencias y Construcción:** Maven.

---

## 📂 Arquitectura y Estructura del Proyecto

El proyecto sigue una arquitectura limpia orientada a servicios y controladores web tradicionales (`Spring MVC`), utilizando inyección estricta por constructor en todos los componentes:

- **`model/`**: Entidades JPA (`Juego`, `Torneo`, `Jugador`) gestionadas mediante Hibernate con anotaciones de mapeo objeto-relacional y validaciones de bean.
- **`repository/`**: Interfaces de Spring Data JPA para la persistencia y consultas personalizadas (`JuegoRepository`, `TorneoRepository`, `JugadorRepository`).
- **`service/`**: Lógica de negocio, gestión de usuarios (`JugadorDetailsService`) y pasarela de integración con Cloudinary.
- **`security/` / `config/`**: Configuración de seguridad (`SecurityConfig`) con filtros de autorización, encriptación y carga inicial de datos de prueba (`DataInitializer`).
- **`controller/`**: Controladores web (`JuegoController`, `TorneoController`, `AuthController`) orientados a vistas Thymeleaf.
- **`templates/`**: Vistas HTML para la interfaz de usuario, incluyendo plantillas adaptadas para login, registro y diseño modular mediante layouts.

---

## 🔒 Sistema de Seguridad y Autenticación

La aplicación implementa **Spring Security** para garantizar un control robusto de los accesos:
- **Roles:** Dos perfiles diferenciados (`ROLE_ADMIN` y `ROLE_USER`).
- **Protección de Rutas:** Las acciones de gestión crítica (crear, editar o eliminar juegos y torneos) están restringidas exclusivamente a administradores mediante `SecurityFilterChain`.
- **Credenciales por Defecto:** Al arrancar la aplicación por primera vez, el componente `DataInitializer` crea automáticamente una cuenta de administrador si la base de datos de jugadores está vacía:
    - **Email:** `admin@guarida.com`
    - **Contraseña:** `admin123`

---

## ⚙️ Configuración y Despliegue

1. ### Requisitos previos
- Java 21 instalado.
- Maven.
- Instancia de PostgreSQL (Local o Supabase).
- Cuenta en Cloudinary (para las credenciales de subida de imágenes).

2. **Configuración de propiedades (`application-dev.properties`)**  
   Configura tus credenciales de base de datos, el comportamiento de Hibernate y Cloudinary en el archivo `src/main/resources/application-dev.properties`:

```properties
# Configuración de Base de Datos (PostgreSQL / Supabase)
spring.datasource.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

# Configuración de Hibernate / JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configuración de Cloudinary
cloudinary.cloud-name=tu_cloud_name
cloudinary.api-key=tu_api_key
cloudinary.api-secret=tu_api_secret
cloudinary.folder=tu_folder
```
### 3. Ejecución de la aplicación
Puedes compilar y ejecutar el proyecto utilizando Maven desde tu terminal:

```
mvn clean spring-boot:run
```

Una vez arrancado, accede a la aplicación desde tu navegador en:
http://localhost:8080