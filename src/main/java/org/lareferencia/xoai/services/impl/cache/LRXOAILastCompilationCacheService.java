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

import org.apache.commons.io.FileUtils;
import org.lareferencia.xoai.ConfigurationManager;
import org.lareferencia.xoai.services.api.cache.XOAILastCompilationCacheService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LRXOAILastCompilationCacheService implements XOAILastCompilationCacheService {

    private static final SimpleDateFormat format = new SimpleDateFormat();
    private static final String DATEFILE = File.separator + "date.file";

    private static File file = null;

    private static File getFile()
    {
        if (file == null)
        {
            String dir = ConfigurationManager.getProperty( "cache.dir") + DATEFILE;
            file = new File(dir);
        }
        return file;
    }


    @Override
    public boolean hasCache() {
        return getFile().exists();
    }





    @Override
    public void put(Date date) throws IOException {
        FileUtils.write(getFile(), format.format(date));
    }





    @Override
    public Date get() throws IOException {
        try {
            return format.parse(FileUtils.readFileToString(getFile()).trim());
        } catch (ParseException e) {
            throw new IOException(e);
        }
    }

}
