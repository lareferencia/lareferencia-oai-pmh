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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class DateUtils
{

    private static Logger log = LogManager.getLogger(DateUtils.class);

    public static String format(Date date)
    {
        return format(date, true);
    }
    public static String format(Date date, boolean init)
    {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.'000Z'");
    	if (!init) sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.'999Z'");
        // We indicate that the returned date is in Zulu time (UTC) so we have
        // to set the time zone of sdf correct.
        sdf.setTimeZone(TimeZone.getTimeZone("ZULU"));
        String ret = sdf.format(date);
        return ret;
    }

    public static Date parse(String date)
    {
        // 2008-01-01T00:00:00Z
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        // format.setTimeZone(TimeZone.getTimeZone("ZULU"));
        Date ret;
        try
        {
            ret = format.parse(date);
            return ret;
        }
        catch (ParseException e)
        {
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.getDefault());
            try
            {
                return format.parse(date);
            }
            catch (ParseException e1)
            {
                format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try
                {
                    return format.parse(date);
                }
                catch (ParseException e2)
                {
                    format = new SimpleDateFormat("yyyy-MM",
                            Locale.getDefault());
                    try
                    {
                        return format.parse(date);
                    }
                    catch (ParseException e3)
                    {
                        format = new SimpleDateFormat("yyyy",
                                Locale.getDefault());
                        try
                        {
                            return format.parse(date);
                        }
                        catch (ParseException e4)
                        {
                            log.error(e4.getMessage(), e);
                        }
                    }
                }
            }
        }
        return new Date();
    }

    public static Date parseFromSolrDate(String date)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date ret;
        try
        {
            ret = format.parse(date);
            return ret;
        }
        catch (ParseException e)
        {
            log.error(e.getMessage(), e);
        }
        return new Date();
    }
}
