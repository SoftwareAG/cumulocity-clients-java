#!/usr/bin/env bash


function update-status {
    echo $(curl -s --user ${BITBUCKET_USER}:${BITBUCKET_PASSWORD} -H "Content-Type: application/json" -X POST https://bitbucket.org/rest/build-status/1.0/commits/${MERCURIAL_REVISION} --data "{\"state\":\"${1}\", \"name\": \"${2}\", \"key\": \"${3}\", \"url\": \"\", \"description\": \"\" }")
}

update-status $1 $2 $3 