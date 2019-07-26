#!/bin/bash
set -e
./mvnw clean install -DskipTests -U
./mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies