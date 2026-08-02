# ADR 0001: Align the OAI-PMH provider with LA Referencia Platform

- Status: Accepted
- Date: 2026-08-02

## Context

The OAI-PMH provider is currently a standalone Spring Boot 2.3 application built
with Java 8-era dependencies. LA Referencia Platform 5.0 uses Spring Boot 3.5,
Java 17 and Jakarta APIs. Platform currently uses Solr 9.5.0 in Maven and its
integration tests, while its runtime image uses Solr 9.8.0.

The provider must retain an independent release history while being buildable,
testable and deployable from the Platform workspace.

## Decision

The target compatibility baseline is:

| Component | Target |
| --- | --- |
| Java source and runtime | 17 |
| Additional CI runtime | 21 |
| Spring Boot | 3.5.x, matching Platform |
| Servlet and related APIs | Jakarta |
| SolrJ | 9.5.0, matching Platform |
| Solr runtime and Testcontainers image | 9.8.0 |
| Tests | JUnit 5, REST Assured and Testcontainers |
| Logging | SLF4J with Spring Boot-managed Logback |
| Runtime image | Eclipse Temurin 17 JRE, layered and non-root |
| Configuration | External properties and environment variables |

The provider remains a separate Git repository. Platform will pin it in its
workspace manifest and include it in its Maven build and Docker Compose runtime.
A shared, published LA Referencia build parent is preferred so that the provider
also remains buildable outside the Platform checkout.

The canonical OAI Solr core will be `lareferencia-solr-cores/oai`. The provider's
copy will only be removed after the canonical core passes the provider integration
suite and a clean reindex test.

## Compatibility constraints

- Existing OAI identifiers remain unchanged. The current handle value continues
  to be emitted and accepted by `GetRecord`, `ListIdentifiers` and `ListRecords`.
- Modernization must not change datestamps solely because of an internal code or
  dependency upgrade.
- Deleted-record behavior, set membership, metadata formats and resumption-token
  pagination remain compatible with the current provider.
- Private records must not become harvestable.
- Solr failures must become explicit service failures rather than empty result
  sets, but protocol errors must retain their OAI-PMH representation.

## Delivery order

1. Capture the current behavior with regression tests and protocol fixtures.
2. Remove unused DSpace coupling.
3. Modernize or replace the XOAI dependency without changing protocol output.
4. Migrate the application to Spring Boot 3.5 and Jakarta.
5. Align Solr and adopt the canonical Platform core.
6. Add Platform packaging, runtime integration and observability.

Each step must leave the project buildable and is delivered as a separate,
bisectable commit or coordinated pull request.

## Implementation status

The provider now builds with Java 17 bytecode on Spring Boot 3.5.0 and Jakarta
Servlet APIs. CI also exercises Java 21. SolrJ 9.5.0 matches Platform, while the
provider integration suite runs against Solr 9.8.0 using a core upgraded to
Lucene 9.8 and point-based numeric and date fields.

XOAI 3.2.10 remains temporarily isolated behind its required JAXB 2.3 runtime
and a Log4j-to-SLF4J bridge. The provider no longer depends on DSpace 5.1; its
request context and filters now contain only provider-owned behavior. Replacing
or modernizing XOAI, producing the runtime image and integrating the provider
into Platform remain separate follow-up phases. Platform has not been modified
during this phase.

## Consequences

The migration deliberately favors protocol compatibility over internal API
compatibility. The XOAI modernization and the Solr core validation are explicit
gates because they carry the greatest risk. Platform's Maven, container and test
Solr versions must be unified before the final integration pull request.
