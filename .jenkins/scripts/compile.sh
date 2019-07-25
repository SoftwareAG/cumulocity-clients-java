#!/bin/bash
set -e
./mvnw install -DskipTests -s $MVN_SETTINGS -U
./mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies