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
package org.lareferencia.xoai.services.impl.solr;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import org.lareferencia.xoai.services.api.config.ConfigurationService;
import org.lareferencia.xoai.services.api.solr.SolrClientResolver;

import org.springframework.beans.factory.annotation.Autowired;

public class LRSolrClientResolver implements SolrClientResolver {
    private static Logger log = LogManager.getLogger(LRSolrClientResolver.class);
    //private static SolrServer server = null;
    private static SolrClient server;
    
    
    @Autowired
    private ConfigurationService configurationService;

    @Override
    public SolrClient getClient() throws SolrServerException
    {
        if (server == null)
        {
            try
            {
            	server = new HttpSolrClient.Builder( configurationService.getProperty("solr.url") )
            		    .withConnectionTimeout(10000)
            		    .withSocketTimeout(60000)
            		    .build();
            	
                //server = new HttpSolrServer(configurationService.getProperty( "solr.url"));
                log.debug("Solr Server Initialized");
            }
            catch (Exception e)
            {
                log.error(e.getMessage(), e);
                throw new SolrServerException(e.getMessage(), e);
            }
        }
        return server;
    }
}
