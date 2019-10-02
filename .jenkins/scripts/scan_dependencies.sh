#!/usr/bin/env bash
+if [ -z "$TPP_FETCHER_URL" ]; then TPP_FETCHER_URL="http://172.30.0.129:8083"; fi
./mvnw -f ./java-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
./mvnw -f ./java-email-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
./mvnw -f ./java-sms-client/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
./mvnw -f ./java-client-services/pom.xml com.nsn.cumulocity.dependencies:3rd-license-maven-plugin:3rd-tpp-fetcher-scan -Dtpp.fetcher.url=$TPP_FETCHER_URL -Dtpp.fetcher.project.name=java-sdk
