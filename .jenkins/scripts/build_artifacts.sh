#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
export MAVEN_OPTS="-Xmx2048m -XX:MetaspaceSize=1024m ${MAVEN_OPTS}"

MVN_EXEC="$(pwd)/mvnw"

$MVN_EXEC -B generate-resources \
  -s $MVN_SETTINGS -U \
  -Pjavadoc \
  -DskipTests=true "$@"

cd microservice

$MVN_EXEC -B javadoc:aggregate-jar \
  -s $MVN_SETTINGS "$@"

cd -
cd lpwan-backend

$MVN_EXEC -B javadoc:aggregate-jar \
  -s $MVN_SETTINGS "$@"
  
cd -

cd cumulocity-sdk

$MVN_EXEC -B package \
  -s $MVN_SETTINGS -U \
  -DskipTests=true "$@"

cd -