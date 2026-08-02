package org.lareferencia.xoai.filter;

import org.junit.jupiter.api.Test;
import org.lareferencia.xoai.util.DateUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OaiSolrFilterTest {

    @Test
    void appliesPublicAccessFiltering() {
        assertEquals("item.public:true",
                new LRAuthorizationFilter(null).buildSolrQuery().getQuery());
    }

    @Test
    void buildsAnInclusiveFromRange() {
        assertEquals("item.lastmodified:[2026\\-08\\-02T10\\:15\\:30.000Z TO *]",
                new DateFromFilter(DateUtils.parse("2026-08-02T10:15:30Z"))
                        .buildSolrQuery().getQuery());
    }

    @Test
    void buildsAnInclusiveUntilRange() {
        assertEquals("item.lastmodified:[* TO 2026\\-08\\-02T10\\:15\\:30.999Z]",
                new DateUntilFilter(DateUtils.parse("2026-08-02T10:15:30Z"))
                        .buildSolrQuery().getQuery());
    }
}
