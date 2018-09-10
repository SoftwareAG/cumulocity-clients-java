#!/bin/bash
./mvnw verify  -P ci
if [ -f microservice/pom.xml ] ;
then
    ../mvnw verify  -P ci
fi