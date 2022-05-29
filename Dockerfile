FROM openjdk:11
MAINTAINER matias-palomeque-portfolio.web.app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} disney-world-backend-docker.jar
ENTRYPOINT ["java", "-jar", "disney-world-backend-docker.jar"]