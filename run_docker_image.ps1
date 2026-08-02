$ErrorActionPreference = "Stop"

$solrUrl = if ($env:SOLR_URL) { $env:SOLR_URL } else { "http://host.docker.internal:8983/solr/oai" }

docker run --rm `
  --name lareferencia-oai-pmh `
  --publish 8092:8092 `
  --env "SOLR_URL=$solrUrl" `
  lareferencia/oai-pmh:local
