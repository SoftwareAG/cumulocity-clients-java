#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

DEPLOYMENT_ENVIRONMENT = $1

source /etc/profile.d/java.sh
cd java-client && mvn -B clean install -Pintegration -Dcumulocity.host=http://${DEPLOYMENT_ENVIRONMENT}




    