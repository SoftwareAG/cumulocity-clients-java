#!/usr/bin/env bash

release_version=$1

function git_finish_release {
  repository_url=$1
  git pull $repository_url develop
  git commit -am "flow: Closed <release> ${release_version}" --allow-empty
  git checkout develop
  git merge -s recursive -Xtheirs release/r${release_version}
  git commit -am "flow: Merged <release> r${release_version} to <develop> (develop)."  --allow-empty
  git push --follow-tags $repository_url release/r${release_version}
  git push --follow-tags $repository_url develop
}

echo "equivalent of git flow release finish r${release_version}"

git_finish_release https://${GITHUB_USER}:${GITHUB_PASSWORD}@github.com/SoftwareAG/cumulocity-clients-java

cd cumulocity-sdk
git_finish_release https://${GHES_USER}:${GHES_PASSWORD}@github.softwareag.com/IOTA/cumulocity-sdk
