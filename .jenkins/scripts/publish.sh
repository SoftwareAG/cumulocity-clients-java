#!/bin/bash
set -e
./mvnw  clean deploy  -DskipTests  -Dnexus.host=http://nexus:8081 
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw clean deploy  -DskipTests  -Dnexus.host=http://nexus:8081 
fi