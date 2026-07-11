FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup --system energyflow \
    && adduser --system --ingroup energyflow energyflow

COPY --from=build /app/target/*.jar app.jar

USER energyflow

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]