#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

source ${BASH_SOURCE%/*}/common.sh

call-mvn install -Pci -DskipTests -s $MVN_SETTINGS "$@"