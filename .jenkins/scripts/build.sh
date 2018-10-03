#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
exit -1
./mvnw install -Pci
cd microservice
../mvnw install -Pci 
cd -
