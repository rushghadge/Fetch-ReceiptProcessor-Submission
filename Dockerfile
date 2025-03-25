# Use a platform-compatible JDK image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/receipt-processor.jar app.jar

# Run the application receipt-processor.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
