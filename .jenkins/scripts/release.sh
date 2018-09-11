#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh


call-mvn clean -T 4
current_version=$(resolve-version)
version=$(next-release ${current_version})
next_version=$(next-snapshot ${version})

echo "Update version to ${version}"
call-mvn versions:set -DnewVersion=${version} 
call-mvn clean deploy ${release_args} 


scp cumulocity-sdk/maven-repository/target/maven-repository-*.tar.gz ${resources}:/tmp/maven-repository-${version}.tar.gz
ssh ${resources}  "mkdir  /tmp/maven-repository-${version} ;  tar -xvzf /tmp/maven-repository-${version}.tar.gz -C /tmp/maven-repository-${version}"
ssh ${resources}  "cp -Rn /tmp/maven-repository-${version}/com/* /var/www/resources/maven/repository/com/ "
ssh ${resources}  "rm -R /tmp/maven-repository-${version}"

tag-version "clients-java-${version}"
cd cumulocity-sdk
tag-version "sdk-${version}"
cd -

echo "Update version to ${next_version}"
call-mvn versions:set -DnewVersion=${version} 
hg commit -m "[maven-release-plugin] prepare for next development iteration"
cd cumulocity-sdk
hg commit -m "[maven-release-plugin] prepare for next development iteration"
cd -
hg push 
cd cumulocity-sdk
hg push 

