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
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.util.BooleanUtil;

/**
 * This abstract class is base for all focusable widgets.
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
 * 		<td> <code>enabled</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the widget's state. The value is a boolean (<code>true or false</code>). </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>focusable</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define if the widget is focusable. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>onfocus</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The focus gain called method. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>onlostfocus</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The focus lost called method. </td>
 * 	</tr>
 * 	<tr>
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
 * 		<td> <code>hover</code> </th>
 * 		<td> Active when the widget is focused </th>
 *	</tr>
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
public abstract class AbstractFocusableWidget extends Widget {

	// Widget's pseudo class list
	public static final String HOVER_PSEUDO_CLASS = "hover";
	public static final String DISABLED_PSEUDO_CLASS = "disabled";
	protected static final String[] PSEUDO_CLASSES = new String[] { HOVER_PSEUDO_CLASS, DISABLED_PSEUDO_CLASS };

	// Focusable ?
	protected boolean focusable = true;
	
	// Button focus state
	protected boolean focused = false;
	
	// The onFocus method
	private String onFocus;
	private String onLostFocus;
	
	// Define the action widget state
	protected boolean enabled = true;
	
	/**
	 * Construct a {@link AbstractFocusableWidget}
	 */
	public AbstractFocusableWidget() {
		super();
	}
	
	/**
	 * Construct a {@link AbstractFocusableWidget}
	 *
	 * @param tag
	 */
	public AbstractFocusableWidget(String tag) {
		super(tag);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.ON_FOCUS_ATTRIBUTE.equals(name)) {
			onFocus = value;
			return true;
		}
		if (KuixConstants.ON_LOST_FOCUS_ATTRIBUTE.equals(name)) {
			onLostFocus = value;
			return true;
		}
		if (KuixConstants.ENABLED_ATTRIBUTE.equals(name)) {
			setEnabled(BooleanUtil.parseBoolean(value));
			return true;
		}
		if (KuixConstants.FOCUSABLE_ATTRIBUTE.equals(name)) {
			setFocusable(BooleanUtil.parseBoolean(value));
			return true;
		}
		return super.setAttribute(name, value);
	}

	/**
	 * @param focusable the focusable to set
	 */
	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
		if (!focusable) {
			giveFocusToNearestWidget();
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isFocusable()
	 */
	public boolean isFocusable() {
		return enabled && focusable;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isFocused()
	 */
	public boolean isFocused() {
		return focused;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		if (!enabled) {
			giveFocusToNearestWidget();
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getPseudoClass()
	 */
	public String[] getAvailablePseudoClasses() {
		return PSEUDO_CLASSES;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isPseudoClassCompatible(java.lang.String)
	 */
	public boolean isPseudoClassCompatible(String pseudoClass) {
		if (HOVER_PSEUDO_CLASS.equals(pseudoClass)) {
			return isFocused();
		}
		return false;
	}
	
	/**
	 * Give the focus to the nearest {@link AbstractFocusableWidget}.
	 */
	private void giveFocusToNearestWidget() {
		if (isFocused()) {
			FocusManager focusManager = getFocusManager();
			if (focusManager != null) {
				focusManager.requestOtherFocus(true, null);				// Request forward focus
				if (focusManager.getFocusedWidget() == this) {			// No next focus ?
					focusManager.requestOtherFocus(false, null);		// Request backward focus
					if (focusManager.getFocusedWidget() == this) {		// No previous focus ?
						focusManager.requestFocus(null);				// No focus
					}
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#remove()
	 */
	public void remove() {
		giveFocusToNearestWidget();
		super.remove();
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#requestFocus()
	 */
	public void requestFocus() {
		FocusManager focusManager = getFocusManager();
		if (focusManager != null) {
			ScrollContainer scrollContainer = focusManager.findFirstScrollContainerParent(this);
			if (scrollContainer != null) {
				scrollContainer.bestScrollToChild(this, false);
			}
			focusManager.requestFocus(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processEvent(org.kalmeo.kuix.core.event.KuixEvent)
	 */
	public boolean processFocusEvent(byte type) {
		switch (type) {
			case KuixConstants.FOCUS_GAINED_EVENT_TYPE: {
				focused = true;
				if (onFocus != null) {
					Kuix.callActionMethod(Kuix.parseMethod(onFocus, this, getDesktop()));
				}
				propagateFocusEvent(this, false);
				return true;
			}
			case KuixConstants.FOCUS_LOST_EVENT_TYPE: {
				focused = false;
				if (onLostFocus != null) {
					Kuix.callActionMethod(Kuix.parseMethod(onLostFocus, this, getDesktop()));
				}
				propagateFocusEvent(this, true);
				return true;
			}
		}
		return super.processFocusEvent(type);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		if (type == KuixConstants.POINTER_RELEASED_EVENT_TYPE) {
			if (isFocusable()) {
				requestFocus();
				return true;
			}
		}
		return super.processPointerEvent(type, x, y);
	}
	
}
