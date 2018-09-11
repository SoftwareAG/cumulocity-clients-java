#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
version=$(resolve-vesion)

scp cumulocity-sdk/maven-repository/target/maven-repository-*.tar.gz ${resources}:/tmp/maven-repository-${version}.tar.gz
ssh ${resources}  "mkdir  /tmp/maven-repository-${version} ;  tar -xvzf /tmp/maven-repository-${version}.tar.gz -C /tmp/maven-repository-${version}"
ssh ${resources}  "cp -Rn /tmp/maven-repository-${version}/com/* /var/www/resources/maven/repository/com/ "
ssh ${resources}  "rm -R /tmp/maven-repository-${version}"
