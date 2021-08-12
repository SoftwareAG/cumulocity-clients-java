#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
call-mvn clean deploy  -DskipTests -s $MVN_SETTINGS