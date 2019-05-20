#!/bin/bash
source ${BASH_SOURCE%/*}/common.sh
call-mvn clean -q
call-mvn release:clean -q

