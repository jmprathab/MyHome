FROM azul/zulu-openjdk-alpine:8-jre
ARG JAR_FILE=service/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
