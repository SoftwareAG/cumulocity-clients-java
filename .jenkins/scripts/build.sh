#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

source ${BASH_SOURCE%/*}/common.sh

./mvnw -B install -Pci,javadoc -s $MVN_SETTINGS "$@"
cd microservice
../mvnw -B install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS "$@"
cd -
cd lpwan-backend
../mvnw -B install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS "$@"
cd - 

