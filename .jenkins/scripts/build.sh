#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -Pci
cd microservice
../mvnw install -Pci 
cd -
