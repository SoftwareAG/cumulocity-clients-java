#!/bin/bash
set -e
release_args="-Dmaven.javadoc.skip=true -Dskip.microservice.package=false -Dskip.agent.package.container=false -Dnexus.url=http://nexus:8081  -Darguments=-Dskip.microservice.package=false -Dskip.agent.package.rpm=false -Dskip.agent.package.container=false -Dnexus.url=http://nexus:8081"
./mvnw release:prepare release:perform ${release_args}
if [ -f microservice/pom.xml ] ;
then
    cd microservice
    ../mvnw release:prepare release:perform ${release_args}
fi