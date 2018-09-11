#!/bin/bash

export $resources=hudson@resources.cumulocity.com

function call-mvn {
    ./mvnw ${@}
    if [ -f microservice/pom.xml ] ;
    then
        cd microservice
        ../mvnw ${@}
        cd -
    else
        echo "Skipping microservice"
    fi
    if [ -f cumulocity-sdk/pom.xml ] ;
    then
        cd cumulocity-sdk
        ../mvnw ${@}
        cd -
    else
        echo "Skipping cumulocity-sdk"
    fi

}

function resolve-version {
    ./mvnw org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version | sed -n -e '/^\[.*\]/ !{ /^[0-9]/ { p; q } }'
}