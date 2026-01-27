FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY . .
# Make Maven wrapper executable
RUN chmod +x mvnw
# Build the project
RUN ./mvnw clean package -DskipTests
CMD ["java", "-jar", "target/Messenger-0.0.1-SNAPSHOT.jar"]
