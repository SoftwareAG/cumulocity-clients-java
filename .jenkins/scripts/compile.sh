#!/bin/bash
set +eu
[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"
set -e
./mvnw install -DskipTests -s $MVN_SETTINGS -U -Dmaven.repo.local=.m2/repository
./mvnw de.qaware.maven:go-offline-maven-plugin:resolve-dependencies -Dmaven.repo.local=.m2/repository