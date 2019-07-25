#!/bin/bash
set -e
./mvnw -o clean install -Dskip.microservice.package=false -Dskip.agent.package.container=false -Pci
