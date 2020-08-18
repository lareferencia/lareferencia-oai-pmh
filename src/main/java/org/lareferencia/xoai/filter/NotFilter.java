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

import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

public class NotFilter extends LRFilter {
    private LRFilter inFilter;

    public NotFilter(LRFilter inFilter) {
        this.inFilter = inFilter;
    }


//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context) {
//        DatabaseFilterResult result = inFilter.buildDatabaseQuery(context);
//        return new DatabaseFilterResult("NOT ("+result.getQuery()+")", result.getParameters());
//    }

    @Override
    public SolrFilterResult buildSolrQuery() {
        return new SolrFilterResult("NOT("+inFilter.buildSolrQuery()+")");
    }

    @Override
    public boolean isShown(RepostioryItem item) {
        return !inFilter.isShown(item);
    }
}
