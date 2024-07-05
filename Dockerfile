FROM openjdk:17-jdk

COPY build/libs/*SNAPSHOT.jar /app.jar

ENV SPRING_PROFILE="dev"

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "/app.jar"]

