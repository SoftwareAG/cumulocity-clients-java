#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -DskipTests -s $MVN_SETTINGS -U -e
./mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
cd microservice
../mvnw install -DskipTests -s $MVN_SETTINGS -U -e
../mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
cd -