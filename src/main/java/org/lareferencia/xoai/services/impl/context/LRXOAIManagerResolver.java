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
package org.lareferencia.xoai.services.impl.context;

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;

import org.lareferencia.xoai.services.api.config.XOAIManagerResolver;
import org.lareferencia.xoai.services.api.config.XOAIManagerResolverException;

import org.lareferencia.xoai.services.api.xoai.LRFilterResolver;

import org.springframework.beans.factory.annotation.Autowired;

import static com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration.readConfiguration;

public class LRXOAIManagerResolver implements XOAIManagerResolver {
    public static final String XOAI_CONFIGURATION_FILE = "xoai.xml";
    @Autowired ResourceResolver resourceResolver;
    @Autowired LRFilterResolver filterResolver;

    private XOAIManager manager;

    @Override
    public XOAIManager getManager() throws XOAIManagerResolverException {
        if (manager == null) {
            try {
                manager = new XOAIManager(filterResolver, resourceResolver, readConfiguration(resourceResolver.getResource(XOAI_CONFIGURATION_FILE)));
            } catch (Exception e) {
                throw new XOAIManagerResolverException(e);
            }
        }
        return manager;
    }
}
