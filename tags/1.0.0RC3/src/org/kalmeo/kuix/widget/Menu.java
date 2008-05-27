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

import java.util.Vector;

import org.kalmeo.kuix.core.KuixConstants;

/**
 * This class represent a menu.
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
 * 		<td colspan="5"> Inherited attributes : see {@link MenuItem} </td>
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
 * 		<td colspan="4"> Inherited style properties : see {@link MenuItem} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link MenuItem} </td>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link MenuItem} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class Menu extends MenuItem {

	// The static menus list
	private static final Vector menus = new Vector();
	
	// The associated popupMenu
	protected PopupMenu popup;
	
	/**
	 * Construct a {@link Menu}
	 */
	public Menu() {
		this(KuixConstants.MENU_WIDGET_TAG);
	}
	
	/**
	 * Construct a {@link Menu}
	 *
	 * @param tag
	 */
	public Menu(String tag) {
		super(tag);
		// Add the menu to the static list
		menus.addElement(this);
	}
	
	/**
	 * @return the popup
	 */
	public PopupMenu getPopup() {
		return popup;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		if (parent instanceof PopupMenu) {
			return ((PopupMenu) parent).menu.getDepth() + 1;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		if (widget instanceof PopupMenu) {
			popup = (PopupMenu) widget;
			popup.menu = this; // It's not possible to use parent here because the popup menu will be place in an other widget
		} else {
			super.add(widget);
		}
		return this;
	}

	/**
	 * Open the popupMenu
	 */
	public void showPopup() {
		showPopup(getWidth(), getHeight());
	}

	/**
	 * Open the popupMenu
	 * 
	 * @param x
	 * @param y
	 */
	public void showPopup(int x, int y) {
		hideAllPopupMenu();
		if (popup != null) {
			popup.show(Menu.this, x, y);
		}
	}
	
	/**
	 * Close the popupMenu
	 */
	public void hidePopup() {
		if (popup != null) {
			popup.hide();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.MenuItem#hideMenuTree()
	 */
	public boolean hideMenuTree() {
		if (!super.hideMenuTree()) {
			hidePopup();
			return true;
		}
		return false;
	}

	/**
	 * Hide all popup for menu in the same group 
	 */
	protected void hideAllPopupMenu() {
		int refLevel = getDepth();
		Menu menu;
		for (int i = menus.size() - 1; i>=0; --i) {
			menu = ((Menu) menus.elementAt(i));
			if (menu.getDepth() >= refLevel) {
				menu.hidePopup();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#cleanUp()
	 */
	public void cleanUp() {
		super.cleanUp();
		hidePopup();
		if (popup != null) {
			popup.cleanUp();
		}
		menus.removeElement(this);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.MenuItem#processActionEvent()
	 */
	public boolean processActionEvent() {
		if (popup != null) {
			if (popup.parent == null) {
				showPopup();
			}
			return true;
		}
		return super.processActionEvent();
	}

}
