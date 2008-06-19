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
 * Creation date : 22 nov. 07
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.widget;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.model.DataProvider;

/**
 * This class represent a radio button group.
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
 * 		<td> The selected RadioButton value </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>onchange</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The change called method. </td>
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
public class RadioGroup extends List {

	// The selected RadioButton
	private RadioButton selectedRadioButton = null;
	
	// The change method
	private String onChange;
	
	// The value is kept in this variable only if no radio button corresponding
	private String wantedValue;
	
	/**
	 * Construct a {@link RadioGroup}
	 */
	public RadioGroup() {
		super(KuixConstants.RADIO_GROUP_WIDGET_TAG);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.CheckBox#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.VALUE_ATTRIBUTE.equals(name)) {
			setValue(value);
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
			return getValue();
		}
		return null;
	}

	/**
	 * @return the select radio button value
	 */
	public String getValue() {
		if (selectedRadioButton != null) {
			return selectedRadioButton.getValue();
		}
		return null;
	}
	
	/**
	 * Check if the value exists in RadioButton children value and the select
	 * the first occurrence
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		if (value != null) {
			RadioButton radioButton = null;
			for (Widget widget = getChild(); widget != null; widget = widget.next) {
				if (widget instanceof RadioButton) {
					radioButton = (RadioButton) widget;
					if (value.equals(radioButton.getValue())) {
						setSelectedRadioButton(radioButton);
						return;
					}
				}
			}
		}
		setSelectedRadioButton(null);
		wantedValue = value;
	}
	
	/**
	 * @return the selectedButton
	 */
	public RadioButton getSelectedRadioButton() {
		return selectedRadioButton;
	}

	/**
	 * @param radioButton the selectedButton to set
	 */
	public void setSelectedRadioButton(RadioButton radioButton) {
		wantedValue = null;
		if (radioButton != null && radioButton.parent != this) {
			return;
		}
		if (selectedRadioButton != null) {
			selectedRadioButton.setSelected(false);
		}
		boolean changed = selectedRadioButton != radioButton;
		selectedRadioButton = radioButton;
		if (radioButton != null) {
			radioButton.setSelected(true);
		}
		if (changed && onChange != null) {
			Kuix.callActionMethod(Kuix.parseMethod(onChange, this));
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.List#newItemWidgetInstance(org.kalmeo.kuix.core.model.DataProvider)
	 */
	protected Widget newItemWidgetInstance(DataProvider item) {
		return new RadioButton(item);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		super.add(widget);
		if (widget instanceof RadioButton) {
			RadioButton radioButton = (RadioButton) widget;
			if (radioButton.isSelected() || (wantedValue != null && wantedValue.equals(radioButton.getValue()))) {
				setSelectedRadioButton(radioButton);
			}
		}
		return this;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#cleanUp()
	 */
	public void cleanUp() {
		selectedRadioButton = null;
		super.cleanUp();
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onChildRemoved(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onChildRemoved(Widget widget) {
		if (widget == selectedRadioButton) {
			setSelectedRadioButton(null);
		}
		super.onChildRemoved(widget);
	}
	
}

