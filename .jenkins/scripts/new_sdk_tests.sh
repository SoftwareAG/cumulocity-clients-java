##!/bin/bash

DEPLOYMENT_ENVIRONMENT=$1


./mvnw -s $MVN_SETTINGS clean install -Pintegration -Dcumulocity.host=http://${DEPLOYMENT_ENVIRONMENT}



