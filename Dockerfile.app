FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
COPY settings.xml /root/.m2/settings.xml
RUN mvn clean package -DskipTests

FROM openjdk:17.0.2-jdk-slim-buster
ARG JAR_FILE=target/*.jar
COPY --from=build /app/${JAR_FILE} app.jar

RUN mkdir /logs
ENTRYPOINT ["java","-jar","/app.jar"]
