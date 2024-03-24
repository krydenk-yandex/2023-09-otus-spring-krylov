FROM maven:3.9.6-amazoncorretto-17-al2023
COPY . .
RUN mvn package
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/target/users-1.0.0.jar"]
