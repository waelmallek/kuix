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
import org.kalmeo.util.LinkedList.LinkedListEnumeration;

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
		this(KuixConstants.LIST_WIDGET_TAG);
	}
	
	/**
	 * Construct a {@link List}
	 *
	 * @param tag
	 */
	public List(String tag) {
		super(tag);
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
			if (value instanceof LinkedListEnumeration) {
				setItems((LinkedListEnumeration) value);
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

	/**
	 * Redifine all item values
	 * 
	 * @param itemsEnumeration
	 */
	public void setItems(LinkedListEnumeration itemsEnumeration) {
		removeAllItems();
		if (itemsEnumeration != null) {
			try {
				itemsEnumeration.reset();
				while (itemsEnumeration.hasNextItems()) {
					addItem((DataProvider) itemsEnumeration.nextItem());
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
		return internalAddItem(item, renderer, referenceItem != null ? getListItem(referenceItem) : null, after);
	}
	
	/**
	 * Add an item value near an other {@link ListItem}.
	 * 
	 * @param item
	 * @param renderer
	 * @param referenceListItem
	 * @param after
	 * @return The added {@link ListItem}
	 */
	private ListItem internalAddItem(DataProvider item, InputStream renderer, ListItem referenceListItem, boolean after) {
		ListItem listItem = newListItemInstance(item);
		if (referenceListItem != null) {
			super.add(listItem, referenceListItem, after);
		} else {
			super.add(listItem);
		}
		Kuix.loadXml(listItem, renderer, item, true);
		dataProvidersMapping.put(item, listItem);
		return listItem;
	}
	
	/**
	 * Create and returns a new instance of a {@link ListItem}.
	 * 
	 * @param item
	 * @return a new instance of {@link ListItem}.
	 */
	protected ListItem newListItemInstance(DataProvider item) {
		return new ListItem(item);
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
		internalRemoveItem(listItem);
		return listItem != null;
	}
	
	/**
	 * @param listItem
	 * @return <code>true</code> if the <code>listItem</code> is found and the
	 *         associated {@link ListItem} removed
	 */
	private void internalRemoveItem(ListItem listItem) {
		if (listItem != null) {
			dataProvidersMapping.remove(listItem.getDataProvider());
			listItem.cleanUp();
			listItem.remove();
		}
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
	
	/**
	 * Returns the next (after <code>startWidget</code>) {@link ListItem}
	 * child instance.
	 * 
	 * @param startWidget
	 * @return a {@link ListItem} child instance.
	 */
	private ListItem getNextListItem(Widget startWidget) {
		if (startWidget == null) {
			startWidget = getChild();
		} else {
			startWidget = startWidget.next;
		}
		if (startWidget != null) {
			Widget nextWidget = startWidget;
			while (nextWidget != null) {
				if (nextWidget instanceof ListItem) {
					return (ListItem) nextWidget;
				}
				nextWidget = nextWidget.next;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processItemsModelUpdateEvent(byte, java.lang.String, org.kalmeo.kuix.core.model.DataProvider, org.kalmeo.util.LinkedList.LinkedListEnumeration)
	 */
	public boolean processItemsModelUpdateEvent(byte type, String property, DataProvider item, LinkedListEnumeration itemsEnumeration) {
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

						case DataProvider.SORT_MODEL_UPDATE_EVENT_TYPE: {
							
							// Reorder listItems
							
							ListItem previousListItem = null;
							itemsEnumeration.reset();
							if (itemsEnumeration.hasNextItems()) {
								// Bring the first item to front
								previousListItem = getListItem((DataProvider) itemsEnumeration.nextItem());
								bringToFront(previousListItem);
							}
							
							// BringNear the orthers
							ListItem listItem;
							while (itemsEnumeration.hasNextItems()) {
								listItem = getListItem((DataProvider) itemsEnumeration.nextItem());
								if (listItem != null) {
									bringNear(listItem, previousListItem, true);
									previousListItem = listItem;
								}
							}
							return true;
						}
							
						case DataProvider.FILTER_MODEL_UPDATE_EVENT_TYPE: {
							
							itemsEnumeration.reset();
							if (itemsEnumeration.hasNextItems()) {
								
								LinkedList items = itemsEnumeration.getList();
								LinkedListItem linkedListItem = itemsEnumeration.nextItem();
								ListItem listItem = getNextListItem(null);
								
								for (LinkedListItem currentItem = items.getFirst(); currentItem != null; currentItem = currentItem.getNext()) {
									
									boolean isEnumerationItem = currentItem.equals(linkedListItem);
									boolean isListItemDataProvider = listItem != null && currentItem.equals(listItem.getDataProvider());
									
									// If listItem and linkedListItem are not in at least one of two list, continue
									if (!isListItemDataProvider && !isEnumerationItem) {
										continue;
									}
									
									// If listItem and linkedListItem are in both of two list, get next items and continue
									if (isListItemDataProvider && isEnumerationItem) {
										linkedListItem = null;
										if (itemsEnumeration.hasNextItems()) {
											linkedListItem = itemsEnumeration.nextItem();
										}
										
										listItem = getNextListItem(listItem);
										continue;
									}
									
									// If not in list but in enumeration, add in list
									if (!isListItemDataProvider && isEnumerationItem) {
										if (listItem != null) {
											if (renderer != null) {
												renderer.reset();
											}
											internalAddItem((DataProvider) linkedListItem, renderer, listItem, false);
										} else {
											addItem((DataProvider) linkedListItem);
										}
										
										if (itemsEnumeration.hasNextItems()) {
											linkedListItem = itemsEnumeration.nextItem();
										} else {
											linkedListItem = null;
										}
										
									// If already in list but not in enumeration, remove it
									} else if (isListItemDataProvider && !isEnumerationItem) {
										ListItem nextListItem = (ListItem) listItem.next;
										internalRemoveItem(listItem);
										listItem = nextListItem;
									}
									
									// All items are trailed in linkedList and list
									if (linkedListItem == null && listItem == null) {
										break;
									}
								}
								return true;
								
							}
							
							removeAllItems();
							return true;
						}
							
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
