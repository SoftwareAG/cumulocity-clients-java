#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh

./mvnw -B install -Pci,javadoc -s $MVN_SETTINGS "$@"
cd microservice
../mvnw -B install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS "$@"
cd -
cd lpwan-backend
../mvnw -B install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS "$@"
cd - 

