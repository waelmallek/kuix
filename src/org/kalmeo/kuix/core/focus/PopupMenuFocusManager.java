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

package org.kalmeo.kuix.core.focus;

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.widget.Menu;
import org.kalmeo.kuix.widget.PopupMenu;
import org.kalmeo.kuix.widget.Widget;

/**
 * @author bbeaulant
 */
public class PopupMenuFocusManager extends FocusManager {

	/**
	 * Construct a {@link PopupMenuFocusManager}
	 *
	 * @param poupMenu
	 */
	public PopupMenuFocusManager(PopupMenu poupMenu) {
		super(poupMenu, true);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.focus.FocusManager#processKeyEvent(byte, int)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		switch (type) {

			case KuixConstants.KEY_PRESSED_EVENT_TYPE:
			case KuixConstants.KEY_REPEATED_EVENT_TYPE: {

				switch (kuixKeyCode) {
					
					case KuixConstants.KUIX_KEY_BACK:
					case KuixConstants.KUIX_KEY_LEFT:
						hidePopupMenu();
						return true;

					case KuixConstants.KUIX_KEY_RIGHT:
						Widget widget;
						// Search forward first
						for (widget = focusedWidget; widget != null; widget = widget.next) {
							if (widget instanceof Menu) {
								requestFocus(widget);
								((Menu) widget).processActionEvent();
								break;
							}
						}
						// Search backward
						for (widget = focusedWidget; widget != null; widget = widget.previous) {
							if (widget instanceof Menu) {
								requestFocus(widget);
								((Menu) widget).processActionEvent();
								break;
							}
						}
						return true;

				}
			}
		}
		if (!super.processKeyEvent(type, kuixKeyCode)) {
			return processSoftKeyEvent(type, kuixKeyCode);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.focus.FocusManager#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		boolean superProcess = super.processPointerEvent(type, x, y);
		if (type == KuixConstants.POINTER_RELEASED_EVENT_TYPE) {
			if (!superProcess) {
				hidePopupMenu();
				return true;
			}
		}
		return superProcess;
	}	
	
	/**
	 * Hide the associated {@link PopupMenu}
	 */
	private void hidePopupMenu() {
		PopupMenu popupMenu = ((PopupMenu) rootWidget);
		if (popupMenu.menu != null && !(popupMenu.menu.parent instanceof PopupMenu)) {
			popupMenu.menu.hideMenuTree();
		} else {
			popupMenu.hide();
		}
	}
	
}
