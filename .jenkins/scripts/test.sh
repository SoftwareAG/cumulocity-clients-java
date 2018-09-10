#!/bin/bash
set -e
./mvnw verify
if [ -f microservice/pom.xml ] ;
then
    ../mvnw verify
fi