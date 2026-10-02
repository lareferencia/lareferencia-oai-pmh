/*******************************************************************************
 * Copyright (c) 2013, 2019 LA Referencia / Red CLARA and others
 *
 * This file is part of LRHarvester v4.x software
 *
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *     
 *     For any further information please contact
 *     Lautaro Matas <lmatas@gmail.com>
 *******************************************************************************/
package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.services.api.SetRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.lareferencia.xoai.data.RepositorySet;
import org.lareferencia.xoai.services.api.solr.SolrClientResolver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/** Solr sets, loaded once per repository instance (one HTTP request). */
public class LRSetRepository implements SetRepository {
    private final SolrClient solrServer;
    private List<Set> sets;

    public LRSetRepository(SolrClientResolver resolver) {
        try {
            solrServer = resolver.getClient();
        } catch (SolrServerException e) {
            throw new IllegalStateException("Unable to initialize Solr set repository", e);
        }
    }

    private List<Set> getSets() {
        if (sets == null) {
            SolrQuery query = new SolrQuery("item.public:true");
            query.setRows(0);
            query.setFacet(true);
            query.addFacetField("item.communities", "item.collections");
            query.setFacetLimit(-1);
            query.setFacetMinCount(1);
            query.setFacetSort("index");
            try {
                QueryResponse response = solrServer.query(query);
                java.util.Set<String> names = new TreeSet<>();
                if (response.getFacetFields() != null) {
                    for (FacetField facet : response.getFacetFields()) {
                        if (facet.getValues() != null) {
                            for (FacetField.Count count : facet.getValues()) names.add(count.getName());
                        }
                    }
                }
                sets = new ArrayList<>();
                for (String name : names) sets.add(RepositorySet.newSet(name, name));
            } catch (SolrServerException | IOException e) {
                throw new IllegalStateException("Unable to retrieve OAI sets from Solr", e);
            }
        }
        return sets;
    }

    @Override
    public ListSetsResult retrieveSets(int offset, int length) {
        List<Set> all = getSets();
        if (offset < 0 || length <= 0 || offset >= all.size()) {
            return new ListSetsResult(false, new ArrayList<>(), all.size());
        }
        int end = (int) Math.min((long) offset + length, all.size());
        return new ListSetsResult(end < all.size(), all.subList(offset, end), all.size());
    }

    @Override
    public boolean supportSets() { return true; }

    @Override
    public boolean exists(String setSpec) {
        return getSets().stream().anyMatch(set -> setSpec.equals(set.getSetSpec()));
    }
}
