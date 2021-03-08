FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080 80
ADD ./target/demo-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","./app.jar"]
