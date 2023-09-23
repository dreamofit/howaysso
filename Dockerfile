FROM openjdk:17-alpine
ADD howaysso-access/target/howaysso-access-1.0.0.jar howaysso-access-1.0.0.jar
EXPOSE 8082
CMD ["java","--add-opens=java.base/java.util=ALL-UNNAMED","-jar","howaysso-access-1.0.0.jar"]