package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.support.SharedSolrContainer;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class LRItemSolrRepositoryIntegrationTest {

    private static SolrClient solrClient;
    private static LRItemSolrRepository repository;

    @BeforeAll
    static void indexFixtures() throws Exception {
        String solrUrl = SharedSolrContainer.getSolrUrl();
        solrClient = new HttpSolrClient.Builder(solrUrl).build();
        repository = new LRItemSolrRepository(solrClient, filters -> "item.public:true");

        solrClient.add(item(1, "20.500.12345/public+record", true));
        solrClient.add(item(2, "20.500.12345/private", false));
        solrClient.commit();
    }

    @AfterAll
    static void closeClient() throws Exception {
        if (solrClient != null) {
            solrClient.close();
        }
    }

    @Test
    void queriesSolr98WithTheCurrentEscapedHandleIdentifier() throws Exception {
        assertEquals("20.500.12345/public+record",
                repository.getItem("20.500.12345/public+record").getIdentifier());
    }

    @Test
    void appliesTheDefaultPublicFilterWhenListingRecords() throws Exception {
        ListItemsResults results = repository.getItems(Collections.emptyList(), 0, 10);

        assertEquals(1, results.getTotal());
        assertEquals("20.500.12345/public+record",
                results.getResults().get(0).getIdentifier());
        assertFalse(results.hasMore());
    }

    private static SolrInputDocument item(int id, String handle, boolean isPublic) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("item.id", id);
        document.addField("item.handle", handle);
        document.addField("item.public", isPublic);
        document.addField("item.deleted", false);
        document.addField("item.lastmodified", Instant.parse("2026-08-02T10:15:30Z").toString());
        document.addField("item.communities", "community:test");
        document.addField("item.collections", "collection:test");
        document.addField("item.compile", "<metadata/>");
        return document;
    }
}
