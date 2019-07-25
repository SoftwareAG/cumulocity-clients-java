#!/bin/bash
set +eu
[[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh"
set -e
./mvnw -o install -Dmaven.repo.local=.m2/repository
