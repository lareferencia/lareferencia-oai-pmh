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
package org.lareferencia.xoai.filter.results;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DatabaseFilterResult
{
    private String where;
    private List<Object> parameters;
    private boolean nothing;

    public DatabaseFilterResult()
    {
        nothing = true;
    }

    public DatabaseFilterResult(String where, Object... params)
    {
        nothing = false;
        this.where = where;
        parameters = new ArrayList<Object>();
        for (Object obj : params)
            parameters.add(obj);
    }

    public DatabaseFilterResult(String where, List<Object> params)
    {
        nothing = false;
        this.where = where;
        parameters = params;
    }

    public boolean hasResult()
    {
        return !nothing;
    }

    public String getQuery()
    {
        return where;
    }

    public List<Object> getParameters()
    {
        return parameters;
    }

}
