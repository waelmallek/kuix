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

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;

/**
 * This abstract class is base for all action widgets.
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
 * 		<td> <code>onaction</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The action called method. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="5"> Inherited attributes : see {@link AbstractFocusableWidget} </td>
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
 * 		<td colspan="4"> Inherited style properties : see {@link AbstractFocusableWidget} </td>
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
 * 		<td> <code>disabled</code> </th>
 * 		<td> Active when the widget is disabled (<code>enabled = false</code>) </th>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link AbstractFocusableWidget} </td>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link AbstractFocusableWidget} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public abstract class AbstractActionWidget extends AbstractFocusableWidget {
	
	// The action method
	private String onAction;

	/**
	 * Construct a {@link AbstractActionWidget}
	 *
	 * @param tag
	 */
	public AbstractActionWidget(String tag) {
		super(tag);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.ON_ACTION_ATTRIBUTE.equals(name)) {
			setOnAction(value);
			return true;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractFocusableWidget#isPseudoClassCompatible(java.lang.String)
	 */
	public boolean isPseudoClassCompatible(String pseudoClass) {
		if (DISABLED_PSEUDO_CLASS.equals(pseudoClass)) {
			return !isEnabled();
		}
		return super.isPseudoClassCompatible(pseudoClass);
	}

	/**
	 * @param onAction
	 */
	public void setOnAction(String onAction) {
		this.onAction = onAction;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processKeyEvent(byte, intt)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		if (isEnabled()
				&& kuixKeyCode == KuixConstants.KUIX_KEY_FIRE
				&& type == KuixConstants.KEY_PRESSED_EVENT_TYPE) {
			return processActionEvent();
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processShortcutKeyEvent(byte, int)
	 */
	public boolean processShortcutKeyEvent(byte type, int kuixKeyCode) {
		if (isEnabled()) {
			if (!super.processShortcutKeyEvent(type, kuixKeyCode)) {
				return processActionEvent();
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		if (isEnabled() && type == KuixConstants.POINTER_RELEASED_EVENT_TYPE) {
			boolean superProcess = super.processPointerEvent(type, x, y);
			return processActionEvent() || superProcess;
		}
		return super.processPointerEvent(type, x, y);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processActionEvent()
	 */
	public boolean processActionEvent() {
		if (onAction != null) {
			Kuix.callActionMethod(Kuix.parseMethod(onAction, this));
			return true;
		}
		return false;
	}
}
