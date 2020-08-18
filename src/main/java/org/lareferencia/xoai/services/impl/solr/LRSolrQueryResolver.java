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

import com.lyncode.xoai.dataprovider.filter.Scope;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;
import com.lyncode.xoai.dataprovider.filter.conditions.Condition;
import org.apache.commons.lang.StringUtils;

import org.lareferencia.xoai.services.api.solr.SolrQueryResolver;
import org.lareferencia.xoai.services.api.xoai.LRFilterResolver;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LRSolrQueryResolver implements SolrQueryResolver {
    @Autowired
    LRFilterResolver filterResolver;

    @Override
    public String buildQuery(List<ScopedFilter> filters) {
        List<String> whereCond = new ArrayList<String>();
        for (ScopedFilter filter : filters)
            whereCond.add(buildQuery(filter.getScope(), filter.getCondition()));

        if (whereCond.isEmpty())
            whereCond.add("*:*");
        String where = "(" + StringUtils.join(whereCond.iterator(), ") AND (") + ")";

        return where;
    }


    private String buildQuery (Scope scope, Condition condition) {
        return filterResolver.buildSolrQuery(scope, condition);
    }
}
