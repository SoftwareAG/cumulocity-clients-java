#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
call-mvn install -DskipTests -s $MVN_SETTINGS -U
