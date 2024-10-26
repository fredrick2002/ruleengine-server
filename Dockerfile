# Use an official Maven image with OpenJDK 21 for the build stage
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code into the container
COPY src ./src

# Build the Spring Boot application
RUN mvn clean package -DskipTests

# Use the official OpenJDK 21 runtime for the run stage
FROM eclipse-temurin:21-jre

# Set the working directory in the container
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port that the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
