package org.lareferencia.xoai.services.impl.solr;

import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.services.api.config.ConfigurationService;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LRSolrClientResolverTest {

    @Test
    void usesTheLegacyXoaiConfigurationAsFallback() {
        ConfigurationService configuration = configuration(
                " http://localhost:8080/solr/oai ");
        LRSolrClientResolver resolver = resolver("", configuration);

        assertEquals("http://localhost:8080/solr/oai", resolver.resolveSolrUrl());
    }

    @Test
    void explicitSpringConfigurationTakesPrecedence() {
        ConfigurationService configuration = configuration(null);
        LRSolrClientResolver resolver = resolver(
                " http://solr:8983/solr/oai ", configuration);

        assertEquals("http://solr:8983/solr/oai", resolver.resolveSolrUrl());
    }

    private ConfigurationService configuration(String solrUrl) {
        return new ConfigurationService() {
            @Override
            public String getProperty(String key) {
                return solrUrl;
            }

            @Override
            public boolean getBooleanProperty(
                    String module, String key, boolean defaultValue) {
                return defaultValue;
            }
        };
    }

    private LRSolrClientResolver resolver(
            String springSolrUrl, ConfigurationService configuration) {
        LRSolrClientResolver resolver = new LRSolrClientResolver();
        ReflectionTestUtils.setField(resolver, "solrUrl", springSolrUrl);
        ReflectionTestUtils.setField(resolver, "configurationService", configuration);
        return resolver;
    }
}
