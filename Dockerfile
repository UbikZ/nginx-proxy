FROM maven:3-jdk-8-alpine

MAINTAINER Gabriel Malet <gabriemalet@gmail.com>

ENV PROJECT_NAME "proxify"
ENV PROJECT_VERSION "0.0.1-SNAPSHOT"

RUN apk update \
    && apk add bash \
    && rm -rf /var/cache/apk/*

ADD . /${PROJECT_NAME}
WORKDIR /${PROJECT_NAME}

COPY scripts/entrypoint.sh /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]