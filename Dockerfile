FROM openjdk:11.0.3-stretch

WORKDIR /app
COPY . /app
RUN ./gradlew shadowJar

CMD java -jar ./build/libs/Bluetooth-location-server-all.jar
