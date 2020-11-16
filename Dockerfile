FROM adoptopenjdk/openjdk11:slim
MAINTAINER "William Custodio"

ENV PORT 8080
EXPOSE 8080

COPY target/shipping-api.jar /opt/application.jar

WORKDIR /opt
# sleep the application execution for 15 seconds to allow the database to startup and avoid connection failure.
# this could be improved by creating a script that listens to a host and port and verify if it is up and running.
CMD ["sh", "-c", "sleep 15 && java $APPLICATION_ARGS -jar application.jar"]
