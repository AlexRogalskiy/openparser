# Base OS layer
# docker build -t container_tag --build-arg IMAGE_SOURCE=openjdk IMAGE_TAG=latest

ARG IMAGE_SOURCE=openjdk
ARG IMAGE_TAG=latest

FROM ${IMAGE_SOURCE}:${IMAGE_TAG}

RUN groupadd spring && useradd -m -g spring -s /bin/bash spring
USER spring:spring

VOLUME /tmp

LABEL org.opencontainers.image.title="GkOmega.OpenParser"
LABEL org.opencontainers.image.description="GkOmega OpenParser application"
LABEL org.opencontainers.image.vendor="GkOmega, Ltd."
LABEL org.opencontainers.image.version="1.0"

# The application's jar file
ARG JAR_FILE

# Add the application's jar to the container
ADD ${JAR_FILE} /opt/app/ru.gkomega.api-openparser-solr.jar
ADD ./entrypoint.sh /opt/app/entrypoint.sh

# Set workdir inside container
WORKDIR /opt/app

# Expose service default port
EXPOSE 8080

# General environment options
ENV TERM xterm

# Run service process
ENTRYPOINT [ "./entrypoint.sh" ]
CMD ["OpenParserSolr container started"]
