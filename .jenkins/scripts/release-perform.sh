#!/bin/bash
set -e

source ${BASH_SOURCE%/*}/common.sh


./mvnw release:perform ${release_args} 
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw release:perform ${release_args} 
    cd -
else
    echo "Skipping microservice"
fi


version=$(resolve-version)

cd cumulocity-sdk
echo "SDK version set to ${version}"
./mvnw versions:set -DnewVersion=${version}

hg commit -m "Update to next SNAPSHOT version"