#!/bin/bash
set -e
./mvnw compile
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw compile
fi