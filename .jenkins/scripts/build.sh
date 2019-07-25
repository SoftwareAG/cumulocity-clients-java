#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw -o install
cd microservice
../mvnw -o install -Pci
cd -
