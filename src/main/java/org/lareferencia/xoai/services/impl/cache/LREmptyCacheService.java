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
package org.lareferencia.xoai.services.impl.cache;

import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;
import org.lareferencia.xoai.services.api.cache.XOAICacheService;

import java.io.IOException;
import java.io.OutputStream;

public class LREmptyCacheService implements XOAICacheService {
    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean hasCache(String requestID) {
        return false;
    }

    @Override
    public void handle(String requestID, OutputStream out) throws IOException {

    }

    @Override
    public void store(String requestID, OAIPMH response) throws IOException {

    }

    @Override
    public void delete(String requestID) {

    }

    @Override
    public void deleteAll() {

    }
}
