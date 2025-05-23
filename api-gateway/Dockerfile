FROM maven:3.9.3-amazoncorretto-20 AS build
WORKDIR /app

COPY pom.xml ./
COPY api-gateway/pom.xml ./api-gateway/pom.xml

RUN mvn -B -f api-gateway/pom.xml dependency:go-offline

COPY api-gateway/src ./api-gateway/src

RUN mvn -B -f api-gateway/pom.xml clean package -DskipTests

FROM amazoncorretto:23-headless
LABEL authors="gfper"
WORKDIR /app
COPY --from=build /app/api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
