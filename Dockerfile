FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/travel-agency-app.jar app.jar

# Railway provides PORT environment variable
EXPOSE 8080

CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]
