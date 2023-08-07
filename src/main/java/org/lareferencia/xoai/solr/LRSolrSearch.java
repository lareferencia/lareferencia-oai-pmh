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

package org.lareferencia.xoai.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.lareferencia.xoai.solr.exceptions.LRSolrException;
import org.lareferencia.xoai.solr.exceptions.SolrSearchEmptyException;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRSolrSearch
{
    public static SolrDocumentList query(SolrClient server, SolrQuery solrParams)
            throws LRSolrException
    {
        try
        {
            solrParams.setSort("item.id", ORDER.asc);
            QueryResponse response = server.query(solrParams);
            return response.getResults();
        }
        catch (SolrServerException | IOException ex)
        {
            throw new LRSolrException(ex.getMessage(), ex);
        }
    }

    public static SolrDocument querySingle(SolrClient server, SolrQuery solrParams)
            throws SolrSearchEmptyException, LRSolrException
    {
        try
        {
            solrParams.setSort("item.id", ORDER.asc);
            QueryResponse response = server.query(solrParams);
            if (response.getResults().getNumFound() > 0)
                return response.getResults().get(0);
            else
                throw new SolrSearchEmptyException();
        }
        catch (SolrServerException | IOException ex)
        {
            throw new LRSolrException(ex.getMessage(), ex);
        }
    }
}
