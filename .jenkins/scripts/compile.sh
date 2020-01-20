#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -DskipTests -s $MVN_SETTINGS -U
cd microservice
../mvnw install -DskipTests -s $MVN_SETTINGS -U
cd -