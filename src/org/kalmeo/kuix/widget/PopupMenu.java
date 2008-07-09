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
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.util.Metrics;

/**
 * This class represent a popup menu.
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
 * 		<td colspan="5"> Inherited attributes : see {@link List} </td>
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
 * 		<td> <code>layout-data</code> </th>
 * 		<td> <code>sld(null,-1,-1)</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link List} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="4"> Inherited style properties : see {@link List} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link List} </td>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link List} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class PopupMenu extends List {

	// The associated PopupMenuFocusManager
	private final FocusManager focusManager;
	
	// The associated parent menu
	public Menu menu;
	
	// Instance of PopupMenu StaticLayoutData
	private final StaticLayoutData layoutData = new StaticLayoutData(null, -1, -1);
	
	/**
	 * Construct a {@link PopupMenu}
	 */
	public PopupMenu() {
		super(KuixConstants.POPUP_MENU_WIDGET_TAG);
		focusManager = new FocusManager(this, true) {

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
			
		};

	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getFocusManager()
	 */
	public FocusManager getFocusManager() {
		return focusManager;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
	 */
	public LayoutData getLayoutData() {
		return layoutData;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.List#newItemWidgetInstance(org.kalmeo.kuix.core.model.DataProvider)
	 */
	protected Widget newItemWidgetInstance(DataProvider item) {
		return new MenuItem(item);
	}

	/**
	 * Display the {@link PopupMenu}
	 * 
	 * @param desktop
	 * @param displayX
	 * @param displayY
	 */
	public void show(Desktop desktop, int displayX, int displayY) {
		if (desktop != null) {
			
			int desktopWidth = desktop.getWidth();
			int desktopHeight = desktop.getHeight();
			Metrics preferredSize = getPreferredSize(desktopWidth);
			int width = preferredSize.width;
			int height = Math.min(preferredSize.height, desktopHeight);
			
			int x = displayX;
			int y = displayY;
			
			y -= height;
			
			if (x + width > desktopWidth) {
				x = desktopWidth - width;
			}
			
			layoutData.x = x;
			layoutData.y = y;
			
			desktop.addPopup(this);
		}
	}
	
	/**
	 * Hide this popupMenu
	 */
	public void hide() {
		if (parent == null) {
			return;
		}
		for (Widget widget = getChild(); widget != null; widget = widget.next) {
			if (widget instanceof Menu) {
				((Menu) widget).hidePopup();
			}
		}
		remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.kalmeo.kuix.widget.Widget#onRemoved(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onRemoved(Widget parent) {
		focusManager.reset();
	}
	
}
