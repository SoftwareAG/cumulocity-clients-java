#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -DskipTests
cd microservice
../mvnw install -DskipTests
cd -