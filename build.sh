#!/bin/bash
set -euo pipefail

./mvnw --batch-mode clean package -Dmaven.javadoc.skip=true
