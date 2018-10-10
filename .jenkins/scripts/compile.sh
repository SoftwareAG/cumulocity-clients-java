#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
./mvnw install -DskipTests