#!/bin/bash
source ${BASH_SOURCE%/*}/common.sh
./mvnw clean
cd microservice
../mvnw clean
cd -
