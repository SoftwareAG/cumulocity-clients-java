#!/bin/bash
./mvnw clean -T 8
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw  clean -T 8
fi