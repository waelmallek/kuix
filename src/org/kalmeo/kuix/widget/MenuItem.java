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
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.widget.Menu.MenuPopup;

/**
 * This class represents a menu item. <br>
 * <br>
 * <strong>For further informations, visit the <a
 * href="http://www.kalmeo.org/files/kuix/widgetdoc/index.html"
 * target="new">Kuix widgets reference page</a></strong>.
 * 
 * @author bbeaulant
 */
public class MenuItem extends ListItem {

	/**
	 * Construct a {@link MenuItem}
	 */
	public MenuItem() {
		this(null);
	}
	
	/**
	 * Construct a {@link MenuItem}
	 *
	 * @param dataProvider
	 */
	public MenuItem(DataProvider dataProvider) {
		this(KuixConstants.MENU_ITEM_WIDGET_TAG, dataProvider);
	}

	/**
	 * Construct a {@link MenuItem}
	 *
	 * @param tag
	 * @param dataProvider
	 */
	public MenuItem(String tag, DataProvider dataProvider) {
		super(tag, dataProvider);
	}
	
	/**
	 * Hide the menu tree
	 */
	public boolean hideMenuTree() {
		if (parent != null && parent instanceof MenuPopup) {
			((MenuPopup) parent).getMenu().hideMenuTree();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractActionWidget#processActionEvent()
	 */
	public boolean processActionEvent() {
		super.processActionEvent();
		hideMenuTree();	// Caution the action is execute before hideMenuTree !
		return true;
	}
	
}
