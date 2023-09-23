FROM openjdk:17-alpine
#修改时区
RUN apk --update add tzdata && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone && \
    apk del tzdata && \
    rm -rf /var/cache/apk/*
ADD howaysso-access/target/howaysso-access-1.0.0.jar howaysso-access-1.0.0.jar
EXPOSE 8081
#–add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED
CMD ["java","--add-opens=java.base/java.lang=ALL-UNNAMED","--add-opens=java.base/java.lang.reflect=ALL-UNNAMED","-jar","howaysso-access-1.0.0.jar"]