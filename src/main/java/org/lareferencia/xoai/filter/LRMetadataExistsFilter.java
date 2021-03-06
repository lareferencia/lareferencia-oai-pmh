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
package org.lareferencia.xoai.filter;

import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterMap;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.ParameterValue;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.parameters.SimpleType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.core.Constants;
import org.lareferencia.xoai.Context;
import org.lareferencia.xoai.data.RepostioryItem;
import org.lareferencia.xoai.exceptions.InvalidMetadataFieldException;
import org.lareferencia.xoai.filter.results.DatabaseFilterResult;
import org.lareferencia.xoai.filter.results.SolrFilterResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This filter allows one to retrieve (from the data source) those items
 * which contains at least one metadata field value defined, it allows
 * one to define multiple metadata fields to check against.
 * <p/>
 * One line summary: At least one metadata field defined
 *
 * @author Ariel J. Lira <arieljlira@gmail.com>
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class LRMetadataExistsFilter extends LRFilter {
    private static Logger log = LogManager
            .getLogger(LRMetadataExistsFilter.class);

    private List<String> fields;
    private ParameterMap configuration;

    public LRMetadataExistsFilter(ParameterMap configuration) {
        this.configuration = configuration;
    }

    private List<String> getFields() {
        if (this.fields == null) {
            ParameterValue fields = getConfiguration().get("fields");
            if (fields == null) fields = getConfiguration().get("field");

            if (fields instanceof SimpleType) {
                this.fields = new ArrayList<String>();
                this.fields.add(((SimpleType) fields).asString());
            } else {
                this.fields = new ArrayList<String>();
                for (ParameterValue val : fields.asParameterList().getValues())
                    this.fields.add(val.asSimpleType().asString());
            }

        }
        return fields;
    }

//    @Override
//    public DatabaseFilterResult buildDatabaseQuery(Context context) {
//        try {
//            List<String> fields = this.getFields();
//            StringBuilder where = new StringBuilder();
//            List<Object> args = new ArrayList<Object>(fields.size());
//            where.append("(");
//            for (int i = 0; i < fields.size(); i++) {
//                where.append("EXISTS (SELECT tmp.* FROM metadatavalue tmp WHERE tmp.resource_id=i.item_id AND tmp.resource_type_id=" + Constants.ITEM+ " AND tmp.metadata_field_id=?)");
//                args.add(fieldResolver.getFieldID(context, fields.get(i)));
//
//                if (i < fields.size() - 1)
//                    where.append(" OR ");
//            }
//            where.append(")");
//
//            return new DatabaseFilterResult(where.toString(), args);
//        } catch (InvalidMetadataFieldException e) {
//            log.error(e.getMessage(), e);
//        } catch (SQLException e) {
//            log.error(e.getMessage(), e);
//        }
//        return new DatabaseFilterResult();
//    }

    @Override
    public boolean isShown(RepostioryItem item) {
        for (String field : this.getFields()) {
            //do we have a match? if yes, our job is done
            if (item.getMetadata(field).size() > 0)
                return true;
        }
        return false;
    }

    @Override
    public SolrFilterResult buildSolrQuery() {
        StringBuilder cond = new StringBuilder("(");
        List<String> fields = this.getFields();
        for (int i = 0; i < fields.size(); i++) {
            cond.append("metadata.").append(fields.get(i)).append(":[* TO *]");
            if (i < fields.size() - 1)
                cond.append(" OR ");
        }
        cond.append(")");

        return new SolrFilterResult(cond.toString());
    }

    public ParameterMap getConfiguration() {
        return configuration;
    }
}
