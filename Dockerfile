# Stage 1: build
FROM gradle:7.5-jdk11 AS builder
WORKDIR /app
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test

# Stage 2: runtime
FROM tomcat:10-jdk11-openjdk
LABEL maintainer="kivislime.org"
RUN rm -rf /usr/local/tomcat/webapps/ROOT*
COPY --from=builder /app/build/libs/TennisScoreboard-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
