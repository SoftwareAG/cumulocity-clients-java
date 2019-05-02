#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw deploy -Pci
cd microservice
../mvnw deploy -Pci 
cd -
