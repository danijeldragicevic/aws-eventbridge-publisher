# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the project's JAR file into the container at /app
COPY target/aws-eventbridge-publisher-1.0.0-SNAPSHOT.jar /app/aws-eventbridge-publisher.jar

# Expose the port the application runs on
EXPOSE 8081

# Run the JAR file
ENTRYPOINT ["java", "-jar", "aws-eventbridge-publisher.jar"]
