#!/bin/bash
source ${BASH_SOURCE%/*}/semver.sh
set +e
[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"
set -e

export resources=hudson@yum.cumulocity.com
export release_args="-DskipTests -Dmaven.javadoc.skip=true -Dskip.microservice.package=false -Dskip.agent.package.container=false -Dnexus.url=http://nexus:8081  -Darguments=-Dskip.microservice.package=false -Dskip.agent.package.rpm=false -Dskip.agent.package.container=false -Dnexus.url=http://nexus:8081"
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

function next-release {
     version-bump release $1
}

function next-snapshot {
     echo $(version-bump patch $1)-SNAPSHOT
}

function tag-version {
    tag=$1
    hg commit -m "[maven-release-plugin] prepare release ${tag}" || echo ""
    hg tag -f -m "copy for tag ${tag}" "${tag}"
}