##!/bin/bash

DEPLOYMENT_ENVIRONMENT=$1

source /etc/profile.d/java.sh
./mvnw -s $MVN_SETTINGS clean install -Pintegration -Dcumulocity.host=http://${DEPLOYMENT_ENVIRONMENT}




    