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
package org.lareferencia.xoai.services.impl.database;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class MetadataFieldCache
{
    private static Logger log = LogManager
            .getLogger(MetadataFieldCache.class);

    private Map<String, Integer> fields;

    public MetadataFieldCache()
    {
        fields = new HashMap<String, Integer>();
    }

    public boolean hasField(String field)
    {
        return fields.containsKey(field);
    }

    public int getField(String field)
    {
        return fields.get(field).intValue();
    }

    public void add(String field, int id)
    {
        fields.put(field, new Integer(id));
    }
}
