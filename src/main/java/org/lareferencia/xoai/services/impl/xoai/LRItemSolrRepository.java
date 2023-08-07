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

import com.google.common.base.Function;
import com.lyncode.builder.ListBuilder;
import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.data.Item;
import com.lyncode.xoai.dataprovider.data.ItemIdentifier;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.lareferencia.xoai.data.RepositorySolrItem;
import org.lareferencia.xoai.services.api.solr.SolrQueryResolver;
import org.lareferencia.xoai.solr.LRSolrSearch;
import org.lareferencia.xoai.solr.exceptions.LRSolrException;
import org.lareferencia.xoai.solr.exceptions.SolrSearchEmptyException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRItemSolrRepository extends LRItemRepository
{
    private static Logger log = LogManager.getLogger(LRItemSolrRepository.class);
    private SolrClient server;
    private SolrQueryResolver solrQueryResolver;

    public LRItemSolrRepository(SolrClient server, /*CollectionsService collectionsService, HandleResolver handleResolver*/ SolrQueryResolver solrQueryResolver)
    {
        super();
        this.server = server;
        this.solrQueryResolver = solrQueryResolver;
    }

    @Override
    public Item getItem(String identifier) throws IdDoesNotExistException, OAIException {
          if (identifier == null) throw new IdDoesNotExistException();   
		  
          try
		  {
		      SolrQuery params = new SolrQuery("item.handle:\"" + identifier + "\"");
		      return new RepositorySolrItem(LRSolrSearch.querySingle(server, params));
		  }
		  catch (SolrSearchEmptyException ex)
		  {
		      throw new IdDoesNotExistException(ex);
		  }
          catch (LRSolrException ex) {
              throw new OAIException("SOLR is not available" + ex.getMessage());
          }
      }
     
//        String parts[] = identifier.split(Pattern.quote(":"));
//        
//        
//        if (parts.length == 3)
//        {
//            try
//            {
//                SolrQuery params = new SolrQuery("item.handle:" + parts[2]);
//                return new RepositorySolrItem(LRSolrSearch.querySingle(server, params));
//            }
//            catch (SolrSearchEmptyException ex)
//            {
//                throw new IdDoesNotExistException(ex);
//            }
//        }
//        throw new IdDoesNotExistException();
//    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length) throws OAIException
    {
        try
        {
            QueryResult queryResult = retrieveItems(filters, offset, length);
            List<ItemIdentifier> identifierList = new ListBuilder<Item>()
                    .add(queryResult.getResults())
                    .build(new Function<Item, ItemIdentifier>() {
                        @Override
                        public ItemIdentifier apply(Item elem) {
                            return elem;
                        }
                    });
            return new ListItemIdentifiersResult(queryResult.hasMore(), identifierList, queryResult.getTotal());
        }
        catch (LRSolrException ex)
        {
            log.error(ex.getMessage(), ex);
            //return new ListItemIdentifiersResult(false, new ArrayList<ItemIdentifier>());
            throw new OAIException("SOLR is not available" + ex.getMessage());
        }
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset, int length) throws OAIException
    {
        try
        {
            QueryResult queryResult = retrieveItems(filters, offset, length);
            return new ListItemsResults(queryResult.hasMore(), queryResult.getResults(), queryResult.getTotal());
        }
        catch (LRSolrException ex)
        {
            log.debug(ex.getMessage(), ex);
            throw new OAIException("SOLR is not available" + ex.getMessage());
        }
    }

    private QueryResult retrieveItems (List<ScopedFilter> filters, int offset, int length) throws LRSolrException {
        List<Item> list = new ArrayList<Item>();
        SolrQuery params = new SolrQuery(solrQueryResolver.buildQuery(filters))
                .setRows(length)
                .setStart(offset);
        SolrDocumentList solrDocuments = LRSolrSearch.query(server, params);
        for (SolrDocument doc : solrDocuments)
            list.add(new RepositorySolrItem(doc));
        return new QueryResult(list, (solrDocuments.getNumFound() > offset + length), (int) solrDocuments.getNumFound());
    }

    private class QueryResult {
        private List<Item> results;
        private boolean hasMore;
        private int total;

        private QueryResult(List<Item> results, boolean hasMore, int total) {
            this.results = results;
            this.hasMore = hasMore;
            this.total = total;
        }

        private List<Item> getResults() {
            return results;
        }

        private boolean hasMore() {
            return hasMore;
        }

        private int getTotal() {
            return total;
        }
    }

}
