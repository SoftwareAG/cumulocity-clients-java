#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh

repository_url=https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m
repository_clients_java=$repository_url/cumulocity-clients-java
repository_sdk=$repository_url/cumulocity-sdk

while [ "$1" != "" ]; do
    case $1 in
        -r | --release )        shift
                                version=$1
                                ;;
        -d | --development )    shift
                                next_version=$1
                                ;;
        -b | --build_type )     shift
                                build_type=$1
                                ;;
        *)                      ;;
    esac
    shift
done

call-mvn clean -T 4
#if it is a release on develop branch, hg branch (git symbolic-ref --short HEAD) will return release/rX.X.X as it is the branch created in previous step.
# If it is a release/hotfix on release branch it should just push the branch it was on
branch_name=$(git symbolic-ref --short HEAD)
if [ "develop" == "${branch_name}" ]; then
    branch_name="release/r${version}"
fi
echo "branch name: $branch_name"

echo checkout to new branch
git checkout ${branch_name}
git push --follow-tags $repository_clients_java ${branch_name}
git pull $repository_clients_java ${branch_name}

cd cumulocity-sdk
git checkout ${branch_name}
git push --follow-tags $repository_sdk ${branch_name}
git pull $repository_sdk ${branch_name}
cd -

echo "Update version to ${version}"
call-mvn versions:set -DnewVersion=${version}
call-mvn clean deploy -Dmaven.javadoc.skip=true -s $MVN_SETTINGS
./mvnw generate-resources -Pjavadoc
cd microservice
../mvnw javadoc:aggregate-jar
cd -

echo "Publish cumulocity-sdk/maven-repository/target/maven-repository-${version}.tar.gz to resources tmp "
scp cumulocity-sdk/maven-repository/target/maven-repository-*.tar.gz ${resources}:/tmp/maven-repository-${version}.tar.gz
ssh ${resources}  "mkdir  /tmp/maven-repository-${version} ;  tar -xvzf /tmp/maven-repository-${version}.tar.gz -C /tmp/maven-repository-${version}"
echo "Publish extracted files to maven repository"
ssh ${resources}  "cp -Rn /tmp/maven-repository-${version}/com/* /var/www/resources/maven/repository/com/ "
echo "Cleanup tmp files"
ssh ${resources}  "rm -R /tmp/maven-repository-${version}*"

echo "Publishing java-client/target/java-client-${version}-javadoc.jar to resources tmp "
scp java-client/target/java-client-${version}-javadoc.jar ${resources}:/tmp/java-client-${version}-javadoc.jar
ssh ${resources} "mkdir /resources/documentation/javasdk/${version} ; unzip /tmp/java-client-${version}-javadoc.jar -d /resources/documentation/javasdk/${version}"
if [ "RELEASE" == "${build_type}" ]; then
    echo "Update current symbolic link of javasdk javadocs"
    ssh ${resources} "rm -f /resources/documentation/javasdk/current ; ln -s /resources/documentation/javasdk/${version} /resources/documentation/javasdk/current"
fi
echo "Delete /tmp/java-client-${version}-javadoc.jar file"
ssh ${resources}  "rm -f /tmp/java-client-${version}-javadoc.jar"

echo "Publishing microservice/target/microservice-dependencies-${version}-javadoc.jar to resources tmp "
scp microservice/target/microservice-dependencies-${version}-javadoc.jar ${resources}:/tmp/microservice-dependencies-${version}-javadoc.jar
ssh ${resources} "mkdir /resources/documentation/microservicesdk/${version} ; unzip /tmp/microservice-dependencies-${version}-javadoc.jar -d /resources/documentation/microservicesdk/${version}"
if [ "RELEASE" == "${build_type}" ]; then
    echo "Update current symbolic link of microservicesdk javadocs"
    ssh ${resources} "rm -f /resources/documentation/microservicesdk/current ; ln -s /resources/documentation/microservicesdk/${version} /resources/documentation/microservicesdk/current"
fi
echo "Delete /tmp/microservice-dependencies-${version}-javadoc.jar file"
ssh ${resources}  "rm -f /tmp/microservice-dependencies-${version}-javadoc.jar"

echo "tagging cumulocity-clients-java"
tag-version "clients-java-${version}"
echo "tagging cumulocity-sdk"
cd cumulocity-sdk
tag-version "sdk-${version}"
cd -

echo "Update version to ${next_version}"
call-mvn versions:set -DnewVersion=${next_version} -DgenerateBackupPoms=false
git commit -am "[maven-release-plugin] prepare for next development iteration" --allow-empty
cd cumulocity-sdk
git commit -am "[maven-release-plugin] prepare for next development iteration" --allow-empty
cd -
echo "Push repositores"
git push --follow-tags $repository_clients_java ${branch_name}
cd cumulocity-sdk
git push --follow-tags $repository_sdk ${branch_name}
cd -

.jenkins/scripts/deploy.sh
