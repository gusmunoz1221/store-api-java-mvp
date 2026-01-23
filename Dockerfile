# Imagen base con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos Maven Wrapper y carpeta .mvn
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

# Copiamos pom.xml y código fuente
COPY pom.xml .
COPY src ./src

# Compilamos el jar dentro del contenedor, saltando los tests
RUN ./mvnw clean package -DskipTests

# Exponemos el puerto que usa Spring Boot
EXPOSE 8080

# Ejecutamos la app
CMD ["java", "-jar", "target/app.jar"]