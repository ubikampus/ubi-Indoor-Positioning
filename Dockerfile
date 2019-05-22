FROM openjdk:11.0.3-stretch

WORKDIR /app
COPY . /app
RUN ./gradlew compileJava

CMD ["./gradlew", "run"]