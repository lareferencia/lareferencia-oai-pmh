package org.lareferencia.xoai.support;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.nio.file.Paths;
import java.time.Duration;

public final class SharedSolrContainer {

    private static final GenericContainer<?> INSTANCE = createAndStart();

    private SharedSolrContainer() {
    }

    public static GenericContainer<?> getInstance() {
        return INSTANCE;
    }

    public static String getSolrUrl() {
        return "http://" + INSTANCE.getHost() + ":" + INSTANCE.getMappedPort(8983)
                + "/solr/oai";
    }

    private static GenericContainer<?> createAndStart() {
        GenericContainer<?> container = new GenericContainer<>(DockerImageName.parse("solr:9.8.0"))
                .withExposedPorts(8983)
                .withCopyFileToContainer(
                        MountableFile.forHostPath(Paths.get("solr.core/oai/conf").toAbsolutePath()),
                        "/opt/oai-config")
                .withCommand("solr-precreate", "oai", "/opt/oai-config")
                .waitingFor(Wait.forHttp("/solr/oai/select?q=*:*&rows=0&wt=json")
                        .forStatusCode(200))
                .withStartupTimeout(Duration.ofSeconds(120));
        container.start();
        return container;
    }
}
