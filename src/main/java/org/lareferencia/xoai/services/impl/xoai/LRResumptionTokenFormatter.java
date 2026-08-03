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
package org.lareferencia.xoai.services.impl.xoai;

import com.lyncode.xoai.dataprovider.core.ResumptionToken;
import com.lyncode.xoai.dataprovider.exceptions.BadResumptionToken;
import com.lyncode.xoai.dataprovider.services.api.ResumptionTokenFormatter;

import org.lareferencia.xoai.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class LRResumptionTokenFormatter implements ResumptionTokenFormatter {
    private static final Logger log = LoggerFactory.getLogger(LRResumptionTokenFormatter.class);

    public LRResumptionTokenFormatter() {
    }

    @Override
    public ResumptionToken parse(String resumptionToken) throws BadResumptionToken {
        if (resumptionToken == null) return new ResumptionToken();
        String[] res = resumptionToken.split("/", -1);
        if (res.length != 5) throw new BadResumptionToken();
        else {
            try {
                int offset = Integer.parseInt(res[4]);
                if (offset < 0) throw new BadResumptionToken();
                String prefix = (res[0].equals("")) ? null : res[0];
                String set = (res[3].equals("")) ? null : res[3];
                Date from = (res[1].equals("")) ? null : DateUtils.parse(res[1]);
                Date until = res[2].equals("") ? null : DateUtils.parse(res[2]);
                return new ResumptionToken(offset, prefix, set, from, until);
            } catch (Exception e) {
                log.debug("Rejected malformed OAI resumption token", e);
                throw new BadResumptionToken();
            }
        }
    }


    @Override
    public String format(ResumptionToken resumptionToken) {
        String result = "";
        if (resumptionToken.hasMetadataPrefix())
            result += resumptionToken.getMetadataPrefix();
        result += "/";
        if (resumptionToken.hasFrom())
            result += DateUtils.format(resumptionToken.getFrom());
        result += "/";
        if (resumptionToken.hasUntil())
            result += DateUtils.format(resumptionToken.getUntil());
        result += "/";
        if (resumptionToken.hasSet())
            result += resumptionToken.getSet();
        result += "/";
        result += resumptionToken.getOffset();
        return result;
    }

}
