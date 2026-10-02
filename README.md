[![Build](https://github.com/lareferencia/lareferencia-oai-pmh/actions/workflows/maven-build-master.yml/badge.svg)](https://github.com/lareferencia/lareferencia-oai-pmh/actions/workflows/maven-build-master.yml)

# LA Referencia OAI-PMH Provider

Standalone OAI-PMH provider backed by the LA Referencia Solr `oai` core.

The Maven artifact is `org.lareferencia:lareferencia-oai-pmh:5.0.0-rc2`, aligned
with the platform release. The provider uses Spring Boot directly as its Maven
parent so its standalone build and Docker image do not inherit platform-wide
dependencies or require the platform parent to be published.

## Runtime baseline

- Java 17
- Spring Boot 3.5
- XOAI 3.4
- SolrJ 9.5, aligned with LA Referencia Platform
- Solr 9.8 runtime and integration tests

The public OAI identifier remains the repository handle. The modernization does
not add an `oai:` prefix or otherwise rewrite existing identifiers.

## Build and test

Use the included Maven wrapper:

```bash
./mvnw --batch-mode clean test
./mvnw --batch-mode clean verify
./mvnw --batch-mode clean package -DskipTests
```

`test` and `build.sh` run the unit suite and do not require Docker. `verify` also
runs the `*IntegrationTest` classes, which start Solr 9.8 with Testcontainers and
therefore require a working Docker engine.

## Configuration

Application defaults are packaged in
`src/main/resources/application.properties`. Spring Boot configuration can be
overridden with environment variables or a local `config/application.properties`
created from the versioned model:

```bash
cp config/application.properties.model config/application.properties
```

The generated file is intentionally ignored by Git so deployment-specific
values remain local.

| Property | Environment variable | Default |
| --- | --- | --- |
| `server.port` | `SERVER_PORT` | `8092` |
| `solr.url` | `SOLR_URL` | `http://localhost:8080/solr/oai` |
| `xoai.config.path` | `XOAI_CONFIG_PATH` | Model, or `config/application.properties` after setup |

Use the standard `JAVA_TOOL_OPTIONS` environment variable when JVM flags are
needed in a container.

Spring Boot settings, repository identity and XOAI paths share the generated
`config/application.properties` file. Its versioned source is
`config/application.properties.model`. Metadata formats and crosswalks remain in
`config/crosswalks/`. `SOLR_URL` and other Spring environment variables override
the corresponding file values. Treat this configuration as part of the provider
compatibility contract when customizing an installation.

## Run the JAR

```bash
SOLR_URL=http://localhost:8983/solr/oai \
  java -jar target/lareferencia-oai-pmh-5.0.0-rc2.jar
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
