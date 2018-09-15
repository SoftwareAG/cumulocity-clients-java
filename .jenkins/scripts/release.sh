#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh


call-mvn clean -T 4

hg pull -u 
hg up -C ${BRANCH_NAME}
cd cumulocity-sdk
hg pull -u 
hg up -C ${BRANCH_NAME}
cd -

current_version=$(resolve-version)
version=$(next-release ${current_version})
next_version=$(next-snapshot ${version})

echo "Update version to ${version}"
call-mvn versions:set -DnewVersion=${version} 
call-mvn clean deploy ${release_args} 

echo "Publish cumulocity-sdk/maven-repository/target/maven-repository-${version}.tar.gz to resources tmp "
scp cumulocity-sdk/maven-repository/target/maven-repository-*.tar.gz ${resources}:/tmp/maven-repository-${version}.tar.gz
ssh ${resources}  "mkdir  /tmp/maven-repository-${version} ;  tar -xvzf /tmp/maven-repository-${version}.tar.gz -C /tmp/maven-repository-${version}"
echo "Publish extracted files to maven repository"
ssh ${resources}  "sudo cp -Rn /tmp/maven-repository-${version}/com/* /var/www/resources/maven/repository/com/ "
echo "Cleanup tmp files"
ssh ${resources}  "rm -R /tmp/maven-repository-${version}*"
echo "tagging cumulocity-clients-java"
tag-version "clients-java-${version}"
echo "tagging cumulocity-sdk"
cd cumulocity-sdk
tag-version "sdk-${version}"
cd -

echo "Update version to ${next_version}"
call-mvn versions:set -DnewVersion=${next_version} -DgenerateBackupPoms=false
hg commit -m "[maven-release-plugin] prepare for next development iteration"
cd cumulocity-sdk
hg commit -m "[maven-release-plugin] prepare for next development iteration"
cd -
hg push -r${BRANCH_NAME}
cd cumulocity-sdk
hg push -r${BRANCH_NAME}

