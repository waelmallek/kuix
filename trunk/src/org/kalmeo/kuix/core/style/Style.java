/*
 * This file is part of org.kalmeo.kuix.
 * 
 * org.kalmeo.kuix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * org.kalmeo.kuix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with org.kalmeo.kuix.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * Creation date : 21 nov. 2007
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.core.style;

import org.kalmeo.util.Filter;
import org.kalmeo.util.LinkedList;
import org.kalmeo.util.LinkedListItem;

/**
 * @author bbeaulant
 */
public class Style implements LinkedListItem {

	class PropertyFilter implements Filter {
		
		public String name;
		
		/* (non-Javadoc)
		 * @see com.kalmeo.util.Filter#accept(java.lang.Object)
		 */
		public int accept(Object obj) {
			return ((StyleProperty) obj).getName().equals(name) ? 1 : 0;
		}
		
	}
	
	// Filter for internal use (Caution it doesn't work if getProperty is call for multiple threads)
	private final PropertyFilter internalFilter = new PropertyFilter();
	
	// Style selector
	private final StyleSelector selector;

	// List of StyleProperty
	private final LinkedList properties;

	// LinkedListItem vars
	private Style parent;
	private Style next;
	
	/**
	 * Construct a {@link Style}
	 * 
	 * @param selector
	 */
	public Style(StyleSelector selector) {
		this.selector = selector;
		this.properties = new LinkedList();
	}

	/* (non-Javadoc)
	 * @see com.kalmeo.util.LinkedListItem#getNext()
	 */
	public LinkedListItem getNext() {
		return next;
	}

	/* (non-Javadoc)
	 * @see com.kalmeo.util.LinkedListItem#getParent()
	 */
	public LinkedListItem getPrevious() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see com.kalmeo.util.LinkedListItem#setNext(com.kalmeo.util.LinkedListItem)
	 */
	public void setNext(LinkedListItem next) {
		this.next = (Style) next;
	}

	/* (non-Javadoc)
	 * @see com.kalmeo.util.LinkedListItem#setParent(com.kalmeo.util.LinkedListItem)
	 */
	public void setPrevious(LinkedListItem parent) {
		this.parent = (Style) parent;
	}

	/**
	 * @return the selector
	 */
	public StyleSelector getSelector() {
		return selector;
	}

	/**
	 * @return the properties
	 */
	public LinkedList getProperties() {
		return properties;
	}

	/**
	 * Returns the {@link StyleProperty} that correspond to the
	 * <code>name</code> or <code>null</code>.
	 * 
	 * @param name
	 * @return The {@link StyleProperty} that correspond to the
	 *         <code>name</code>
	 */
	public StyleProperty getProperty(final String name) {
		internalFilter.name = name;
		Object attribute = properties.find(internalFilter);
		return (StyleProperty) attribute;
	}

	/**
	 * Add a {@link StyleProperty} to this {@link Style}
	 * 
	 * @param styleProperty
	 */
	public void add(StyleProperty styleProperty) {
		properties.add(styleProperty);
	}

	/**
	 * Returns the {@link StyleProperty} corresponding to the <code>name</code>
	 * or <code>null</code> if it does not exist
	 * 
	 * @param name
	 * @return The {@link StyleProperty} corresponding to the <code>name</code>
	 */
	public StyleProperty get(String name) {
		if (properties.getFirst() != null) {
			for (StyleProperty sa = (StyleProperty) properties.getFirst(); sa != null; sa = (StyleProperty) sa.getNext()) {
				if (sa.getName().equals(name)) {
					return sa;
				}
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.LinkedListItem#compareTo(org.kalmeo.util.LinkedListItem, int)
	 */
	public int compareTo(LinkedListItem item, int flag) {
		return 0;
	}

}
