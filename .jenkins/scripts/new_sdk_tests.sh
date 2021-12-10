##!/bin/bash

DEPLOYMENT_ENVIRONMENT=$1
MANAGE_PASS=$2
ADDITIONAL_BUILD_ARGS="${BUILD_ARGS:-''}"

echo "additional args: $ADDITIONAL_BUILD_ARGS"

if [[ -z "$3" ]]; then
  MANAGE_USER=admin
else
  MANAGE_USER="$3"
fi

./mvnw -s $MVN_SETTINGS clean install $ADDITIONAL_BUILD_ARGS -Pintegration -Dcumulocity.host=http://${DEPLOYMENT_ENVIRONMENT} -Dcumulocity.management.password=${MANAGE_PASS} -Dcumulocity.management.username=${MANAGE_USER}




    