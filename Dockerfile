# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container
COPY . /app

# Build the project (assuming Maven is used)
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# Expose the internal port defined in the fly.toml (8080)
EXPOSE 8080

# Set memory-related Java options, and run the jar file
CMD ["java", "-Xmx500m", "-XX:+UseSerialGC", "-XX:MaxRAM=500m", "-jar", "target/familytree-0.0.1-SNAPSHOT.jar"]
