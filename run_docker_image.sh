#!/usr/bin/env bash
set -euo pipefail

docker run --rm \
  --name lareferencia-oai-pmh \
  --publish 8092:8092 \
  --env "SOLR_URL=${SOLR_URL:-http://host.docker.internal:8983/solr/oai}" \
  lareferencia/oai-pmh:local
