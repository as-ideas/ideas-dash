#!/bin/sh

echo "Copy app-jar..."
cp ../../../target/dash-example-1.0-SNAPSHOT.jar ./app.jar

echo "Login & Pull base image"
docker login --username=asjenkins --password=ideas987
docker pull asideas/pcp-java-baseimage:19

echo "Build docker container"
docker build -t "asideas/ideas-dash-example:3" -f Dockerfile .

echo "Push docker containers..."
docker push asideas/ideas-dash-example:3

rm ./app.jar
