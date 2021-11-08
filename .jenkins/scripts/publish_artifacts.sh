#!/bin/bash
set -e
source ${BASH_SOURCE%/*}/common.sh
RELEASE_TYPE=$1
shift 1

export MAVEN_OPTS="-Xmx2048m -XX:MetaspaceSize=1024m ${MAVEN_OPTS}"

call-mvn -s $MVN_SETTINGS deploy -T 2C -Dmaven.install.skip=true $release_args -DskipTests "$@"

if [[ "$RELEASE_TYPE" == "snapshot" ]]; then
    exit 0;
fi

yum_srv=hudson@staging-resources.cumulocity.com

### Publishing to maven ###
echo "Publish cumulocity-sdk/maven-repository/target/maven-repository-${VERSION}.tar.gz to resources tmp "
scp cumulocity-sdk/maven-repository/target/maven-repository-*.tar.gz ${yum_srv}:/tmp/maven-repository-${VERSION}.tar.gz
ssh ${yum_srv} "mkdir -p /tmp/maven-repository-${VERSION} ; tar -xvzf /tmp/maven-repository-${VERSION}.tar.gz -C /tmp/maven-repository-${VERSION}"
echo "Publish extracted files to maven repository"
ssh ${yum_srv} "cp -Rn /tmp/maven-repository-${VERSION}/com/* /var/www/resources/maven/repository/com/ "
ssh ${yum_srv} "cp -Rn /tmp/maven-repository-${VERSION}/c8y/* /var/www/resources/maven/repository/c8y/ "
echo "Cleanup tmp files"
ssh ${yum_srv} "rm -R /tmp/maven-repository-${VERSION}*"

echo "Publishing java-client/target/java-client-${VERSION}-javadoc.jar to resources tmp "
scp java-client/target/java-client-${VERSION}-javadoc.jar ${yum_srv}:/tmp/java-client-${VERSION}-javadoc.jar
ssh ${yum_srv} "mkdir -p /resources/documentation/javasdk/${VERSION} ; unzip -o /tmp/java-client-${VERSION}-javadoc.jar -d /resources/documentation/javasdk/${VERSION}"

if [ "release" == "${RELEASE_TYPE}" ]; then
  echo "Update current symbolic link of javasdk javadocs"
  ssh ${yum_srv} "rm -f /resources/documentation/javasdk/current ; ln -s /resources/documentation/javasdk/${VERSION} /resources/documentation/javasdk/current"
fi

echo "Delete /tmp/java-client-${VERSION}-javadoc.jar file"
ssh ${yum_srv} "rm -f /tmp/java-client-${VERSION}-javadoc.jar"

echo "Publishing microservice/target/microservice-dependencies-${VERSION}-javadoc.jar to resources tmp "
scp microservice/target/microservice-dependencies-${VERSION}-javadoc.jar ${yum_srv}:/tmp/microservice-dependencies-${VERSION}-javadoc.jar
ssh ${yum_srv} "mkdir -p /resources/documentation/microservicesdk/${VERSION} ; unzip -o /tmp/microservice-dependencies-${VERSION}-javadoc.jar -d /resources/documentation/microservicesdk/${VERSION}"

if [ "release" == "${RELEASE_TYPE}" ]; then
  echo "Update current symbolic link of microservicesdk javadocs"
  ssh ${yum_srv} "rm -f /resources/documentation/microservicesdk/current ; ln -s /resources/documentation/microservicesdk/${VERSION} /resources/documentation/microservicesdk/current"
fi

echo "Delete /tmp/microservice-dependencies-${VERSION}-javadoc.jar file"
ssh ${yum_srv} "rm -f /tmp/microservice-dependencies-${VERSION}-javadoc.jar"

echo "Publishing lpwan-backend/target/lpwan-backend-${VERSION}-javadoc.jar to resources tmp "
scp lpwan-backend/target/lpwan-backend-${VERSION}-javadoc.jar ${yum_srv}:/tmp/lpwan-backend-${VERSION}-javadoc.jar
ssh ${yum_srv} "mkdir -p /resources/documentation/lpwan-backend/${VERSION} ; unzip -o /tmp/lpwan-backend-${VERSION}-javadoc.jar -d /resources/documentation/lpwan-backend/${VERSION}"

if [ "release" == "${RELEASE_TYPE}" ]; then
  echo "Update current symbolic link of lpwan-backend javadocs"
  ssh ${yum_srv} "rm -f /resources/documentation/lpwan-backend/current ; ln -s /resources/documentation/lpwan-backend/${VERSION} /resources/documentation/lpwan-backend/current"
fi

echo "Delete /tmp/lpwan-backend-${VERSION}-javadoc.jar file"
ssh ${yum_srv} "rm -f /tmp/lpwan-backend-${VERSION}-javadoc.jar"
