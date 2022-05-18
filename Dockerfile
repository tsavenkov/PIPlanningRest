FROM amazoncorretto:11
RUN #apk add --no-cache ttf-dejavu

LABEL maintainer="Timo"



EXPOSE 80

WORKDIR /home
RUN mkdir logs
RUN chmod 777 /home/logs

#USER 1000
#RUN touch ./logs/application-debug.log
COPY app-starter/target/app-starter-0.0.1-SNAPSHOT.jar /home/solver.jar

ENTRYPOINT [ "java", "-jar", "-Xmx4098M", "-Xms2048M", "-XX:+UseSerialGC", "-Dlog4j.formatMsgNoLookups=true", "/home/solver.jar" ]