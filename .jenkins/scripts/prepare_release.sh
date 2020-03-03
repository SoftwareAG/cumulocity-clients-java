#!/usr/bin/env bash

release_version=r$1
echo "equivalent of git flow release start $release_version"
git branch release/$release_version
git checkout release/$release_version
git commit -m "flow: Created branch release/${release_version}"

cd cumulocity-sdk
git branch release/$release_version
git checkout release/$release_version
git commit -m "flow: Created branch release/${release_version}"

cd -
