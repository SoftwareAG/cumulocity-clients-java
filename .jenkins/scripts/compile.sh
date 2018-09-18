#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw compile -Pci
cd microservice
../mvnw install -Pci 
cd -