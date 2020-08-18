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

import com.lyncode.xoai.dataprovider.services.api.ItemRepository;

import org.apache.solr.client.solrj.SolrServerException;

import org.lareferencia.xoai.services.api.cache.XOAIItemCacheService;
import org.lareferencia.xoai.services.api.config.ConfigurationService;

import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;

//import org.lareferencia.xoai.services.api.database.CollectionsService;
//import org.lareferencia.xoai.services.api.database.DatabaseQueryResolver;
//import org.lareferencia.xoai.services.api.database.HandleResolver;

import org.lareferencia.xoai.services.api.solr.SolrQueryResolver;
import org.lareferencia.xoai.services.api.solr.SolrClientResolver;

import org.lareferencia.xoai.services.api.xoai.ItemRepositoryResolver;

import org.springframework.beans.factory.annotation.Autowired;

public class LRItemRepositoryResolver implements ItemRepositoryResolver {
	
    @Autowired
    ContextService contextService;
    @Autowired
    ConfigurationService configurationService;
    @Autowired
    SolrClientResolver solrServerResolver;
    @Autowired
    SolrQueryResolver solrQueryResolver;
    @Autowired
    
//    DatabaseQueryResolver databaseQueryResolver;
//    @Autowired
//   
//    CollectionsService collectionsService;
//    @Autowired
//    
//    private HandleResolver handleResolver;
//    @Autowired
    
    private XOAIItemCacheService cacheService;

    private ItemRepository itemRepository;


    @Override
    public ItemRepository getItemRepository() throws ContextServiceException {
        if (itemRepository == null) {
            //String storage = configurationService.getProperty( "storage");
            //if (storage == null || !storage.trim().toLowerCase().equals("database")) {
                try {
                    itemRepository = new LRItemSolrRepository(solrServerResolver.getClient(), /*, collectionsService, handleResolver, */ solrQueryResolver);
                } catch (SolrServerException e) {
                    throw new ContextServiceException(e.getMessage(), e);
                }
//            } else
//                itemRepository = new DSpaceItemDatabaseRepository(configurationService, collectionsService, handleResolver, cacheService, databaseQueryResolver, contextService);
        }

        return itemRepository;
    }
}
