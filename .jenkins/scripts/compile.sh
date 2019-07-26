#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw clean install -DskipTests -s $MVN_SETTINGS -U
./mvnw dependency:go-offline
./mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies