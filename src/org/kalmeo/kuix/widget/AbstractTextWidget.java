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
 * Creation date : 21 nov. 07
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.widget;

import javax.microedition.lcdui.Font;

import org.kalmeo.kuix.core.KuixConstants;

/**
 * This abstract class is base for all text widgets.
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
 * 		<td> <code>text</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> Define the text value. </td>
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
 * 		<td> <code>font-face</code> </th>
 * 		<td> <code>system</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the font face </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>font-style</code> </th>
 * 		<td> <code>plain</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the font style </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>font-size</code> </th>
 * 		<td> <code>medium</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the font size </td>
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
public abstract class AbstractTextWidget extends AbstractFocusableWidget {

	// The default Font value
	private Integer defaultFontFace = null;
	private Integer defaultFontStyle = null;
	private Integer defaultFontSize = null;
	
	// Text value
	protected String text;
	
	// The cached objects
	private Font cachedFont;

	/**
	 * Construct a {@link AbstractTextWidget}
	 * 
	 * @param tag
	 */
	public AbstractTextWidget(String tag) {
		super(tag);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.TEXT_ATTRIBUTE.equals(name)) {
			setText(value);
			return true;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if (KuixConstants.TEXT_ATTRIBUTE.equals(name)) {
			return getText();
		}
		return super.getAttribute(name);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.FocusableWidget#isFocusable()
	 */
	public boolean isFocusable() {
		return false;
	}
	
	/**
	 * @param defaultFontFace the defaultFontFace to set
	 */
	public void setDefaultFontFace(int defaultFontFace) {
		this.defaultFontFace = new Integer(defaultFontFace);
	}

	/**
	 * @param defaultFontStyle the defaultFontStyle to set
	 */
	public void setDefaultFontStyle(int defaultFontStyle) {
		this.defaultFontStyle = new Integer(defaultFontStyle);
	}

	/**
	 * @param defaultFontSize the defaultFontSize to set
	 */
	public void setDefaultFontSize(int defaultFontSize) {
		this.defaultFontSize = new Integer(defaultFontSize);
	}

	/**
	 * Returns the text value.
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Returns the displayed text. By default displayed text is the text value.
	 * 
	 * @return the displayed text
	 */
	protected String getDisplayedText() {
		return getText();
	}

	/**
	 * Returns the instance of this {@link AbstractTextWidget}
	 * Useful in this case :
	 * <code>AbstractTextWidget text = new Text().setText("message");</code>
	 * 
	 * @param text the text to set
	 */
	public AbstractTextWidget setText(String text) {
		this.text = text;
		invalidate();
		return this;
	}
	
	/**
	 * @return The font face
	 */
	private int getFontFace() {
		Object fontFaceValue = getStylePropertyValue(KuixConstants.FONT_FACE_STYLE_PROPERTY, true);
		if (fontFaceValue != null) {
			return ((Integer) fontFaceValue).intValue();
		}
		return Font.FACE_SYSTEM;
	}
	
	/**
	 * @return The font style
	 */
	private int getFontStyle() {
		int fontStyle = Font.STYLE_PLAIN;
		Object fontStyleValue;
		for (Widget widget = this; widget != null; widget = widget.parent) {
			fontStyleValue = widget.getStylePropertyValue(KuixConstants.FONT_STYLE_STYLE_PROPERTY, false);
			if (fontStyleValue != null) {
				fontStyle |= ((Integer) fontStyleValue).intValue();
			}
		}
		return fontStyle;
	}
	
	/**
	 * @return The font size
	 */
	private int getFontSize() {
		Object fontSizeValue = getStylePropertyValue(KuixConstants.FONT_SIZE_STYLE_PROPERTY, true);
		if (fontSizeValue != null) {
			return ((Integer) fontSizeValue).intValue();
		}
		return Font.SIZE_MEDIUM;
	}
	
	/**
	 * @return The font of this {@link Text}
	 */
	protected Font getFont() {
		if (cachedFont == null) {
			cachedFont = Font.getFont(getFontFace(), getFontStyle(), getFontSize());
		}
		return cachedFont;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getDefaultStylePropertyValue(java.lang.String)
	 */
	protected Object getDefaultStylePropertyValue(String name) {
		if (defaultFontFace != null && KuixConstants.FONT_FACE_STYLE_PROPERTY.equals(name)) {
			return defaultFontFace;
		}
		if (defaultFontStyle != null && KuixConstants.FONT_STYLE_STYLE_PROPERTY.equals(name)) {
			return defaultFontStyle;
		}
		if (defaultFontSize != null && KuixConstants.FONT_SIZE_STYLE_PROPERTY.equals(name)) {
			return defaultFontSize;
		}
		return super.getDefaultStylePropertyValue(name);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#clearCachedStyle(boolean)
	 */
	public void clearCachedStyle(boolean propagateToChildren) {
		cachedFont = null;
		super.clearCachedStyle(propagateToChildren);
	}
	
}
