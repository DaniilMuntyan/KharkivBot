FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080 80
ARG JAR_FILE=target/*.jar
RUN mkdir /workdir
WORKDIR /workdir
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
