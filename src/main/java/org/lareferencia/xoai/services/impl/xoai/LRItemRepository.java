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

import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.data.Filter;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.filter.Scope;
import com.lyncode.xoai.dataprovider.filter.ScopedFilter;
import com.lyncode.xoai.dataprovider.filter.conditions.Condition;
import com.lyncode.xoai.dataprovider.services.api.ItemRepository;
import org.lareferencia.xoai.filter.LRSetSpecFilter;
import org.lareferencia.xoai.filter.DateFromFilter;
import org.lareferencia.xoai.filter.DateUntilFilter;

import java.util.Date;
import java.util.List;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public abstract class LRItemRepository implements ItemRepository
{
//    private CollectionsService collectionsService;
//    private HandleResolver handleResolver;

    protected LRItemRepository(/*CollectionsService collectionsService, HandleResolver handleResolver*/) {
//        this.collectionsService = collectionsService;
//        this.handleResolver = handleResolver;
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, Date from) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, String setSpec) throws OAIException
    {
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec), Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
	public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, Date from, Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, String setSpec,
            Date from) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiers(
            List<ScopedFilter> filters, int offset, int length, String setSpec,
            Date from, Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(
            List<ScopedFilter> filters, int offset, int length, Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemIdentifiersResult getItemIdentifiersUntil(
            List<ScopedFilter> filters, int offset, int length, String setSpec,
            Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItemIdentifiers(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, Date from) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, String setSpec) throws OAIException
    {
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, Date from, Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, String setSpec, Date from) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItems(List<ScopedFilter> filters, int offset,
            int length, String setSpec, Date from, Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateFromCondition(from), Scope.Query));
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset,
            int length, Date until) throws OAIException
    {
        filters.add(new ScopedFilter(getDateUntilFilter(until), Scope.Query));
        return this.getItems(filters, offset, length);
    }

    @Override
    public ListItemsResults getItemsUntil(List<ScopedFilter> filters, int offset,
            int length, String setSpec, Date from) throws OAIException
    {
        filters.add(new ScopedFilter(getDateUntilFilter(from), Scope.Query));
        filters.add(new ScopedFilter(getDSpaceSetSpecFilter(setSpec),
                Scope.Query));
        return this.getItems(filters, offset, length);
    }

    private Condition getDateFromCondition(final Date from) {
        return new Condition() {
            @Override
            public Filter getFilter() {
                return new DateFromFilter(from);
            }
        };
    }

    private Condition getDSpaceSetSpecFilter(final String setSpec) {
        return new Condition() {
            @Override
            public Filter getFilter() {
                return new LRSetSpecFilter(setSpec);
            }
        };
    }

    private Condition getDateUntilFilter(final Date until) {
        return new Condition() {
            @Override
            public Filter getFilter() {
                return new DateUntilFilter(until);
            }
        };
    }
}
