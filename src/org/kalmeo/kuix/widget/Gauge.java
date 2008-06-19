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
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayout;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.util.MathFP;

/**
 * This class represent a gauge.
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
 * 		<td> <code>Yes</code> </td>
 * 		<td> Define the gauge value. The value is decimal (fixed point integer) and between 0 and 1. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>increment</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the gauge increment. The value is decimal (fixed point integer) and between 0 and 1. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>onchange</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The change called method. </td>
 *	</tr>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link AbstractFocusableWidget} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class Gauge extends AbstractFocusableWidget {

	// Bar child
	private final StaticLayoutData barLayoutData;
	private final Widget bar;
	
	// The gauge value (fixed-point integer)
	private int value;
	
	// Increment value (0 represents the minimal increment)
	private int increment = 0;

	// The change method
	private String onChange;
	
	/**
	 * Construct a {@link Gauge}
	 */
	public Gauge() {
		super(KuixConstants.GAUGE_WIDGET_TAG);
		barLayoutData = new StaticLayoutData(Alignment.LEFT, 0, -1); 	// Caution the bar would not be resized by the gauge. But it's the bar that resize the gauge.
		bar = new Widget(KuixConstants.BAR_WIDGET_TAG) {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
			 */
			public LayoutData getLayoutData() {
				return barLayoutData;
			}

		};
		add(bar);
		
		// By default this widget is not focusable
		setFocusable(false);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.VALUE_ATTRIBUTE.equals(name)) {
			setValue(MathFP.toFP(value));
			return true;
		}
		if (KuixConstants.INCREMENT_ATTRIBUTE.equals(name)) {
			setIncrement(MathFP.toFP(value));
			return true;
		}
		if (KuixConstants.ON_CHANGE_ATTRIBUTE.equals(name)) {
			onChange = value;
			return true;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if (KuixConstants.VALUE_ATTRIBUTE.equals(name)) {
			return MathFP.toString(value);
		}
		return super.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return StaticLayout.instance;
	}
	
	/**
	 * @return a fixed-point integer representing the value
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * @param value a fixed-point integer representing the value
	 */
	public void setValue(int value) {
		// First change check
		if (value != this.value) {
			
			if (increment != 0) {
				value = MathFP.mul(MathFP.ceil(MathFP.div(value, increment)), increment);
			}
			value = Math.min(MathFP.ONE, Math.max(0, value));
			
			// Second change check
			if (value != this.value) {
				this.value = value;
				invalidate();
				if (isFocusable() && onChange != null) {	// Enable and focusable
					Kuix.callActionMethod(Kuix.parseMethod(onChange, this));
				}
			}
			
		}
	}
	
	/**
	 * @return the increment
	 */
	public int getIncrement() {
		return increment;
	}

	/**
	 * @param increment the increment to set
	 */
	public void setIncrement(int increment) {
		this.increment = increment;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#doLayout()
	 */
	protected void doLayout() {
		// Layout the bar
		Insets insets = getInsets();
		int barWidth = MathFP.mul(MathFP.toFP(getWidth() - insets.left - insets.right),  value);
		barLayoutData.width = barWidth;
		
		super.doLayout();
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processKeyEvent(byte, int)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		if (isFocusable() 
				&& (type == KuixConstants.KEY_PRESSED_EVENT_TYPE
					|| type == KuixConstants.KEY_REPEATED_EVENT_TYPE)) {
			switch (kuixKeyCode) {
				case KuixConstants.KUIX_KEY_LEFT:
					setValue(value - increment);
					return true;
				case KuixConstants.KUIX_KEY_RIGHT:
					setValue(value + increment);
					return true;
			}
		}
		return super.processKeyEvent(type, kuixKeyCode);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		if (isFocusable() 
				&& (type == KuixConstants.POINTER_PRESSED_EVENT_TYPE 
						|| type == KuixConstants.POINTER_DRAGGED_EVENT_TYPE)) {
			
			// Convert coordinates in widget space
			for (Widget widget = this; widget != null; widget = widget.parent) {
				x -= widget.getX();
				y -= widget.getY();
			}
			Insets insets = getInsets();
			x -= insets.left;
			y -= insets.top;
			
			setValue(MathFP.div(MathFP.toFP(x), MathFP.toFP(getWidth() - insets.left - insets.right)));

			return true;
		}
		return super.processPointerEvent(type, x, y);
	}

}
