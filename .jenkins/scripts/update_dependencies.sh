#!/usr/bin/env bash
set -e
source ${BASH_SOURCE%/*}/common.sh

function update-dependencies {
    echo "Update properties in POMs to new version"
    PROPERTIES=(cumulocity.root.version cumulocity.dependencies.version cumulocity.model.version cumulocity.shared-components.version cumulocity.core.version)

    for property in "${PROPERTIES[@]}"
    do
        update-property $property ${1}
    done
        hg commit -m "Update dependencies to new version"

    cd cumulocity-sdk
    for property in "${PROPERTIES[@]}"
    do
        update-property $property ${1}
    done
    hg commit -m "Update dependencies to new version"

}

update-dependencies $1