#!/bin/bash
set +eu
[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"
set -e

add-key-for-host(){
  mkdir -p ~/.ssh > /dev/null || echo
  grep -qF $1 ~/.ssh/known_hosts || ssh-keyscan -t rsa $1 >> ~/.ssh/known_hosts
}
add-key-for-host bitbucket.org
add-key-for-host yum.cumulocity.com
export resources=hudson@yum.cumulocity.com
export release_args="-DskipTests -Dmaven.javadoc.skip=true -Dskip.microservice.package=false -Dskip.agent.package.container=false  -Darguments=-Dskip.microservice.package=false -Dskip.agent.package.rpm=false -Dskip.agent.package.container=false"

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

function tag-version {
    tag=$1
    git commit -am "[maven-release-plugin] prepare release ${tag}" --allow-empty || echo ""
    git tag -f -m "copy for tag ${tag}" "${tag}" # create tag with additional message "copy for tag TAG_TAME" and remove old one if exists
}

function update-property {
    property=$1
    property_value=$2
    echo "update property ${property} to value ${property_value}"
    find . -name 'pom.xml' | xargs sed -i "s/<${property}>.*<\/${property}>/<${property}>${property_value}<\/${property}>/g"
}
