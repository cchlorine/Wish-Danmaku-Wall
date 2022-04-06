FROM maven:3.8.4-jdk-8 AS builder
WORKDIR /backend
COPY ./backend /backend
RUN mvn wrapper:wrapper
RUN mvn install -Dmaven.test.skip=true

FROM java:8
COPY --from=builder /backend/target/pray-1.0.jar /app.jar
EXPOSE 8888
CMD ["java", "-jar", "/app.jar"]