FROM azul/zulu-openjdk-alpine:11-jre
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8011
ENTRYPOINT ["java","-jar","/app.jar"]
