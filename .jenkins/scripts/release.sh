#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh

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

call-mvn -s $MVN_SETTINGS clean -T 4
#if it is a release on develop branch, 'git symbolic-ref --short HEAD' will return release/rX.X.X as it is the branch created in previous step.
# If it is a release/hotfix on release branch it should just push the branch it was on
branch_name=$(git symbolic-ref --short HEAD)
if [ "develop" == "${branch_name}" ]; then
    branch_name="release/r${version}"
fi
echo "branch name: $branch_name"

echo checkout to new branch
git checkout ${branch_name}
git push --follow-tags https://${CLIENTS_USER}:${CLIENTS_PASSWORD}@${REPOSITORY_CLIENTS_JAVA}/cumulocity-clients-java ${branch_name}
git pull https://${CLIENTS_USER}:${CLIENTS_PASSWORD}@${REPOSITORY_CLIENTS_JAVA}/cumulocity-clients-java ${branch_name}

cd cumulocity-sdk
git checkout ${branch_name}
git push --follow-tags https://${SDK_USER}:${SDK_PASSWORD}@${REPOSITORY_SDK}/cumulocity-sdk ${branch_name}
git pull https://${SDK_USER}:${SDK_PASSWORD}@${REPOSITORY_SDK}/cumulocity-sdk ${branch_name}
cd -

echo "Update version to ${version}"
call-mvn -s $MVN_SETTINGS versions:set -DnewVersion=${version}
call-mvn -s $MVN_SETTINGS clean deploy -Dmaven.javadoc.skip=true

echo "Publish cumulocity-sdk/maven-repository/target/maven-repository-${version}.tar.gz to resources tmp "
scp cumulocity-sdk/maven-repository/target/maven-repository-*.tar.gz ${resources}:/tmp/maven-repository-${version}.tar.gz
ssh ${resources}  "mkdir  /tmp/maven-repository-${version} ;  tar -xvzf /tmp/maven-repository-${version}.tar.gz -C /tmp/maven-repository-${version}"
echo "Publish extracted files to maven repository"
ssh ${resources}  "cp -Rn /tmp/maven-repository-${version}/com/* /var/www/resources/maven/repository/com/ "
echo "Cleanup tmp files"
ssh ${resources}  "rm -R /tmp/maven-repository-${version}*"
echo "tagging cumulocity-clients-java"
tag-version "clients-java-${version}"
echo "tagging cumulocity-sdk"
cd cumulocity-sdk
tag-version "sdk-${version}"
cd -

echo "Update version to ${next_version}"
call-mvn -s $MVN_SETTINGS versions:set -DnewVersion=${next_version} -DgenerateBackupPoms=false
git commit -am "[maven-release-plugin] prepare for next development iteration" --allow-empty
cd cumulocity-sdk
git commit -am "[maven-release-plugin] prepare for next development iteration" --allow-empty
cd -
echo "Push repositories"
git push --follow-tags https://${CLIENTS_USER}:${CLIENTS_PASSWORD}@${REPOSITORY_CLIENTS_JAVA}/cumulocity-clients-java ${branch_name}
cd cumulocity-sdk
git push --follow-tags https://${SDK_USER}:${SDK_PASSWORD}@${REPOSITORY_SDK}/cumulocity-sdk ${branch_name}
cd -

.jenkins/scripts/deploy.sh
