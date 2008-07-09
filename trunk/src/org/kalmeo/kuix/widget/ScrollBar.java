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

import javax.microedition.lcdui.Graphics;

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayout;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.util.BooleanUtil;
import org.kalmeo.util.MathFP;

/**
 * This class represent a scroll bar.
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
 * 		<td> <code>value</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the scroll bar value. The value is decimal (fixed point integer) and between 0 and 1. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>selection</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the scroll bar selection. The value is decimal (fixed point integer) and between 0 and 1. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>horizontal</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define if this scrollBar is horizontal. Default value is <code>false</code>. </td>
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
 * 		<td> <code>staticlayout</code> </td>
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
public class ScrollBar extends Widget {

	// Bar child
	private final StaticLayoutData barLayoutData;
	private final Widget bar;
	
	// fixed-point integer
	private int selection;
	private int value;
	
	// ScrollBar direction
	private boolean horizontal = false;
	
	// Internal use
	private int pressedX = 0;
	private int pressedY = 0;
	private int pressedValue = 0;
	
	/**
	 * Construct a {@link ScrollBar}
	 */
	public ScrollBar() {
		super(KuixConstants.SCROLL_BAR_WIDGET_TAG);
		barLayoutData = new StaticLayoutData(null, -1, 0);
		bar = new Widget(KuixConstants.BAR_WIDGET_TAG) {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
			 */
			public LayoutData getLayoutData() {
				return barLayoutData;
			}
			
		};
		add(bar);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.VALUE_ATTRIBUTE.equals(name)) {
			setValue(MathFP.toFP(value));
			return true;
		}
		if (KuixConstants.SELECTION_ATTRIBUTE.equals(name)) {
			setSelection(MathFP.toFP(value));
			return true;
		}
		if (KuixConstants.HORIZONTAL_ATTRIBUTE.equals(name)) {
			setHorizontal(BooleanUtil.parseBoolean(value));
			return true;
		}
		return super.setAttribute(name, value);
	}

	/**
	 * Return a fixed-point integer representing the selection
	 * 
	 * @return the selection
	 */
	public int getSelection() {
		return selection;
	}

	/**
	 * Define the ScrollBar selection
	 * 
	 * @param selection a fixed-point integer representing the selection
	 */
	public void setSelection(int selection) {
		this.selection = Math.min(MathFP.ONE, Math.max(0, selection));
		if (horizontal) {
			barLayoutData.width = this.selection;
			barLayoutData.height = -1;
		} else {
			barLayoutData.width = -1;
			barLayoutData.height = this.selection;
		}
		bar.setVisible(this.selection != MathFP.ONE || this.selection != 0);
		bar.invalidate();
	}

	/**
	 * Return a fixed-point integer representing the value
	 * 
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Define the ScrollBar value
	 * 
	 * @param value a fixed-point integer representing the value
	 */
	public void setValue(int value) {
		int lastValue = this.value;
		this.value = Math.min(MathFP.ONE, Math.max(0, value));
		if (this.value != lastValue) {
			invalidate();
		}
	}
	
	/**
	 * @return the horizontal
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * @param horizontal the horizontal to set
	 */
	public void setHorizontal(boolean horizontal) {
		this.horizontal = horizontal;
		setSelection(selection);	// Reapply the selection to switch direction
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return StaticLayout.instance;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#paintChildrenImpl(javax.microedition.lcdui.Graphics)
	 */
	protected void paintChildrenImpl(Graphics g) {
		int xOffset = horizontal ? MathFP.mul(value, MathFP.mul(getInnerWidth(), MathFP.ONE - selection)) : 0;
		int yOffset = horizontal ? 0 : MathFP.mul(value, MathFP.mul(getInnerHeight(), MathFP.ONE - selection));
		g.translate(xOffset, yOffset);
		super.paintChildrenImpl(g);
		g.translate(-xOffset, -yOffset);
	}

	/**
	 * Process a value change event produce by a UI action.
	 */
	protected void processChangeEvent() {
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		switch (type) {
			
			case KuixConstants.POINTER_PRESSED_EVENT_TYPE: {
				pressedX = x;
				pressedY = y;
				pressedValue = value;
				return true;
			}
			
			case KuixConstants.POINTER_DRAGGED_EVENT_TYPE: {
				Insets insets = getInsets();
				if (horizontal) {
					int innerWidth = getWidth() - insets.left - insets.right;
					if (innerWidth != 0) {
						setValue(pressedValue + MathFP.div(x - pressedX, MathFP.mul(innerWidth, MathFP.ONE - selection)));
						processChangeEvent();
					}
				} else {
					int innerHeight = getHeight() - insets.top - insets.bottom;
					if (innerHeight != 0) {
						setValue(pressedValue + MathFP.div(y - pressedY, MathFP.mul(innerHeight, MathFP.ONE - selection)));
						processChangeEvent();
					}
				}
				return true;
			}
			
		}
		return super.processPointerEvent(type, x, y);
	}
	
}
