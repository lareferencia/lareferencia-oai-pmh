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

package org.lareferencia.xoai.filter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.Bundle;
import org.dspace.content.Item;
import org.dspace.core.Constants;
import org.lareferencia.xoai.Context;
import org.dspace.handle.HandleManager;
import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRAuthorizationFilter extends LRFilter
{
    private static Logger log = LogManager.getLogger(LRAuthorizationFilter.class);
    private Context context;

    public LRAuthorizationFilter (Context context) {
        this.context = context;
    }

//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context)
//    {
//        List<Object> params = new ArrayList<Object>();
//        return new DatabaseFilterResult("EXISTS (SELECT p.action_id FROM "
//                + "resourcepolicy p, " + "bundle2bitstream b, " + "bundle bu, "
//                + "item2bundle ib " + "WHERE " + "p.resource_type_id=0 AND "
//                + "p.resource_id=b.bitstream_id AND "
//                + "p.epersongroup_id=0 AND " + "b.bundle_id=ib.bundle_id AND "
//                + "bu.bundle_id=b.bundle_id AND " + "bu.name='ORIGINAL' AND "
//                + "ib.item_id=i.item_id)", params);
//    }

    @Override
    public boolean isShown(RepostioryItem item)
    {
        /*try
        {
            String handle = DSpaceItem.parseHandle(item.getIdentifier());
            if (handle == null) return false;
            Item dspaceItem = (Item) HandleManager.resolveToObject(context, handle);
            AuthorizeManager.authorizeAction(context, dspaceItem, Constants.READ);
            for (Bundle b : dspaceItem.getBundles())
                AuthorizeManager.authorizeAction(context, b, Constants.READ);
            return true;
        }
        catch (AuthorizeException ex)
        {
            log.error(ex.getMessage(), ex);
        }
        catch (SQLException ex)
        {
            log.error(ex.getMessage(), ex);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return false;*/
    	
    	// TODO: Dummy return
    	return true;
    }

    @Override
    public SolrFilterResult buildSolrQuery()
    {
        return new SolrFilterResult("item.public:true");
    }

}
