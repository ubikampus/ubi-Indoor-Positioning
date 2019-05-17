FROM openjdk

WORKDIR /app
COPY . /app
RUN cd /app
RUN ./gradlew compileJava

CMD ["./gradlew", "run"]