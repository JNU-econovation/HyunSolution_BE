FROM openjdk:17-jdk
EXPOSE 8080
COPY build/libs/dangu-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]