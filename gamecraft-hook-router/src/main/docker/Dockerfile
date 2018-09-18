FROM openjdk:8-jre-alpine

ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JHIPSTER_SLEEP=0

# add directly the war
ADD gamecraft-hook-router-1.0-SNAPSHOT.jar /app.jar

VOLUME /tmp
EXPOSE 9999
CMD echo "The application will start in ${JHIPSTER_SLEEP}s..." && \
    sleep ${JHIPSTER_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
