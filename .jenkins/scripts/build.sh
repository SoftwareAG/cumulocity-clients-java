#!/bin/bash
set -e
# source ${BASH_SOURCE%/*}/common.sh

./mvnw install -Pci,javadoc -s $MVN_SETTINGS -U $1
cd microservice
../mvnw install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS -U $1
cd -
cd lpwan-backend
<<<<<<< HEAD
../mvnw install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS -U $1
cd -
=======
../mvnw install javadoc:aggregate-jar -Pci -s $MVN_SETTINGS -U
cd -

>>>>>>> bitbucket/develop
