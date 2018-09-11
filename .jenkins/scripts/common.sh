#!/bin/bash

export resources=hudson@resources.cumulocity.com
export release_args="-Dmaven.javadoc.skip=true -Dskip.microservice.package=false -Dskip.agent.package.container=false -Dnexus.url=http://nexus:8081  -Darguments=-Dskip.microservice.package=false -Dskip.agent.package.rpm=false -Dskip.agent.package.container=false -Dnexus.url=http://nexus:8081"
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