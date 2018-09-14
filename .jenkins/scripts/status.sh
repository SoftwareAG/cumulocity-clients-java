


function update-status{
    curl --user $BITBUCKET_USER:$BITBUCKET_PASSWORD https://bitbucket.org/rest/build-status/1.0/commits/${MERCURIAL_REVISION} --data "{\"state\":\"$1\", \"name\": \"$2\", \"key\": \"$3\", \"url\": \"\", \"description\": \"\" }"
}