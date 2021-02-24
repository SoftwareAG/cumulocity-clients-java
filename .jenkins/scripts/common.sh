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
    if [ -f microservice/lpwan-backend/pom.xml ] ;
    then
        cd microservice/lpwan-backend
        ../../mvnw ${@}
        cd ../..
    else
        echo "Skipping lpwan-backend"
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
    echo "update property ${1} to value ${2}"
    find . -name 'pom.xml' | xargs sed -i "s/<${1}>.*<\/${1}>/<${1}>${2}<\/${1}>/g"
}
