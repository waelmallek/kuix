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
 * Creation date : 7 févr. 08
 * Copyright (c) Kalmeo 2008. All rights reserved.
 */

package org.kalmeo.kuix.core.model;

import java.util.Hashtable;
import java.util.Vector;

import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.Filter;
import org.kalmeo.util.LinkedList;
import org.kalmeo.util.LinkedListItem;

/**
 * This class represent the base object of the Kuix data model. 
 * 
 * @author bbeaulant
 */
public class DataProvider implements LinkedListItem {

	// Update event types
	public static final byte ADD_MODEL_UPDATE_EVENT_TYPE 			= 1;
	public static final byte ADD_BEFORE_MODEL_UPDATE_EVENT_TYPE 	= 2;
	public static final byte ADD_AFTER_MODEL_UPDATE_EVENT_TYPE 		= 3;
	public static final byte REMOVE_MODEL_UPDATE_EVENT_TYPE 		= 4;
	public static final byte SORT_MODEL_UPDATE_EVENT_TYPE 			= 5;
	public static final byte CLEAR_MODEL_UPDATE_EVENT_TYPE 			= 6;

	// Hashtable of property / itemValues pair
	private Hashtable itemsValues;

	// List of binded widgets
	private Vector bindedWidgets;

	// LinkedListItem properties
	private DataProvider previous;
	private DataProvider next;

	/**
	 * Returns the value corresponding the given <code>property</code>.
	 * Override the method to returns your custums values.<br/> Be careful to
	 * always call the <code>super.getValue(property)</code>.
	 * 
	 * @param property
	 * @return the <code>property</code> value
	 */
	public Object getValue(String property) {
		if (itemsValues != null) {
			return itemsValues.get(property);
		}
		return null;
	}

