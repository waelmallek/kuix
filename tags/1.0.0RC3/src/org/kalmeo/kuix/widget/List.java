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

package org.kalmeo.kuix.widget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.layout.InlineLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.util.LinkedList;
import org.kalmeo.util.LinkedListItem;

/**
 * This class represent a list.
 * 
 * <table border="1" width="100%" cellpadding="3" cellspacing="0" >
 * 	<tr class="TableHeadingColor">
 * 		<th align="left" colspan="5"><font size="+2"> Attributes </font></th>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<th width="1%"> Attribute </th>
 * 		<th width="1%"> Object </th>
 * 		<th width="1%"> Set </th>
 * 		<th width="1%"> Get </th>
 * 		<th> Description </th>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>renderer</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The default ListItem renderer. The value is an XML template. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>items</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The list of items (DataProvider) values. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="5"> Inherited attributes : see {@link Widget} </td>
 * 	</tr>
 * </table>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0" >
 * 	<tr class="TableHeadingColor">
 * 		<th align="left" colspan="4"> <font size="+2"> Style properties </font> </th>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<th width="1%"> Property </th>
 * 		<th width="1%"> Default </th>
 * 		<th width="1%"> Inherit </th>
 * 		<th> Description </th>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>align</code> </th>
 * 		<td> <code>fill-top</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout</code> </th>
 * 		<td> <code>inlinelayout(false,fill)</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="4"> Inherited style properties : see {@link Widget} </td>
 * 	</tr>
 * </table>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0" >
 * 	<tr class="TableHeadingColor">
 * 		<th align="left" colspan="2"> <font size="+2"> Available style pseudo-classes </font> </th>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<th width="1%"> Pseudo-class </th>
 * 		<th> Description </th>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link Widget} </td>
 * 	</tr>
 * </table>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0" >
 * 	<tr class="TableHeadingColor">
 * 		<th align="left" colspan="2"> <font size="+2"> Available internal widgets </font> </th>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<th width="1%"> internal widget </th>
 * 		<th> Description </th>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="2"> Inherited internal widgets : see {@link Widget} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class List extends Widget {

	// Defaults
	private static final Alignment LIST_ALIGN = Alignment.FILL_TOP;
	private static final Layout LIST_LAYOUT = new InlineLayout(false, Alignment.FILL);
	
	// Default listItem renderer
	private ByteArrayInputStream renderer;
	
	// Represent the mapping between DataProviders and ListItems
	private final Hashtable dataProvidersMapping = new Hashtable();

	/**
	 * Construct a {@link List}
	 */
	public List() {
		super(KuixConstants.LIST_WIDGET_TAG);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.RENDERER_ATTRIBUTE.equals(name)) {
			if (value != null && value.length() != 0) {
				setRenderer(new ByteArrayInputStream(value.getBytes()));
				return true;
			}
			return false;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setObjectAttribute(java.lang.String, java.lang.Object)
	 */
	public boolean setObjectAttribute(String name, Object value) {
		if (KuixConstants.ITEMS_ATTRIBUTE.equals(name)) {
			if (value instanceof LinkedList) {
				setItems((LinkedList) value);
				return true;
			} else if (value == null) {
				setItems(null);
				return true;
			}
			return false;
		}
		return super.setObjectAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isObjectAttribute(java.lang.String)
	 */
	public boolean isObjectAttribute(String name) {
		if (KuixConstants.ITEMS_ATTRIBUTE.equals(name)) {
			return true;
		}
		return super.isObjectAttribute(name);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getAlign()
	 */
	public Alignment getAlign() {
		return LIST_ALIGN;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return LIST_LAYOUT;
	}
	
	/**
	 * @param renderer the renderer to set
	 */
	public void setRenderer(ByteArrayInputStream renderer) {
		this.renderer = renderer;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		if (widget instanceof ListItem) {
			return super.add(widget);
		}
		return null;
	}
	
	/**
	 * Redifine all item values
	 * 
	 * @param items
	 */
	public void setItems(LinkedList items) {
		removeAllItems();
		if (items != null) {
			try {
				for (LinkedListItem item = items.getFirst(); item != null; item = item.getNext()) {
					addItem((DataProvider) item);
				}
			} catch (ClassCastException e) {
				// An item need to extends the DataProvider model
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add an item value
	 * 
	 * @param item
	 * @return The added {@link ListItem} or <code>null</code> if no renderer is set on the list.
	 */
	public ListItem addItem(DataProvider item) {
		return addItem(item, null, false);
	}
	
	/**
	 * Add an item value
	 * 
	 * @param item
	 * @param renderer
	 * @return The added {@link ListItem}
	 */
	public ListItem addItem(DataProvider item, InputStream renderer) {
		return addItem(item, renderer, null, false);
	}
	
	/**
	 * Add an item value
	 * 
	 * @param item
	 * @param enabled
	 * @param prepend
	 * @return The added {@link ListItem} or <code>null</code> if no renderer is set on the list
	 */
	public ListItem addItem(DataProvider item, DataProvider referenceItem, boolean after) {
		if (renderer != null) {
			renderer.reset();
			return addItem(item, renderer, referenceItem, after);
		}
		return null;
	}
	
	/**
	 * Add an item value near an other.
	 * 
	 * @param item
	 * @param renderer
	 * @param referenceItem
	 * @param after
	 * @return The added {@link ListItem}
	 */
	public ListItem addItem(DataProvider item, InputStream renderer, DataProvider referenceItem, boolean after) {
		ListItem listItem = new ListItem(item);
		if (referenceItem != null) {
			ListItem referenceListItem = getListItem(referenceItem);
			super.add(listItem, referenceListItem, after);
		} else {
			super.add(listItem);
		}
		Kuix.loadXml(listItem, renderer, item, true);
		dataProvidersMapping.put(item, listItem);
		return listItem;
	}
	
	/**
	 * Remove an item value if it exists in the {@link List}
	 * 
	 * @param item
	 * @return <code>true</code> if the <code>item</code> is found and the
	 *         associated {@link ListItem} removed
	 */
	public boolean removeItem(DataProvider item) {
		ListItem listItem = getListItem(item);
		if (listItem != null) {
			listItem.cleanUp();
			listItem.remove();
			dataProvidersMapping.remove(item);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove all items
	 */
	public void removeAllItems() {
		cleanUpAll();
		removeAll();
		dataProvidersMapping.clear();
	}
	
	/**
	 * @param item
	 * @return The {@link ListItem} associated with the specified
	 *         {@link DataProvider}. If no item is found <code>null</code> is
	 *         returned
	 */
	public ListItem getListItem(DataProvider item) {
		return (ListItem) dataProvidersMapping.get(item);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processItemsModelUpdateEvent(byte, java.lang.String, org.kalmeo.kuix.core.model.DataProvider, org.kalmeo.util.LinkedList)
	 */
	public boolean processItemsModelUpdateEvent(byte type, String property, DataProvider item, LinkedList items) {
		if (hasBindInstruction()) {
			for (BindInstruction bindInstruction = (BindInstruction) bindInstructions.getFirst(); bindInstruction != null; bindInstruction = bindInstruction.next) {
				if (bindInstruction.hasProperty(property)) {
					switch (type) {

						case DataProvider.ADD_MODEL_UPDATE_EVENT_TYPE:
							addItem(item);
							return true;

						case DataProvider.ADD_BEFORE_MODEL_UPDATE_EVENT_TYPE:
							addItem(item, (DataProvider) item.getNext(), false);
							return true;
							
						case DataProvider.ADD_AFTER_MODEL_UPDATE_EVENT_TYPE:
							addItem(item, (DataProvider) item.getPrevious(), true);
							return true;
							
						case DataProvider.REMOVE_MODEL_UPDATE_EVENT_TYPE:
							removeItem(item);
							return true;

						case DataProvider.SORT_MODEL_UPDATE_EVENT_TYPE:
							
							// Reorder listItems
							
							// Extract first item
							DataProvider firstItem = (DataProvider) items.getFirst();
							
							// Bring the first item to front
							ListItem previousListItem = getListItem(firstItem);
							bringToFront(previousListItem);
							
							// BringNear the orthers
							ListItem listItem;
							for (LinkedListItem tmpItem = firstItem.getNext(); tmpItem != null; tmpItem = tmpItem.getNext()) {
								listItem = getListItem((DataProvider) tmpItem);
								if (listItem != null) {
									bringNear(listItem, previousListItem, true);
									previousListItem = listItem;
								}
							}
							
							return true;
							
						case DataProvider.CLEAR_MODEL_UPDATE_EVENT_TYPE:
							removeAllItems();
							return true;
							
					}
				}
			}
		}
		return false;
	}

}
