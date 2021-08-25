#!/usr/bin/env bash
set -e
release_version=$1

function git_finish_release {
  repository_url=$1
  git pull $repository_url githubdev
  git commit -am "flow: Closed <release> ${release_version}" --allow-empty
  git checkout githubdev
  git merge -s recursive -Xtheirs release/r${release_version}
  git commit -am "flow: Merged <release> r${release_version} to <githubdev> (githubdev)."  --allow-empty
  git push --follow-tags $repository_url release/r${release_version}
  git push --follow-tags $repository_url githubdev
}

echo "equivalent of git flow release finish r${release_version}"

  git_finish_release ${REPOSITORY_CLIENTS_JAVA}
  cd cumulocity-sdk
  git_finish_release ${REPOSITORY_SDK}


