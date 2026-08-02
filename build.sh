#!/bin/bash
set -euo pipefail

./mvnw --batch-mode clean install -Dmaven.javadoc.skip=true
