#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
cd microservice
../mvnw install -DskipTests -s $MVN_SETTINGS -U
../mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies
cd -