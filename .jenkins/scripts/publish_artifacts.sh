#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
RELEASE_TYPE=$1
shift 1

export MAVEN_OPTS="-Xmx2048m -XX:MetaspaceSize=1024m ${MAVEN_OPTS}"

call-mvn -s $MVN_SETTINGS deploy -T 2C -Dmaven.install.skip=true $release_args -DskipTests "$@"

#TODO upload java artifacts to maven repo (see release.sh script)