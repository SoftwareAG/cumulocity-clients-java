#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -Pci -X -o
cd microservice
../mvnw install -Pci  -X -o
cd -
