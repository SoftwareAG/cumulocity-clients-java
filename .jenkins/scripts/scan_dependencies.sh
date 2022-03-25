#!/usr/bin/env bash
if [ -z "$TPP_FETCHER_URL" ]; then TPP_FETCHER_URL="http://172.30.0.129:8083"; fi
if [ -n "$1" ]
  then
    git checkout "clients-java-$1"
fi
./mvnw -B -f ./java-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=c8y-connection-sdk
./mvnw -B -f ./java-email-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=c8y-connection-sdk
./mvnw -B -f ./java-sms-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=c8y-connection-sdk
./mvnw -B -f ./java-client-services/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=c8y-connection-sdk
./mvnw -B -f ./microservice/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=c8y-connection-sdk
./mvnw -B -f ./lpwan-backend/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=c8y-connection-sdk
