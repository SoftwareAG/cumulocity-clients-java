#!/bin/bash
./mvnw compile -P ci
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw compile -P ci
fi