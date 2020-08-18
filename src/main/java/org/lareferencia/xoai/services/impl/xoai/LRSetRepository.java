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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrResponse;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.DSpaceObject;
import org.lareferencia.xoai.Context;
import org.dspace.handle.HandleManager;
import org.dspace.storage.rdbms.DatabaseManager;
import org.dspace.storage.rdbms.TableRow;
import org.dspace.storage.rdbms.TableRowIterator;
import org.lareferencia.xoai.data.RepositorySet;
import org.lareferencia.xoai.services.api.solr.SolrClientResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRSetRepository implements SetRepository
{
    private static final Logger log = LogManager.getLogger(LRSetRepository.class);

    //private final Context _context;
    
    SolrClient solrServer;
    

    public LRSetRepository(SolrClientResolver solrServerResolver)
    {
       // _context = context;
    	try {
			solrServer = solrServerResolver.getClient();
		} catch (SolrServerException e) {
			log.error(e.getMessage(), e);
		}
    	
        
    }
    
    
    private List<FacetField.Count> getFacetValues(String facetFieldName) {
    	
    	
       try {
    	
    	SolrQuery query = new SolrQuery("*:*");
			query.setRows(0);
			query.setFacet(true);
			query.addFacetField(facetFieldName);
			query.setFacetLimit(-1);
			
			QueryResponse response = solrServer.query( query );
					
			return response.getFacetFields().get(0).getValues();
		
       
       } catch (SolrServerException | IOException e) {
			
     	log.error(e.getMessage(), e);  
     	  
			e.printStackTrace();
		}
       
       List<FacetField.Count> list = new ArrayList<FacetField.Count>();
       
       return list;  	
    }

    private int getCommunityCount()
    {  		
        return getFacetValues("item.communities").size();
    }

    private int getCollectionCount()
    {
    	
        return getFacetValues("item.collections").size();
    }

    /**
     * Produce a list of DSpaceCommunitySet.  The list is a segment of the full
     * list of Community ordered by ID.
     *
     * @param offset start this far down the list of Community.
     * @param length return up to this many Sets.
     * @return some Sets representing the Community list segment.
     */
    private List<Set> community(int offset, int length)
    {
        List<Set> array = new ArrayList<Set>();
        
		
  		for (FacetField.Count count: getFacetValues("item.communities") ) {
  		    array.add(RepositorySet.newSet(
  		    		count.getName(),
  		    		count.getName()));     
  		}
                   
        // FIXME: HACER CONSTANTES PARA LOS VALORES DE LAS FACETAS
        return array;
    }

    /**
     * Produce a list of DSpaceCollectionSet.  The list is a segment of the full
     * list of Collection ordered by ID.
     *
     * @param offset start this far down the list of Collection.
     * @param length return up to this many Sets.
     * @return some Sets representing the Collection list segment.
     */
    private List<Set> collection(int offset, int length)
    {
        List<Set> array = new ArrayList<Set>();	
				
		for (FacetField.Count count: getFacetValues("item.collections") ) {
		    array.add(RepositorySet.newSet(
		    		count.getName(),
		    		count.getName()));     
		}
             
        return array;
    }

    @Override
    public ListSetsResult retrieveSets(int offset, int length)
    {
        // Only database sets (virtual sets are added by lyncode common library)
        log.debug("Querying sets. Offset: " + offset + " - Length: " + length);
        List<Set> array = new ArrayList<Set>();
        
        int communityCount = this.getCommunityCount();
        log.debug("Communities: " + communityCount);
        int collectionCount = this.getCollectionCount();
        log.debug("Collections: " + collectionCount);
        
        // todos los sets son comunity + collection
        List<Set> tmp = community(0,-1) ;
        tmp.addAll( collection(0,-1) );
        return new ListSetsResult(offset + length < tmp.size(), tmp.subList(offset, Math.min(offset + length, tmp.size())), tmp.size());


        /**
        if (offset < communityCount)
        {
            if (offset + length > communityCount)
            {
                // Add some collections
                List<Set> tmp = community(offset, length);
                array.addAll(tmp);
                array.addAll(collection(0, length - tmp.size()));
            }
            else
                array.addAll(community(offset, length));
        }
        else if (offset < communityCount + collectionCount)
        {
            array.addAll(collection(offset - communityCount, length));
        }
        log.debug("Has More Results: "
                + ((offset + length < communityCount + collectionCount) ? "Yes"
                        : "No"));
        
        
         
        return new ListSetsResult(offset + length < communityCount
                + collectionCount, array, communityCount + collectionCount);*/
        
        
        
        // FIXME: Hay que considerar la implementación de la paginación de sets
    }

    @Override
    public boolean supportSets()
    {
        return true;
    }

    @Override
    public boolean exists(String setSpec)
    {
    	
    	    boolean found = false;
        	
      		for (FacetField.Count count: getFacetValues("item.collections") ) {		
      			found |= count.getName().equals(setSpec);  
      		}
                    
        	
      		
      		for (FacetField.Count count: getFacetValues("item.communities") ) {
      			found |= count.getName().equals(setSpec);      		  
      		}
                            

        return found;
    }

}
