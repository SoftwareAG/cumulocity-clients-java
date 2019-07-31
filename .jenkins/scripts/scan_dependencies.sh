#!/usr/bin/env bash
./mvnw -f ./java-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
./mvnw -f ./java-email-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
./mvnw -f ./java-sms-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
./mvnw -f ./java-client-services/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk