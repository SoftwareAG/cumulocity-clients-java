#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

source ${BASH_SOURCE%/*}/common.sh

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