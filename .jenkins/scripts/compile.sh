#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh

export MAVEN_OPTS="-Xmx2048m -XX:MetaspaceSize=1024m ${MAVEN_OPTS}"

call-mvn install -Pci -DskipTests -s $MVN_SETTINGS "$@"