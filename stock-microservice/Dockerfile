FROM maven:3.9.3-amazoncorretto-20 AS build
WORKDIR /app

COPY pom.xml                        ./pom.xml
COPY stock-microservice/pom.xml     ./stock-microservice/pom.xml

RUN mvn -B -f stock-microservice/pom.xml dependency:go-offline

COPY stock-microservice/src         ./stock-microservice/src


RUN mvn -B -f stock-microservice/pom.xml clean package -DskipTests

FROM amazoncorretto:23-headless
LABEL authors="gfper"

WORKDIR /app
COPY --from=build /app/stock-microservice/target/stock-microservice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
