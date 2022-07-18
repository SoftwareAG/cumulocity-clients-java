#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

MVN_SETTINGS="${MVN_SETTINGS:-$HOME/.m2/settings.xml}"

MAVEN_PROFILES="${MAVEN_PROFILES:-ci}"

MAVEN_ARGS="${MAVEN_ARGS:-}"

if [ -n "$WORKSPACE" ]; then
  MAVEN_ARGS="--define maven.repo.local=${WORKSPACE}/.m2/repository ${MAVEN_ARGS}"
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

for project in '.' 'microservice' 'lpwan-backend' 'cumulocity-sdk'; do
  if [ -d "$project" ]; then
    ./mvnw --batch-mode --show-version --errors --settings $MVN_SETTINGS \
      --file $project --activate-profiles $MAVEN_PROFILES $MAVEN_ARGS "$@"
  fi
done
