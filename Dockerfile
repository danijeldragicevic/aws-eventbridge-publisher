# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project's JAR file into the container at /app
COPY target/*.jar /app/

# Expose the port the application runs on
EXPOSE 8081

# Find the jar file dynamically and run it
ENTRYPOINT ["sh", "-c", "exec java -jar $(ls /app/*.jar | head -n 1)"]