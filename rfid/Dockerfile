FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build ./target/*.jar ./app.jar
COPY .env /app/.env
RUN addgroup --system spring && adduser --system spring -G spring
RUN chown -R spring:spring /app
ENTRYPOINT java -jar app.jar
