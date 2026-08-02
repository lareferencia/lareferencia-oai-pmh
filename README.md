[![Build](https://github.com/lareferencia/lareferencia-oai-pmh/actions/workflows/maven-build-master.yml/badge.svg)](https://github.com/lareferencia/lareferencia-oai-pmh/actions/workflows/maven-build-master.yml)

# LA Referencia OAI-PMH Provider

Standalone OAI-PMH provider backed by the LA Referencia Solr `oai` core.

## Runtime baseline

- Java 17
- Spring Boot 3.5
- SolrJ 9.5, aligned with LA Referencia Platform
- Solr 9.8 runtime and integration tests

The public OAI identifier remains the repository handle. The modernization does
not add an `oai:` prefix or otherwise rewrite existing identifiers.

## Build and test

Use the included Maven wrapper:

```bash
./mvnw --batch-mode clean test
./mvnw --batch-mode clean package -DskipTests
```

Integration tests start Solr 9.8 with Testcontainers, so a working Docker engine
is required for the complete test suite.

## Configuration

Application defaults are packaged in
`src/main/resources/application.properties`. Spring Boot configuration can be
overridden with environment variables or an external
`config/application.properties` file.

| Property | Environment variable | Default |
| --- | --- | --- |
| `server.port` | `SERVER_PORT` | `8092` |
| `solr.url` | `SOLR_URL` | `http://localhost:8983/solr/oai` |
| `xoai.config.path` | `XOAI_CONFIG_PATH` | `config/xoai.config` |

Use the standard `JAVA_TOOL_OPTIONS` environment variable when JVM flags are
needed in a container.

Repository identity, metadata formats and crosswalks are configured in
`config/xoai.config` and `config/crosswalks/`. Treat these files as part of the
provider compatibility contract when customizing an installation.

## Run the JAR

```bash
SOLR_URL=http://localhost:8983/solr/oai \
  java -jar target/lareferencia-oai-pmh-2.0.1.jar
```

The OAI endpoint is `http://localhost:8092/request` and the health endpoint is
`http://localhost:8092/actuator/health`.

## Run with Docker

Build the provider image:

```bash
./build_docker_image.sh
```

Solr is a separate service. If it is reachable as `solr` on a Docker network:

```bash
docker run --rm --name lareferencia-oai-pmh \
  --network lareferencia \
  --publish 8092:8092 \
  --env SOLR_URL=http://solr:8983/solr/oai \
  lareferencia/oai-pmh:local
```

The image uses Eclipse Temurin 17, runs as the non-root `provider` user and has a
Docker healthcheck backed by Spring Boot Actuator.

## Compatibility and architecture

- [Platform alignment decision](docs/architecture/0001-platform-alignment.md)
- [OAI-PMH compatibility contract](docs/testing/oai-compatibility-contract.md)
