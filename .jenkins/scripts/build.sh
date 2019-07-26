#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
cd microservice
../mvnw -o install -Pci
cd -