	/**
	 * @param property
	 * @return the <code>property</code> string value
	 */
	public String getStringValue(String property) {
		try {
			return (String) getValue(property);
		} catch (Exception e) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.LinkedListItem#getNext()
	 */
	public LinkedListItem getNext() {
		return next;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.LinkedListItem#getPrevious()
	 */
	public LinkedListItem getPrevious() {
		return previous;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.LinkedListItem#setNext(org.kalmeo.util.LinkedListItem)
	 */
	public void setNext(LinkedListItem next) {
		this.next = (DataProvider) next;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.LinkedListItem#setPrevious(org.kalmeo.util.LinkedListItem)
	 */
	public void setPrevious(LinkedListItem previous) {
		this.previous = (DataProvider) previous;
	}
	
	/**
	 * Returns the {@link LinkedList} relative to the given
	 * <code>property</code>. If the list doesn't exist, a new one is created
	 * and added to itemsValues.
	 * 
	 * @param property
	 * @return the {@link LinkedList} relative to the given
	 *         <code>property</code>
	 */
	private LinkedList getOrCreateItemValue(String property) {
		if (itemsValues == null) {
			itemsValues = new Hashtable();
		}
		LinkedList items;
		if (itemsValues.containsKey(property)) {
			Object tmpItems = itemsValues.get(property);
			if (tmpItems instanceof LinkedList) {
				items = (LinkedList) tmpItems;
			} else {
				return null;
			}
		} else {
			items = new LinkedList();
			itemsValues.put(property, items);
		}
		return items;
	}

	/**
	 * Returns the first or last item for a specific <code>property</code>
	 * items list.
	 * 
	 * @param property
	 * @param first
	 * @return the first or last item
	 */
	private DataProvider getFirstOrLastItem(String property, boolean first) {
		Object value = getValue(property);
		if (value instanceof LinkedList) {
			LinkedList linkedList = (LinkedList) value;
			if (!linkedList.isEmpty()) {
				if (first) {
					return (DataProvider) linkedList.getFirst();
				} else {
					return (DataProvider) linkedList.getLast();
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the count of items assocayed with the specified
	 * <code>property</code>.
	 * 
	 * @param property
	 * @return the count of items assocayed with the specified
	 *         <code>property</code>
	 */
	public int countItemValues(String property) {
		Object value = getValue(property);
		if (value instanceof LinkedList) {
			return ((LinkedList) value).getLength();
		}
		return 0;
	}
	
	/**
	 * Returns the first {@link DataProvider} item of the <code>property</code>
	 * items list or <code>null</code> if the list is empty or doesn't exists.
	 * 
	 * @param property
	 * @return the first {@link DataProvider} item
	 */
	public DataProvider getFirstItem(String property) {
		return getFirstOrLastItem(property, true);
	}

	/**
	 * Returns the last {@link DataProvider} item of the <code>property</code>
	 * items list or <code>null</code> if the list is empty or doesn't exists.
	 * 
	 * @param property
	 * @return the last {@link DataProvider} item
	 */
	public DataProvider getLastItem(String property) {
		return getFirstOrLastItem(property, false);
	}
	
	/**
	 * Add the <code>item</code> to the <code>property</code> items list.
	 * 
	 * @param property
	 * @param item
	 * @return the new items linkedList size or <code>-1</code> if adding is
	 *         faild.
	 */
	public int addItem(String property, DataProvider item) {
		return addItem(property, item, null, false);
	}
	
	/**
	 * Add the <code>item</code> to the <code>property</code> items list by
	 * placing it after or before the <code>referenceItem</code> according to
	 * the <code>after</code> parameter. If <code>item</code> is
	 * <code>null</code> nothing append and <code>-1</code> is returned.
	 * 
	 * @param property
	 * @param item
	 * @param referenceItem
	 * @param after
	 * @return the new items linkedList size or <code>-1</code> if adding is
	 *         faild.
	 */
	public int addItem(String property, DataProvider item, DataProvider referenceItem, boolean after) {
		if (item != null) {
			LinkedList items = getOrCreateItemValue(property);
			if (items != null) {
				if (items.isEmpty() || referenceItem == null) {
					items.add(item);
					dispatchItemsUpdateEvent(ADD_MODEL_UPDATE_EVENT_TYPE, property, item, null);
				} else {
					try {
						items.add(item, referenceItem, after);
						dispatchItemsUpdateEvent(after ? ADD_AFTER_MODEL_UPDATE_EVENT_TYPE : ADD_BEFORE_MODEL_UPDATE_EVENT_TYPE, property, item, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return items.getLength();
			}
		}
		return -1;
	}
	
	/**
	 * Remove the <code>item</code> from the <code>property</code> items
	 * list.
	 * 
	 * @param property
	 * @param item
	 * @return the new items linkedList size or <code>-1</code> if removing is
	 *         faild.
	 */
	public int removeItem(String property, DataProvider item) {
		if (item != null && itemsValues != null && itemsValues.containsKey(property)) {
			Object tmpItems = itemsValues.get(property);
			if (tmpItems instanceof LinkedList) {
				LinkedList items = ((LinkedList) tmpItems);
				items.remove(item);
				dispatchItemsUpdateEvent(REMOVE_MODEL_UPDATE_EVENT_TYPE, property, item, null);
				return items.getLength();
			}
		}
		return -1;
	}

	/**
	 * Remove all items from the <code>property</code> items list.
	 * 
	 * @param property
	 */
	public void removeAllItems(String property) {
		if (itemsValues != null) {
			itemsValues.remove(property);
			dispatchItemsUpdateEvent(CLEAR_MODEL_UPDATE_EVENT_TYPE, property, null, null);
		}
	}

	/**
	 * Search a {@link LinkedListItem} in {@link LinkedList} and return <code>true</code> if it's in.
	 * The value linked to <code>property</code> must be a {@link LinkedList}. 
	 * 
	 * @param property the property where <code>item</code> may be found
	 * @param item the {@link LinkedListItem} to search
	 * @return <code>true</code> if <code>item</code> exist in {@link LinkedList} <code>property</code>, <code>false</code> else.
	 */
	public boolean contains(String property, final DataProvider item) {
		if (item != null && itemsValues != null && itemsValues.containsKey(property)) {
			Object tmpItems = itemsValues.get(property);
			if (tmpItems instanceof LinkedList) {
				LinkedList items = ((LinkedList) tmpItems);
				if (items.find(new Filter() {
					public int accept(Object obj) {
						if (obj.equals(item)) {
							return 1;
						}
						return 0;
					}
					
				}) != null) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Sort <code>property</code> items list.
	 * 
	 * @param property
	 * @param flag
	 */
	public void sortItems(String property, int flag) {
		if (itemsValues != null && itemsValues.containsKey(property)) {
			Object tmpItems = itemsValues.get(property);
			if (tmpItems instanceof LinkedList) {
				LinkedList items = ((LinkedList) tmpItems);
				items.sort(flag);
				dispatchItemsUpdateEvent(SORT_MODEL_UPDATE_EVENT_TYPE, property, null, items);
			}
		}
	}

	/**
	 * Bind the <code>widget</code> to this {@link DataProvider}.
	 * 
	 * @param widget
	 */
	public void bind(Widget widget) {
		if (bindedWidgets == null) {
			bindedWidgets = new Vector();
		} else if (bindedWidgets.contains(widget)) {
			return;
		}
		bindedWidgets.addElement(widget);
		widget.setDataProvider(this);
		widget.processDataBindEvent();
	}

	/**
	 * Unbind the <code>widget</code> from this {@link DataProvider}.
	 * 
	 * @param widget
	 */
	public void unbind(Widget widget) {
		if (bindedWidgets != null) {
			widget.setDataProvider(null);
			bindedWidgets.removeElement(widget);
		}
	}

	/**
	 * Unbind all widgets from this {@link DataProvider}.
	 */
	public void unbindAll() {
		if (bindedWidgets != null) {
			int size = bindedWidgets.size();
			for (int i = 0; i < size; ++i) {
				((Widget) (bindedWidgets.elementAt(i))).setDataProvider(null);
			}
			bindedWidgets.removeAllElements();
		}
	}

	/**
	 * Dispatch an update event for a specific <code>property</code> to all
	 * binded widgets.
	 * 
	 * @param property
	 */
	protected void dispatchUpdateEvent(String property) {
		if (bindedWidgets != null) {
			int size = bindedWidgets.size();
			for (int i = 0; i < size; ++i) {
				((Widget) (bindedWidgets.elementAt(i))).processModelUpdateEvent(property);
			}
		}
	}

	/**
	 * Dispatch an items update event for a specific <code>property</code> to all
	 * binded widgets.
	 * 
	 * @param type
	 * @param property
	 * @param item
	 * @param items
	 */
	protected void dispatchItemsUpdateEvent(byte type, String property, DataProvider item, LinkedList items) {
		if (bindedWidgets != null) {
			int size = bindedWidgets.size();
			for (int i = 0; i < size; ++i) {
				((Widget) (bindedWidgets.elementAt(i))).processItemsModelUpdateEvent(type, property, item, items);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.LinkedListItem#compareTo(org.kalmeo.util.LinkedListItem, int)
	 */
	public int compareTo(LinkedListItem item, int flag) {
		return 0;
	}

}
