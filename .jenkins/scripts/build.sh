#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw -o clean install -Dskip.microservice.package=false -Dskip.agent.package.container=false -Pci
cd microservice
../mvnw -o clean install -Dskip.microservice.package=false -Dskip.agent.package.container=false -Pci
cd -
