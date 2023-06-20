# Base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/AOO-0.0.1-SNAPSHOT.jar .

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "my-spring-boot-app.jar"]
