FROM openjdk:17-slim

ADD build/libs/*.jar app.jar

ENV PROFILE local
ENV URL jdbc:mariadb://host.docker.internal:3306/spring-init

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${PROFILE}", "-Durl=${URL}", "/app.jar"]
