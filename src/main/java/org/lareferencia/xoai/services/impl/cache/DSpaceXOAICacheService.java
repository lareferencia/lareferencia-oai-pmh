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

import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;
import com.lyncode.xoai.util.Base64Utils;

import org.apache.commons.io.FileUtils;
import org.lareferencia.xoai.ConfigurationManager;
import org.lareferencia.xoai.services.api.cache.XOAICacheService;
import org.lareferencia.xoai.services.api.config.ConfigurationService;
import org.lareferencia.xoai.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.stream.XMLStreamException;

import java.io.*;
import java.util.Date;

import static com.lyncode.xoai.dataprovider.core.Granularity.Second;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.apache.commons.io.IOUtils.copy;
import static org.apache.commons.io.IOUtils.write;


public class DSpaceXOAICacheService implements XOAICacheService {
    private static final String REQUEST_DIR = File.separator + "requests";
    private static String baseDir;
    private static String staticHead;

    private static String getBaseDir() {
        if (baseDir == null) {
            String dir = ConfigurationManager.getProperty( "cache.dir") + REQUEST_DIR;
            baseDir = dir;
        }
        return baseDir;
    }

    private static String getStaticHead(XOAIManager manager, Date date) {
        if (staticHead == null)
            staticHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + ((manager.hasStyleSheet()) ? ("<?xml-stylesheet type=\"text/xsl\" href=\""
                    + manager.getStyleSheet() + "\"?>") : "")
                    + "<OAI-PMH xmlns=\"http://www.openarchives.org/OAI/2.0/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                    + "xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd\">";

        return staticHead + "<responseDate>" + DateUtils.format(date) + "</responseDate>";
    }

    @Autowired
    ConfigurationService configurationService;

    private XOAIManager manager;

    public DSpaceXOAICacheService(XOAIManager manager) {
        this.manager = manager;
    }

    private File getCacheFile(String id) {
        File dir = new File(getBaseDir());
        if (!dir.exists())
            dir.mkdirs();

        String name = File.separator + Base64Utils.encode(id);
        return new File(getBaseDir() + name);
    }

    @Override
    public boolean isActive() {
        return configurationService.getBooleanProperty("oai", "cache", true);
    }

    @Override
    public boolean hasCache(String requestID) {
        return this.getCacheFile(requestID).exists();
    }

    @Override
    public void handle(String requestID, OutputStream out) throws IOException {
        InputStream in = new FileInputStream(this.getCacheFile(requestID));
        write(getStaticHead(manager, new Date()), out);
        copy(in, out);
        in.close();
    }

    @Override
    public void store(String requestID, OAIPMH response) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            XmlOutputContext context = XmlOutputContext.emptyContext(output, Second);
            response.write(context);
            context.getWriter().flush();
            context.getWriter().close();

            String xoaiResponse = output.toString();

            // Cutting the header (to allow one to change the response time)
            String end = "</responseDate>";
            int pos = xoaiResponse.indexOf(end);
            if (pos > 0)
                xoaiResponse = xoaiResponse.substring(pos + (end.length()));

            FileUtils.write(this.getCacheFile(requestID), xoaiResponse);
        } catch (XMLStreamException e) {
            throw new IOException(e);
        } catch (WritingXmlException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void delete(String requestID) {
        this.getCacheFile(requestID).delete();
    }

    @Override
    public void deleteAll() throws IOException {
        deleteDirectory(new File(getBaseDir()));
    }

}
