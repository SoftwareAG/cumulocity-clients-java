#!/usr/bin/env bash
set -e
hotfix_version=$1
development_version=$2

git checkout githubdev
cd cumulocity-sdk
git checkout githubdev
cd -

find . -name 'pom.xml' | xargs sed -i "s/<version>${hotfix_version}<\\/version>/<version>${development_version}<\\/version>/g"
.jenkins/scripts/update_dependencies.sh ${development_version}

git commit -am 'Update dependencies to next SNAPSHOT version' --allow-empty
git push --follow-tags ${REPOSITORY_CLIENTS_JAVA} githubdev

cd cumulocity-sdk

git commit -am 'Update to dependencies next SNAPSHOT version' --allow-empty
git push --follow-tags ${REPOSITORY_SDK} githubdev

cd -

.jenkins/scripts/deploy.sh
