#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
cd cumulocity-sdk
../mvnw -s $MVN_SETTINGS clean install  -U
cd -