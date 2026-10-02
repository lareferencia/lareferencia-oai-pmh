package org.lareferencia.xoai.services.impl.xoai;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.services.api.solr.SolrClientResolver;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProviderSolrBehaviorTest {
    @Test
    void identifyUsesTheStoredEarliestDateAndStableEmptyFallback() throws Exception {
        SolrClient client = mock(SolrClient.class);
        QueryResponse response = mock(QueryResponse.class);
        SolrDocumentList documents = new SolrDocumentList();
        SolrDocument document = new SolrDocument();
        Date stored = new Date(123000);
        document.setField("item.lastmodified", stored);
        documents.add(document);
        when(client.query(any(SolrQuery.class))).thenReturn(response);
        when(response.getResults()).thenReturn(documents);
        LRRepositoryConfiguration configuration = new LRRepositoryConfiguration(null, new Context(), client);
        assertEquals(stored, configuration.getEarliestDate());
        documents.clear();
        assertEquals(new Date(0), configuration.getEarliestDate());
    }

    @Test
    void setsDoNotTurnBackendFailureIntoAnEmptySuccess() throws Exception {
        SolrClient client = mock(SolrClient.class);
        SolrClientResolver resolver = () -> client;
        when(client.query(any(SolrQuery.class))).thenThrow(new SolrServerException("offline"));
        LRSetRepository repository = new LRSetRepository(resolver);
        assertThrows(IllegalStateException.class, () -> repository.retrieveSets(0, 10));
    }
}
