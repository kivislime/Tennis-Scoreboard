#!/bin/bash
set -e

./gradlew clean build

if ! docker ps | grep -q "TennisScoreboardTest"; then
    docker run -d --name TennisScoreboardTest -p 8080:8080 tomcat:10-jdk11-openjdk
fi

docker cp build/libs/TennisScoreboard-1.0-SNAPSHOT.war TennisScoreboardTest:/usr/local/tomcat/webapps/TennisScoreboard.war

docker restart TennisScoreboardTest

echo "Deploy success!"
