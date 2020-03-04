#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
mv cumulocity-sdk-test-git cumulocity-sdk
cd cumulocity-sdk
../mvnw clean install -s $MVN_SETTINGS -U
cd -