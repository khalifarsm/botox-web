FROM alpine:3.18.6
ARG JAR_FILE=target/pandora-admin-0.0.1-SNAPSHOT.jar
RUN  apk update \
  && apk upgrade \
  && apk add ca-certificates \
  && update-ca-certificates \
  && apk add --update coreutils && rm -rf /var/cache/apk/*   \
  && apk add --update openjdk17 tzdata curl unzip bash \
  && apk add --no-cache nss \
  && rm -rf /var/cache/apk/*
WORKDIR /home/spring
COPY ${JAR_FILE} app.jar
EXPOSE 8104/tcp
USER 1000
CMD java -jar -Dspring.profiles.active=$PANDORA_ENV app.jar