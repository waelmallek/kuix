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

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.core.model.DataProvider;

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
	
	// Used as a workaround CLDC 1.0 limitation on PopupMenu.class
	private static final Class POPUP_MENU_CLASS = new PopupMenu().getClass();

	// The associated popupMenu
	protected PopupMenu popup;
	
	/**
	 * Construct a {@link Menu}
	 */
	public Menu() {
		this(null);
	}
	
	/**
	 * Construct a {@link Menu}
	 *
	 * @param dataProvider
	 */
	public Menu(DataProvider dataProvider) {
		this(KuixConstants.MENU_WIDGET_TAG, dataProvider);
	}
	
	/**
	 * Construct a {@link Menu}
	 *
	 * @param tag
	 * @param dataProvider
	 */
	public Menu(String tag, DataProvider dataProvider) {
		super(tag, dataProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getInternalChildInstance(java.lang.String)
	 */
	public Widget getInternalChildInstance(String tag) {
		if (KuixConstants.POPUP_MENU_WIDGET_TAG.equals(tag)) {
			return getPopup();
		}
		return super.getInternalChildInstance(tag);
	}

	/**
	 * @return the popup
	 */
	public PopupMenu getPopup() {
		if (popup == null) {
			popup = new PopupMenu();
			popup.menu = this;
		}
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

	/**
	 * Open the popupMenu
	 */
	public void showPopup() {
		showPopup(getDisplayX() + getWidth(), getDisplayY() + getHeight());
	}

	/**
	 * Open the popupMenu
	 * 
	 * @param displayX
	 * @param displayY
	 */
	public void showPopup(int displayX, int displayY) {
		if (getDepth() == 0) {
			hideAllPopupMenu();
		}
		if (popup != null) {
			popup.show(getDesktop(), displayX, displayY);
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
	 * Hide all visible popupMenus
	 */
	protected static void hideAllPopupMenu() {
		KuixMIDlet.getDefault().getCanvas().getDesktop().removeAllPopupInstanceOf(POPUP_MENU_CLASS);
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
