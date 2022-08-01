#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

source ${BASH_SOURCE%/*}/common.sh

call-mvn test -s $MVN_SETTINGS -Pci -U "$@"