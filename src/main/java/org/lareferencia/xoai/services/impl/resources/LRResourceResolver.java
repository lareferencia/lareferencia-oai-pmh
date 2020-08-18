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
package org.lareferencia.xoai.services.impl.resources;

import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import org.lareferencia.xoai.ConfigurationManager;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


// TODO: baja prioridad, revisar la pertinencia de este resolver
public class LRResourceResolver implements ResourceResolver {
	
    //private static final TransformerFactory transformerFactory = TransformerFactory.newInstance();
    private static final TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();

    
    private final String basePath = ConfigurationManager.getProperty("config.dir");

    @Override
    public InputStream getResource(String path) throws IOException {
        return new FileInputStream(new File(basePath, path));
    }

    @Override
    public Transformer getTransformer(String path) throws IOException, TransformerConfigurationException {
        return transformerFactory.newTransformer(new StreamSource(getResource(path)));
    }
}
