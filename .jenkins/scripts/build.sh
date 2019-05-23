#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -Pci -s $MVN_SETTINGS
cd microservice
../mvnw install -Pci -s $MVN_SETTINGS
cd -
