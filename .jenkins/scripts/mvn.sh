#!/bin/bash -e

# Allows to run Maven build, with additional parameters specified using environmental variables

MAVEN_ARGS="${MAVEN_ARGS:-} ${@}"
VERBOSE=true
FILE=false

while [ $# -gt 0 ]; do
  case "$1" in
    -f | --file ) FILE=true; shift ;;
    -q | --quiet ) VERBOSE=false; shift ;;
    * ) shift ;;
  esac
done

if [ -n "${ADMIN_CREDENTIALS}" ]; then
  MAVEN_ARGS="--define \"management.admin.username=${ADMIN_CREDENTIALS_USR}\" ${MAVEN_ARGS}"
  MAVEN_ARGS="--define \"management.admin.password=${ADMIN_CREDENTIALS_PSW}\" ${MAVEN_ARGS}"
fi

if [ -n "${VERSION}" ]; then
  MAVEN_ARGS="--define revision=${VERSION} ${MAVEN_ARGS}"
  if [ -n "${CHANGE_VERSION}" ]; then
    MAVEN_ARGS="--define changelist=${CHANGE_VERSION} ${MAVEN_ARGS}"
  else
    MAVEN_ARGS="--define changelist= ${MAVEN_ARGS}"
  fi
else
  if [ -n "${CHANGE_VERSION}" ]; then
    MAVEN_ARGS="--define changelist=${CHANGE_VERSION} ${MAVEN_ARGS}"
  fi
fi

MAVEN_PROFILES="${MAVEN_PROFILES:-ci}"
MAVEN_ARGS="--activate-profiles $MAVEN_PROFILES ${MAVEN_ARGS}"

if [ -n "$WORKSPACE" ]; then
  MAVEN_ARGS="--define \"maven.repo.local=${WORKSPACE}/.m2/repository\" ${MAVEN_ARGS}"
fi

MVN_SETTINGS="${MVN_SETTINGS:-$HOME/.m2/settings.xml}"
MAVEN_ARGS="--settings $MVN_SETTINGS ${MAVEN_ARGS}"

if [ $VERBOSE = true ]; then
  MAVEN_ARGS="--show-version --errors ${MAVEN_ARGS}"
  set -x
fi

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

if [ $FILE = true ]; then # run specified file only
  ./mvnw --batch-mode ${MAVEN_ARGS}
else # run all contained sub-projects
  for project in '.' 'microservice' 'lpwan-backend' 'cumulocity-sdk'; do
  if [ -d "$project" ]; then
    ./mvnw --batch-mode --file ${project} ${MAVEN_ARGS}
  fi
done
fi
