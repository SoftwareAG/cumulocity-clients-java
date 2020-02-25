#!/usr/bin/env bash

release_version=$1

echo "equivalent of git flow release finish r${release_version}"
git pull  https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-clients-java develop
git tag archive/release/r${release_version} release/r${release_version}
git commit -m "flow: archived <release> ${release_version}" 
git checkout develop
git mergetool --tool=vimdiff
git merge release/r${release_version}
git commit --message "flow: Merged <release> r${release_version} to <develop> (develop)."
git push --new-branch -b release/r${release_version} https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-clients-java
git push -b develop https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-clients-java

cd cumulocity-sdk
git pull  https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-sdk develop
git tag archive/release/r${release_version} release/r${release_version}
git commit -m "flow: archived <release> ${release_version}" 
git checkout develop
git mergetool --tool=vimdiff
git merge release/r${release_version}
git commit -m "flow: Merged <release> r${release_version} to <develop> (develop)."
git push  https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-sdk release/r${release_version}
git push  https://${BITBUCKET_USER}:${BITBUCKET_PASSWORD}@bitbucket.org/m2m/cumulocity-sdk develop
cd -