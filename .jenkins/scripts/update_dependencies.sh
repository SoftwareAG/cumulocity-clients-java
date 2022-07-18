#!/bin/bash -xe

source ${BASH_SOURCE%/*}/common.sh

function update-dependencies {
    echo "Update properties in SDK POMs to new version"

    if [ -n "$(git status -s)" ]; then
        git commit -am "Update dependencies to ${1} version" --allow-empty
    else
        echo "no changes found no commit"
    fi
    cd -

}

update-dependencies $1
