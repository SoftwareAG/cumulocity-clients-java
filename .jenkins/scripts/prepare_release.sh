#!/usr/bin/env bash

release_version=$1
branch_name=release/r$release_version
echo "equivalent of git flow release start $release_version"
git branch $branch_name
git checkout $branch_name
git commit -am "flow: Created branch ${branch_name}" --allow-empty

cd cumulocity-sdk
git branch $branch_name
git checkout $branch_name
git commit -am "flow: Created branch ${branch_name}" --allow-empty
cd -
