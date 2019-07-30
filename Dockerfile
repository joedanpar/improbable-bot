FROM maven:3.6-jdk-8-alpine as maven

WORKDIR /improbabot

COPY pom.xml .

RUN mvn -e -B dependency:resolve

COPY src ./src

RUN mvn -e -B package

FROM openjdk:8-jre-alpine

COPY --from=maven target/improbabot.jar ./

HEALTHCHECK CMD curl --request GET --url http://localhost:8090/actuator/health || exit 1

CMD ["java", "-jar", "./improbabot.jar"]
