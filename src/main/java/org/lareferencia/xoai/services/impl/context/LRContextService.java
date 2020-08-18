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

import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.services.api.context.ContextService;
import org.lareferencia.xoai.services.api.context.ContextServiceException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.sql.SQLException;

public class LRContextService implements ContextService {
    private static final String OAI_CONTEXT = "OAI_CONTEXT";

    @Override
    public Context getContext() throws ContextServiceException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object value = request.getAttribute(OAI_CONTEXT);
        if (value == null || !(value instanceof Context)) {
            try {
                request.setAttribute(OAI_CONTEXT, new Context());
            } catch (SQLException e) {
                throw new ContextServiceException(e);
            }
        }
        return (Context) request.getAttribute(OAI_CONTEXT);
    }
}
