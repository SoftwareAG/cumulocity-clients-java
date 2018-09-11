#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh


call-mvn clean -T 4


./mvnw release:prepare  ${release_args} 
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw release:prepare  ${release_args} 
    cd -
else
    echo "Skipping microservice"
fi


version=$(resolve-version)

cd cumulocity-sdk
echo "SDK version set to ${version}"
./mvnw versions:set -DnewVersion=${version}
./mvnw clean install
hg commit -m "Update to next RELEASE version"
hg push