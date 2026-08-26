FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/restApi.jar restApi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "restApi.jar"]
