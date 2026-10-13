# 🎲 La Guarida Lúdica - Gestión de Torneos y Juegos de Mesa

Aplicación web desarrollada en **Java y Spring Boot** diseñada para la gestión integral de un establecimiento o comunidad de juegos de mesa, permitiendo administrar catálogos de juegos, torneos, aforos y material gráfico en la nube.

---

## 🚀 Tecnologías Principales

* **Backend:** Java 21, Spring Boot, Spring Data JPA, Spring MVC, Validation.
* **Frontend:** Thymeleaf, HTML5, CSS.
* **Base de Datos:** PostgreSQL (compatible con entorno local y Supabase).
* **Almacenamiento en la Nube:** Cloudinary (para la gestión de imágenes y carteles de juegos y torneos).
* **Gestión de Dependencias y Construcción:** Maven.

---

## 📂 Arquitectura y Estructura del Proyecto

El proyecto sigue una arquitectura limpia orientada a servicios y controladores web tradicionales (`Spring MVC`), aplicando inyección estricta por constructor en todos los componentes:

* **`model/`**: Entidades JPA (`Juego`, `Torneo`, etc.) con validaciones de bean.
* **`repository/`**: Interfaces de Spring Data JPA para la persistencia y consultas personalizadas.
* **`service/`**: Lógica de negocio y pasarela de integración con Cloudinary.
* **`controller/`**: Controladores web (`JuegoController`, `TorneoController`) orientados a vistas Thymeleaf.
* **`templates/`**: Vistas HTML para la interfaz de usuario.

---

## ⚙️ Configuración y Despliegue

### 1. Requisitos previos
* Java 21 instalado.
* Maven.
* Instancia de PostgreSQL (Local o Supabase).
* Cuenta en Cloudinary (para las credenciales de subida de imágenes).

### 2. Configuración de propiedades (`application.properties`)
Configura tus credenciales de base de datos y de Cloudinary en el archivo `src/main/resources/application.properties`:

```properties
# Configuración de Base de Datos (PostgreSQL / Supabase)
spring.datasource.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=org.postgresql.Driver

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