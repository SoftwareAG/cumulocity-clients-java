##!/bin/bash

DEPLOYMENT_ENVIRONMENT=$1
MANAGE_PASS=$2


./mvnw -s $MVN_SETTINGS clean install -Pintegration -Dcumulocity.host=http://${DEPLOYMENT_ENVIRONMENT} -Dcumulocity.management.password=${MANAGE_PASS}




    