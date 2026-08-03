FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /workspace

COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw --batch-mode dependency:go-offline

COPY src src
COPY config config
RUN ./mvnw --batch-mode package -DskipTests -Dmaven.javadoc.skip=true


FROM eclipse-temurin:17-jre-jammy

RUN groupadd --system provider \
    && useradd --system --gid provider --home-dir /app provider \
    && apt-get update \
    && apt-get install --yes --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /workspace/target/lareferencia-oai-pmh-*.jar /app/oai-pmh.jar
COPY --chown=provider:provider config /app/config
COPY --chown=provider:provider static /app/static
RUN cp /app/config/application.properties.model /app/config/application.properties \
    && chown provider:provider /app/config/application.properties

ENV JAVA_TOOL_OPTIONS="" \
    SERVER_PORT=8092 \
    SOLR_URL=http://solr:8983/solr/oai \
    XOAI_CONFIG_PATH=/app/config/application.properties

EXPOSE 8092

USER provider

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
    CMD curl --fail --silent "http://127.0.0.1:${SERVER_PORT}/actuator/health" || exit 1

ENTRYPOINT ["java", "-jar", "/app/oai-pmh.jar"]
