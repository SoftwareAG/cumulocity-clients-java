#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
cd cumulocity-sdk
../mvnw install
cd -