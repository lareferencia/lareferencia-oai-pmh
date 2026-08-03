#!/bin/bash
set -euo pipefail

# Maven Wrapper hashes its download URL character by character. An invalid
# inherited locale (for example LC_CTYPE=UTF-8 on Amazon Linux) emits one warning
# per character, so keep this build process on the universally available C locale.
export LANG=C
export LC_ALL=C

./mvnw --batch-mode clean package -Dmaven.javadoc.skip=true
