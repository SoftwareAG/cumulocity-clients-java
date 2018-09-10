#!/bin/bash
set -e
./mvnw verify  -P ci
if [ -f microservice/pom.xml ] ;
then
    ../mvnw verify  -P ci
fi