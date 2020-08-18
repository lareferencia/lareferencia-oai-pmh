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

import com.lyncode.builder.DateBuilder;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import java.util.Date;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DateFromFilter extends LRFilter {
    private static DateProvider dateProvider = new BaseDateProvider();
    private Date date;

    public DateFromFilter(Date date)
    {
        this.date = new DateBuilder(date).setMinMilliseconds().build();
    }

//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context)
//    {
//        return new DatabaseFilterResult("i.last_modified >= ?",
//                new java.sql.Date(date.getTime()));
//    }

    @Override
    public boolean isShown(RepostioryItem item)
    {
        if (item.getDatestamp().compareTo(date) >= 0)
            return true;
        return false;
    }

    @Override
    public SolrFilterResult buildSolrQuery()
    {
        String format = dateProvider.format(date).replace("Z", ".000Z"); // Tweak to set the milliseconds
        return new SolrFilterResult("item.lastmodified:["
                + ClientUtils.escapeQueryChars(format)
                + " TO *]");
    }

}
