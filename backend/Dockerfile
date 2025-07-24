FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

COPY . .

RUN mvn clean install -pl backend -am -DskipTests


FROM eclipse-temurin:17-jre

WORKDIR /app

RUN groupadd -r spring && useradd -r -g spring spring

COPY --from=builder /app/backend/target/*.jar app.jar

RUN chown spring:spring app.jar

USER spring:spring

EXPOSE 8080

ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
