FROM maven:3.9.3-amazoncorretto-20 AS build
WORKDIR /app

COPY pom.xml ./
COPY eureka-server/pom.xml ./eureka-server/pom.xml

RUN mvn -B -f eureka-server/pom.xml dependency:go-offline

COPY eureka-server/src ./eureka-server/src

RUN mvn -B -f eureka-server/pom.xml clean package -DskipTests

FROM amazoncorretto:23-headless
LABEL authors="gfper"
WORKDIR /app
COPY --from=build /app/eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java","-jar","/app/app.jar"]
