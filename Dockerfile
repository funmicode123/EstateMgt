# Stage 1: Build with Java 22 + Maven installed
FROM eclipse-temurin:22 AS build

# Install Maven manually
RUN apt-get update && \
    apt-get install -y maven && \
    mvn -v

WORKDIR /app

# Copy project files
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY . .

# Build the app
RUN mvn clean package -DskipTests

# Stage 2: Runtime with JDK 22
FROM eclipse-temurin:22
WORKDIR /app

COPY --from=build /app/target/*.jar estate.jar

ENV PORT=8080
EXPOSE $PORT

ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=$PORT estate.jar"]
