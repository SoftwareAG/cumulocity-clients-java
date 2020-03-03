#!/usr/bin/env bash

source ${BASH_SOURCE%/*}/update_dependencies.sh

hotfix_version=$1
development_version=$2

git checkout develop
cd cumulocity-sdk
git checkout develop
cd -

find . -name 'pom.xml' | xargs sed -i "s/<version>${hotfix_version}<\\/version>/<version>${development_version}<\\/version>/g"
# update-dependencies ${development_version}

git commit -m 'Update dependencies to next SNAPSHOT version'
git push https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-clients-java develop

cd cumulocity-sdk

git commit -m 'Update to dependencies next SNAPSHOT version'
git push https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-sdk develop

cd -

.jenkins/scripts/deploy.sh