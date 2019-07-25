#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw -o install -Dskip.microservice.package=false -Dskip.agent.package.container=false -Pci
cd microservice
../mvnw -o install -Dskip.microservice.package=false -Dskip.agent.package.container=false -Pci
cd -
