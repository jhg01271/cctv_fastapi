FROM eclipse-temurin:11-jdk-jammy
  
#COPY smartCrane-0.0.1-SNAPSHOT.jar /tmp
#RUN chmod +x /tmp/*.jar

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo Asia/Seoul > /etc/timezone

ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/tmp/backend-0.0.1-SNAPSHOT.jar"]
