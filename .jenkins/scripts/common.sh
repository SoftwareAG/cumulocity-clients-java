#!/bin/bash


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