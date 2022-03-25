#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
cd cumulocity-sdk
../mvnw -B clean install -s $MVN_SETTINGS "$@"
cd -