#!/usr/bin/env bash
set -euo pipefail
script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$script_dir"
parent_context="${PLATFORM_PARENT_CONTEXT:-..}"
[[ -f "$parent_context/pom.xml" ]] || {
  echo "Set PLATFORM_PARENT_CONTEXT to the LA Referencia Platform checkout." >&2
  exit 1
}
docker build --build-context "platform-parent=$parent_context" --tag lareferencia/oai-pmh:local .
