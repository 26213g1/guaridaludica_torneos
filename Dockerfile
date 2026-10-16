# Etapa de compilación
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY . .

# Dar permisos de ejecución al Maven Wrapper y compilar
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

# Arranca forzando el perfil de producción
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]