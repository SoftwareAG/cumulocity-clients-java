#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -Pci,javadoc
cd microservice
../mvnw install javadoc:aggregate-jar -Pci
cd -
