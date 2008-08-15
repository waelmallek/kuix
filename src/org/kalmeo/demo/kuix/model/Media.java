/*
 * This file is part of org.kalmeo.demo.kuix.
 * 
 * org.kalmeo.demo.kuix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * org.kalmeo.demo.kuix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with org.kalmeo.demo.kuix.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Creation date : 10 mar. 2008
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 */

package org.kalmeo.demo.kuix.model;

import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.util.LinkedListItem;

/**
 * @author omarino
 */
public class Media extends DataProvider {

	public final static int TITLE_FLAG = 0;
	public final static int TYPE_FLAG = 1;
	
	private final static String TITLE_PROPERTY = "sTitle";
	private final static String TYPE_PROPERTY = "vType";
	
	public int type;
	public String title;
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.model.DataProvider#getUserDefinedValue(java.lang.String)
	 */
	protected Object getUserDefinedValue(String property) {
		if (TITLE_PROPERTY.equals(property)) {
			return title;
		}
		if (TYPE_PROPERTY.equals(property)) {
			return Integer.toString(type);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.model.DataProvider#compareTo(org.kalmeo.util.LinkedListItem, int)
	 */
	public int compareTo(LinkedListItem item, int flag) {
		if (item instanceof Media) {
			Media media = (Media) item;
			switch (flag) {
				case TITLE_FLAG :
					return title.compareTo(media.title);
				case TYPE_FLAG :
					return media.type - type;
			}
		}
		return 0;
	}
}
