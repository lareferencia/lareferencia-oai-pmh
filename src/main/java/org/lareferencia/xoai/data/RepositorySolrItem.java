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
package org.lareferencia.xoai.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.solr.common.SolrDocument;

import com.lyncode.xoai.dataprovider.core.ItemMetadata;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class RepositorySolrItem extends RepostioryItem
{
    private static Logger log = LogManager
            .getLogger(RepositorySolrItem.class);
    
    private String unparsedMD;
    private ItemMetadata metadata;
    private String handle;
    private Date lastMod;
    private List<ReferenceSet> sets;
    private boolean deleted;
    
    public RepositorySolrItem (SolrDocument doc) {
    	log.debug("Creating OAI Item from Solr source");
        unparsedMD = (String) doc.getFieldValue("item.compile");
        handle = (String) doc.getFieldValue("item.handle");
        lastMod = (Date) doc.getFieldValue("item.lastmodified");
        sets = new ArrayList<ReferenceSet>();
        for (Object obj : doc.getFieldValues("item.communities"))
            sets.add(new ReferenceSet((String) obj));
        for (Object obj : doc.getFieldValues("item.collections"))
            sets.add(new ReferenceSet((String) obj));
        deleted = (Boolean) doc.getFieldValue("item.deleted");
    }
    
    
    public String removeSurrogates(String query) {
    	
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            // !isSurrogate(c) in Java 7
            
            // Character.toCodePoint(high, low); 
            
            if (!( Character.isHighSurrogate(c) || Character.isLowSurrogate(c))  ) {
            
            	sb.append(c);
            } else {
            
            	if ( Character.isHighSurrogate(c) ) {
	            	int codepoint = Character.toCodePoint(c, query.charAt(i+1));
	            	sb.append("&#x" + String.format("%05x", codepoint) + ";");
            	}
            }

        }
        
        System.out.println(sb.toString());
        
        return sb.toString();
    }
    
 
    @Override
    public ItemMetadata getMetadata()
    {
        if (metadata == null) {
            metadata = new ItemMetadata( unparsedMD ); //new ItemMetadata(removeSurrogates( unparsedMD ));
        }
        return metadata;
    }

    @Override
    public Date getDatestamp()
    {
        return lastMod;
    }

    @Override
    public List<ReferenceSet> getSets()
    {
        return sets;
    }

    @Override
    public boolean isDeleted()
    {
        return deleted;
    }

    @Override
    protected String getHandle()
    {
        return handle;
    }

}
