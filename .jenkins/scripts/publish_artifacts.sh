#!/bin/bash -xe

export MAVEN_OPTS="-Xms256m -Xmx512m -XX:MetaspaceSize=96m -XX:MaxMetaspaceSize=128m ${MAVEN_OPTS}"

source ${BASH_SOURCE%/*}/common.sh
RELEASE_TYPE=$1
shift 1

call-mvn -s $MVN_SETTINGS deploy -T 2C -Dmaven.install.skip=true $release_args -DskipTests "$@"

if [[ "$RELEASE_TYPE" == "snapshot" ]]; then
    exit 0;
fi

yum_srv=hudson@staging-resources.cumulocity.com

STAGING_RESOURCES_DIR="/var/www/staging-resources/sag/java-sdk/${VERSION}/"
STAGING_RESOURCES_TARGET="${yum_srv}:${STAGING_RESOURCES_DIR}"

### Publishing to maven ###
echo "Publish cumulocity-sdk/maven-repository/target/maven-repository-${VERSION}.tar.gz to staging resources "
scp cumulocity-sdk/maven-repository/target/maven-repository-${VERSION}.tar.gz ${STAGING_RESOURCES_TARGET}
ssh ${yum_srv} "echo ${VERSION} > ${STAGING_RESOURCES_DIR}:/version.txt"

echo "Publishing java-client/target/java-client-${VERSION}-javadoc.jar to staging resources "
scp java-client/target/java-client-${VERSION}-javadoc.jar ${STAGING_RESOURCES_TARGET}

if [ "release" == "${RELEASE_TYPE}" ]; then
  #echo "Update current symbolic link of javasdk javadocs"
  #ssh ${yum_srv} "rm -f /var/www/staging-resources/documentation/javasdk/current ; ln -s /var/www/staging-resources/documentation/javasdk/${VERSION} /resources/documentation/javasdk/current"
fi

echo "Publishing microservice/target/microservice-dependencies-${VERSION}-javadoc.jar to staging resources "
scp microservice/target/microservice-dependencies-${VERSION}-javadoc.jar ${STAGING_RESOURCES_TARGET}

if [ "release" == "${RELEASE_TYPE}" ]; then
  #echo "Update current symbolic link of microservicesdk javadocs"
  #ssh ${yum_srv} "rm -f /resources/documentation/microservicesdk/current ; ln -s /resources/documentation/microservicesdk/${VERSION} /resources/documentation/microservicesdk/current"
fi

echo "Publishing lpwan-backend/target/lpwan-backend-${VERSION}-javadoc.jar to resources tmp "
scp lpwan-backend/target/lpwan-backend-${VERSION}-javadoc.jar ${STAGING_RESOURCES_TARGET}

if [ "release" == "${RELEASE_TYPE}" ]; then
  #echo "Update current symbolic link of lpwan-backend javadocs"
  #ssh ${yum_srv} "rm -f /resources/documentation/lpwan-backend/current ; ln -s /resources/documentation/lpwan-backend/${VERSION} /resources/documentation/lpwan-backend/current"
fi
