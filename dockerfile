# Etapa de build
FROM maven:3.9.9-eclipse-temurin-23 AS build

WORKDIR /app


COPY . .

# Build com testes ignorados (mais rápido)
RUN mvn clean install -DskipTests

# Etapa de runtime
FROM eclipse-temurin:23-jdk

WORKDIR /app

# Copia o .jar gerado
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring
EXPOSE 8080

# Comando para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
