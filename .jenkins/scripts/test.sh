#!/bin/bash
set -e
export MAVEN_OPTS="-Xmx4096m -XX:MetaspaceSize=1024m ${MAVEN_OPTS}"
source ${BASH_SOURCE%/*}/common.sh

call-mvn test -s $MVN_SETTINGS -Pci -U "$@"

# MVN_EXEC="$(pwd)/mvnw"

# $MVN_EXEC -s $MVN_SETTINGS test -Pci -U "$@"

# cd microservice

# $MVN_EXEC -s $MVN_SETTINGS test -Pci -U "$@"

# cd -
# cd lpwan-backend

# $MVN_EXEC -s $MVN_SETTINGS test -Pci -U "$@"
# cd -
