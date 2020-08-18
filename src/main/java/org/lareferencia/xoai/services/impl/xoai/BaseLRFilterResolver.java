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

import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.filter.Scope;
import com.lyncode.xoai.dataprovider.filter.conditions.*;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;
import org.apache.log4j.Logger;

import org.lareferencia.xoai.filter.*;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;

import org.lareferencia.xoai.services.api.xoai.LRFilterResolver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.lyncode.xoai.dataprovider.filter.Scope.MetadataFormat;

public class BaseLRFilterResolver implements LRFilterResolver {
    private static final Logger LOGGER = Logger.getLogger(BaseLRFilterResolver.class);


    @Autowired
    ContextService contextService;

//    @Override
//    public String buildDatabaseQuery(Condition condition, List<Object> parameters, Scope scope) throws ContextServiceException {
//        DSpaceFilter filter = getFilter(condition);
//        DatabaseFilterResult result = filter.buildDatabaseQuery(contextService.getContext());
//        if (result.hasResult())
//        {
//            parameters.addAll(result.getParameters());
//            if (scope == MetadataFormat)
//                return "(item.deleted:true OR ("
//                        + result.getQuery() + "))";
//            else
//                return "(" + result.getQuery() + ")";
//        }
//        return "true";
//    }

    public LRFilter getFilter (Condition condition) {
        if (condition instanceof AndCondition) return (LRFilter) getFilter((AndCondition) condition);
        else if (condition instanceof OrCondition) return (LRFilter) getFilter((OrCondition) condition);
        else if (condition instanceof NotCondition) return (LRFilter) getFilter((NotCondition) condition);
        else if (condition instanceof CustomCondition) {
            CustomCondition customCondition = (CustomCondition) condition;
            return (LRFilter) customCondition.getFilter();
        } else {
            return (LRFilter) condition.getFilter();
        }
    }

    @Override
    public String buildSolrQuery(Scope scope, Condition condition) {
    
    	//System.out.println(scope);
    	//System.out.println(condition);
    	
    	LRFilter filter = getFilter(condition);
        SolrFilterResult result = filter.buildSolrQuery();
        if (result.hasResult())
        {
            if (scope == MetadataFormat)
                return "(item.deleted:true OR ("
                        + result.getQuery() + "))";
            else
                return "(" + result.getQuery() + ")";
        }
        return "true";
    }

    @Override
    public Filter getFilter(Class<? extends Filter> filterClass, ParameterMap configuration) {
        if (filterClass.isAssignableFrom(LRAtLeastOneMetadataFilter.class)) {
            return new LRAtLeastOneMetadataFilter(configuration);
        } else if (filterClass.isAssignableFrom(LRAuthorizationFilter.class)) {
            try {
               return new LRAuthorizationFilter(contextService.getContext());
            } catch (ContextServiceException e) {
                LOGGER.error(e.getMessage(), e);
                return null;
            }
        } else if (filterClass.isAssignableFrom(LRMetadataExistsFilter.class)) {
            return new LRMetadataExistsFilter(configuration);
        }
        LOGGER.error("Filter "+filterClass.getName()+" unknown instantiation");
        return null;
    }

    @Override
    public Filter getFilter(AndCondition andCondition) {
        LRFilter leftFilter = this.getFilter(andCondition.getLeft());
        LRFilter rightFilter = this.getFilter(andCondition.getRight());
        return new AndFilter(leftFilter, rightFilter);
    }

    @Override
    public Filter getFilter(OrCondition orCondition) {
        LRFilter leftFilter = this.getFilter(orCondition.getLeft());
        LRFilter rightFilter = this.getFilter(orCondition.getRight());
        return new OrFilter(leftFilter, rightFilter);
    }

    @Override
    public Filter getFilter(NotCondition notCondition) {
        LRFilter filter = this.getFilter(notCondition.getCondition());
        return new NotFilter(filter);
    }
}
