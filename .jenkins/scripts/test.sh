#!/bin/bash
set -e
export MAVEN_OPTS="-Xmx4096m -XX:MetaspaceSize=1024m ${MAVEN_OPTS}"
source ${BASH_SOURCE%/*}/common.sh

call-mvn test -s $MVN_SETTINGS -Pci -U "$@"