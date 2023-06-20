# Base image
FROM adoptopenjdk:17-jdk-hotspot

# Set the working directory in the container
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/*.jar .

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "my-spring-boot-app.jar"]
