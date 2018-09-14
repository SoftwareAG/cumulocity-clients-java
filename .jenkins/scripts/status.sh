#!/usr/bin/env bash


function update-status {
    echo $(curl -s --user ${BITBUCKET_USER}:${BITBUCKET_PASSWORD} -H "Content-Type: application/json" -X POST https://api.bitbucket.org/2.0/repositories/m2m/cumulocity-clients-java/commit/${MERCURIAL_REVISION}/statuses/build --data "{\"state\":\"${1}\", \"name\": \"${2}\", \"key\": \"${3}\", \"url\": \"http://localhost:8081/job/Cumulocity-JavaSDK/job/${BRANCH_NAME}/${BUILD_ID}/\", \"description\": \"\" }")
}

update-status $1 $2 $3 