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
package org.lareferencia.xoai.util;

import com.lyncode.xoai.dataprovider.xml.xoai.Element;
import com.lyncode.xoai.dataprovider.xml.xoai.Metadata;
import com.lyncode.xoai.util.Base64Utils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Bundle;
import org.dspace.content.Metadatum;
import org.dspace.content.Item;
import org.dspace.content.authority.Choices;
import org.lareferencia.xoai.ConfigurationManager;
import org.dspace.core.Constants;
import org.dspace.core.Utils;
import org.lareferencia.xoai.data.RepostioryItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
@SuppressWarnings("deprecation")
public class ItemUtils
{
    private static Logger log = LogManager
            .getLogger(ItemUtils.class);

    private static Element getElement(List<Element> list, String name)
    {
        for (Element e : list)
            if (name.equals(e.getName()))
                return e;

        return null;
    }
    private static Element create(String name)
    {
        Element e = new Element();
        e.setName(name);
        return e;
    }

    private static Element.Field createValue(
            String name, String value)
    {
        Element.Field e = new Element.Field();
        e.setValue(value);
        e.setName(name);
        return e;
    }
    public static Metadata retrieveMetadata (Item item) {
        Metadata metadata;
        
        //DSpaceDatabaseItem dspaceItem = new DSpaceDatabaseItem(item);
        
        // read all metadata into Metadata Object
        metadata = new Metadata();
        Metadatum[] vals = item.getMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        for (Metadatum val : vals)
        {
            Element valueElem = null;
            Element schema = getElement(metadata.getElement(), val.schema);
            if (schema == null)
            {
                schema = create(val.schema);
                metadata.getElement().add(schema);
            }
            valueElem = schema;

            // Has element.. with XOAI one could have only schema and value
            if (val.element != null && !val.element.equals(""))
            {
                Element element = getElement(schema.getElement(),
                        val.element);
                if (element == null)
                {
                    element = create(val.element);
                    schema.getElement().add(element);
                }
                valueElem = element;

                // Qualified element?
                if (val.qualifier != null && !val.qualifier.equals(""))
                {
                    Element qualifier = getElement(element.getElement(),
                            val.qualifier);
                    if (qualifier == null)
                    {
                        qualifier = create(val.qualifier);
                        element.getElement().add(qualifier);
                    }
                    valueElem = qualifier;
                }
            }

            // Language?
            if (val.language != null && !val.language.equals(""))
            {
                Element language = getElement(valueElem.getElement(),
                        val.language);
                if (language == null)
                {
                    language = create(val.language);
                    valueElem.getElement().add(language);
                }
                valueElem = language;
            }
            else
            {
                Element language = getElement(valueElem.getElement(),
                        "none");
                if (language == null)
                {
                    language = create("none");
                    valueElem.getElement().add(language);
                }
                valueElem = language;
            }

            valueElem.getField().add(createValue("value", val.value));
            if (val.authority != null) {
                valueElem.getField().add(createValue("authority", val.authority));
                if (val.confidence != Choices.CF_NOVALUE)
                    valueElem.getField().add(createValue("confidence", val.confidence + ""));
            }
        }
        // Done! Metadata has been read!
        // Now adding bitstream info
        Element bundles = create("bundles");
        metadata.getElement().add(bundles);

        Bundle[] bs;
        try
        {
            bs = item.getBundles();
            for (Bundle b : bs)
            {
                Element bundle = create("bundle");
                bundles.getElement().add(bundle);
                bundle.getField()
                        .add(createValue("name", b.getName()));

                Element bitstreams = create("bitstreams");
                bundle.getElement().add(bitstreams);
                Bitstream[] bits = b.getBitstreams();
                for (Bitstream bit : bits)
                {
                    Element bitstream = create("bitstream");
                    bitstreams.getElement().add(bitstream);
                    String url = "";
                    String bsName = bit.getName();
                    String sid = String.valueOf(bit.getSequenceID());
                    String baseUrl = ConfigurationManager.getProperty(
                            "bitstream.baseUrl");
                    String handle = null;
                    // get handle of parent Item of this bitstream, if there
                    // is one:
                    Bundle[] bn = bit.getBundles();
                    if (bn.length > 0)
                    {
                        Item bi[] = bn[0].getItems();
                        if (bi.length > 0)
                        {
                            handle = bi[0].getHandle();
                        }
                    }
                    if (bsName == null)
                    {
                        String ext[] = bit.getFormat().getExtensions();
                        bsName = "bitstream_" + sid
                                + (ext.length > 0 ? ext[0] : "");
                    }
                    if (handle != null && baseUrl != null)
                    {
                        url = baseUrl + "/bitstream/"
                                + handle + "/"
                                + sid + "/"
                                + URLUtils.encode(bsName);
                    }
                    else
                    {
                        url = URLUtils.encode(bsName);
                    }

                    String cks = bit.getChecksum();
                    String cka = bit.getChecksumAlgorithm();
                    String oname = bit.getSource();
                    String name = bit.getName();
                    String description = bit.getDescription();

                    if (name != null)
                        bitstream.getField().add(
                                createValue("name", name));
                    if (oname != null)
                        bitstream.getField().add(
                                createValue("originalName", name));
                    if (description != null)
                        bitstream.getField().add(
                                createValue("description", description));
                    bitstream.getField().add(
                            createValue("format", bit.getFormat()
                                    .getMIMEType()));
                    bitstream.getField().add(
                            createValue("size", "" + bit.getSize()));
                    bitstream.getField().add(createValue("url", url));
                    bitstream.getField().add(
                            createValue("checksum", cks));
                    bitstream.getField().add(
                            createValue("checksumAlgorithm", cka));
                    bitstream.getField().add(
                            createValue("sid", bit.getSequenceID()
                                    + ""));
                }
            }
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }
        

        // Other info
        Element other = create("others");

        other.getField().add(
                createValue("handle", item.getHandle()));
        other.getField().add(
                createValue("identifier", RepostioryItem.buildIdentifier(item.getHandle())));
        other.getField().add(
                createValue("lastModifyDate", item
                        .getLastModified().toString()));
        metadata.getElement().add(other);

        // Repository Info
        Element repository = create("repository");
        repository.getField().add(
                createValue("name",
                        ConfigurationManager.getProperty("dspace.name")));
        repository.getField().add(
                createValue("mail",
                        ConfigurationManager.getProperty("mail.admin")));
        metadata.getElement().add(repository);

        // Licensing info
        Element license = create("license");
        Bundle[] licBundles;
        try
        {
            licBundles = item.getBundles(Constants.LICENSE_BUNDLE_NAME);
            if (licBundles.length > 0)
            {
                Bundle licBundle = licBundles[0];
                Bitstream[] licBits = licBundle.getBitstreams();
                if (licBits.length > 0)
                {
                    Bitstream licBit = licBits[0];
                    InputStream in;
                    try
                    {
                        in = licBit.retrieve();
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        Utils.bufferedCopy(in, out);
                        license.getField().add(
                                createValue("bin",
                                        Base64Utils.encode(out.toString())));
                        metadata.getElement().add(license);
                    }
                    catch (AuthorizeException e)
                    {
                        log.warn(e.getMessage(), e);
                    }
                    catch (IOException e)
                    {
                        log.warn(e.getMessage(), e);
                    }
                    catch (SQLException e)
                    {
                        log.warn(e.getMessage(), e);
                    }

                }
            }
        }
        catch (SQLException e1)
        {
            log.warn(e1.getMessage(), e1);
        }
        
        return metadata;
    }
}
