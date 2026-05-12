FROM eclipse-temurin:21-jdk-jammy
  
COPY interface-ui-test-0.0.1.jar /tmp
RUN chmod +x /tmp/*.jar

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime && echo Asia/Seoul > /etc/timezone

ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/tmp/interface-ui-test-0.0.1.jar"]
