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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.lareferencia.xoai.ConfigurationManager;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRSolrClient
{
    private static Logger log = LogManager.getLogger(LRSolrClient.class);

    private static SolrClient _server = null;

    public static SolrClient getServer() throws SolrServerException
    {
        if (_server == null)
        {
            try
            {

                log.info("Connecting to Solr Server" + ConfigurationManager.getProperty("solr.url") + " ...");
                _server = new HttpSolrClient.Builder( ConfigurationManager.getProperty("solr.url") )
            		    .withConnectionTimeout(60000)
            		    .withSocketTimeout(60000)
            		    .build(); 
                		
                log.debug("Solr Server Initialized");
            }            
            catch (Exception e)
            {
                log.debug(e.getMessage(), e);
                throw new SolrServerException(e.getMessage(), e);
            }
        }
        return _server;
    }
}
