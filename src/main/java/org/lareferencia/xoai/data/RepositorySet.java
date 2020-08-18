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
package org.lareferencia.xoai.data;



import com.lyncode.xoai.dataprovider.core.Set;

/**
 * 
 * @author Lyncode Development Team <dspace@lyncode.com>
 */
public class RepositorySet extends Set {
	private static final String DefaultName = "undefined";

	public static String checkName(String name) {
		if (name != null && !name.trim().equals(""))
			return name;
		return DefaultName;
	}

	
	public static Set newSet(String handle, String name) {

		return new Set(handle, checkName(name));
	}
	
	public RepositorySet() {
		
		super("DUMY_SETSPEC","DUMMY_SETNAME");
		// FIXME: ENTIENDO QUE ESTO NO ES USADO FUERA DEL CONTEXTO DSPACE
		
	}

	/*public RepositorySet(Community c) {
		super("com_" + c.getHandle().replace('/', '_'), checkName(c.getName()));
	}

	public RepositorySet(Collection c) {
		super("col_" + c.getHandle().replace('/', '_'), checkName(c.getName()));
	}*/
}
