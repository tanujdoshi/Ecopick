# Use an official OpenJDK runtime as a parent image
FROM --platform=linux/amd64 amazoncorretto:17-al2023-jdk

# Set the working directory inside the container
WORKDIR /app


COPY target/backend-0.0.1-SNAPSHOT.jar /app/app.jar 

# COPY env.properties /app/env.properties

# Expose the port on which your Spring Boot application will run (adjust as needed)
EXPOSE 8080

# Define the command to run your Spring Boot application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
