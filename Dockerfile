FROM openjdk:21-jdk-slim
LABEL authors="MaiHuy"
ENV APP_JAR=LaptopShop-0.0.1-SNAPSHOT.jar
WORKDIR /app
COPY target/${APP_JAR} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
