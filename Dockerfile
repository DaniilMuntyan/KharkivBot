FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8080 80
WORKDIR /kharkiv_bot
ADD ./target/demo-0.0.1.jar ./kharkiv_bot/app.jar
ENTRYPOINT ["java","-jar","./kharkiv_bot/app.jar"]
