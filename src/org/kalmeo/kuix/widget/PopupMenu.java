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
import org.kalmeo.kuix.core.focus.PopupMenuFocusManager;
import org.kalmeo.kuix.layout.InlineLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.util.Alignment;
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
 * 		<td> <code>layout</code> </th>
 * 		<td> <code>inlinelayout(false,fill)</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout-data</code> </th>
 * 		<td> <code>sld(null,-1,-1)</code> </td>
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
public class PopupMenu extends Widget {

	// Defaults
	private static final Layout POPUP_MENU_DEFAULT_LAYOUT = new InlineLayout(false, Alignment.FILL);

	// The associated PopupMenuFocusManager
	private final PopupMenuFocusManager focusManager;
	
	// The associated menu
	public Menu menu;
	
	// Instance of PopupMenu StaticLayoutData
	private final StaticLayoutData layoutData = new StaticLayoutData(null, -1, -1);
	
	/**
	 * Construct a {@link PopupMenu}
	 */
	public PopupMenu() {
		super(KuixConstants.POPUP_MENU_WIDGET_TAG);
		focusManager = new PopupMenuFocusManager(this);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getDefaultStyleAttributeValue(java.lang.String)
	 */
	protected Object getDefaultStylePropertyValue(String name) {
		if (KuixConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return POPUP_MENU_DEFAULT_LAYOUT;
		}
		return super.getDefaultStylePropertyValue(name);
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

	/**
	 * Display the {@link PopupMenu}
	 * 
	 * @param invoker
	 * @param x
	 * @param y
	 */
	public void show(Widget invoker, int x, int y) {
		
		Desktop desktop = invoker.getDesktop();
		if (desktop != null) {
			int desktopWidth = desktop.getWidth();
			int desktopHeight = desktop.getHeight();
			Metrics preferredSize = getPreferredSize(desktopWidth);
			int width = preferredSize.width;
			int height = Math.min(preferredSize.height, desktopHeight);
			
			for (Widget widget = invoker; widget != desktop; widget = widget.parent) {
				x += widget.getX(); 
				y += widget.getY();
			}
			
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
