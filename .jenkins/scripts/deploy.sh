#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
call-mvn clean deploy  -DskipTests  -Dnexus.host=http://nexus:8081 