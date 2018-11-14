FROM maven:3.5-jdk-8 as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn package

FRO openjdk:8u171-jre-alpine
ARG botToken
WORKDIR /improbabot

COPY --from=maven target/improbabot.jar ./
RUN mkdir data
ENTRYPOINT ["java", "-jar", "./improbabot.jar"]
