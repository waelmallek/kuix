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

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.KuixConverter;
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.core.style.Style;
import org.kalmeo.kuix.core.style.StyleProperty;
import org.kalmeo.kuix.core.style.StyleSelector;
import org.kalmeo.kuix.layout.InlineLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Color;
import org.kalmeo.kuix.util.Gap;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.kuix.util.Span;
import org.kalmeo.kuix.util.Weight;
import org.kalmeo.util.BooleanUtil;
import org.kalmeo.util.LinkedList;
import org.kalmeo.util.LinkedListItem;
import org.kalmeo.util.MathFP;
import org.kalmeo.util.NumberUtil;
import org.kalmeo.util.LinkedList.LinkedListEnumeration;

/**
 * This class is the base of all Kuix widgets.
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
 * 		<td> <code>id</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> The widget's unique identifier. The value is as string text </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>class</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> The widget's style class. The value is as string text </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>style</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The author style. The value is as string text where each style property/value couple is separated by a ';' and is conforme to style properties syntax. ( ex: <code>color:red;layout:borderlayout</code> ) </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>visible</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The widget visibility. The value is a string boolean ( <code>true or false</code> ). </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>shortcuts</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The widget key pressed shortcuts. The value is a string representing one or more KuixKeyCode ( <code>0, 1, 2, 3, 4, 5, 6, 7, 8, 9, *, #, softleft, softright, up, left, right, down, fire, back, delete</code> ). The multiple keys separator is the '|' </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>pressedshortcuts</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> idem <code>shortcuts</code> </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>repeatedshortcuts</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The widget key repeated shortcuts.  </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>releasedshortcuts</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The widget key released shortcuts.  </td>
 * 	</tr>
 * </table>
 * <br>
 * <table border="1" width="100%" cellpadding="3" cellspacing="0" >
 * 	<tr class="TableHeadingColor">
 * 		<th align="left" colspan="4"> <font size="+2"> Style properties </font> </th>
 * 	</tr>
 * 	<tr>
 * 		<th width="1%"> Property </th>
 * 		<th width="1%"> Default </th>
 * 		<th width="1%"> Inherit </th>
 * 		<th> Description </th>
 *	</tr class="TableRowColor">
 * 	<tr class="TableRowColor">
 * 		<td> <code>align</code> </td>
 * 		<td> <code>top-left</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the content alignment. The value is an alignement string ( <code>top, top-left, left, bottom-left, bottom, bottom-right, right, top-right, fill, fill-top, fill-left, fill-bottom, fill-right</code> ) </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>bg-align</code> </td>
 * 		<td> <code>top-left</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the background image alignment. The value is an alignment like <code>align</code> property. This property is only available if <code>bg-image</code> is defined. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>bg-color</code> </td>
 * 		<td> <code>-</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the background color. The value is a color like <code>color</code> property. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>bg-image</code> </td>
 * 		<td> <code>-</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the background image. The value is an the image file path. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>bg-repeat</code> </td>
 * 		<td> <code>0&nbsp;0</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the background image repeat method. <code>Syntax : &lt;repeatx&gt; &lt;repeaty&gt;</code>. <code>0</code> means infinit. This property is only available if <code>bg-image</code> is defined. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>border</code> </td>
 * 		<td> <code>0&nbsp;0&nbsp;0&nbsp;0</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the border. <code>Syntax : &lt;top&gt; &lt;right&gt; &lt;bottom&gt; &lt;left&gt;</code> </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>border-color</code> </td>
 * 		<td> <code>-</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the border color. The value is a color like <code>color</code> property. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>border-images</code> </td>
 * 		<td> <code>-</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the border images. <code>Syntax : url(&lt;top&gt;) url(&lt;top-right&gt;) url(&lt;right&gt;) url(&lt;bottom-right&gt;) url(&lt;bottom&gt;) url(&lt;bottom-left&gt;) url(&lt;left&gt;) url(&lt;top-left&gt;)</code>. </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>color</code> </td>
 * 		<td> <code>#000000</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> Define the font color. The value is a color string ( <code>red, green, blue, white, black or #RRGGBB</code> ) </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>gap</code> </td>
 * 		<td> <code>0&nbsp;0</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the gap. <code>Syntax : &lt;hgap&gt; &lt;vgap&gt;</code> </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout</code> </td>
 * 		<td> <code>inlinelayout(true,top-left)</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the content layout. The value is a layout tag ( <code>borderlayout, flowlayout(&lt;align&gt;), gridlayout(&lt;cols&gt;,&lt;rows&gt;), inlinelayout(&lt;horizontal&gt;,&lt;align&gt;), staticlayout, tablelayout</code> ) </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout-data</code> </td>
 * 		<td> <code>null</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the widget layout data. The value is a layout data tag ( <code>bld(&lt;align&gt;), sld(&lt;align&gt;,&lt;width&gt;,&lt;height&gt;)</code> ) </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>margin</code> </td>
 * 		<td> <code>0&nbsp;0&nbsp;0&nbsp;0</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the margin. <code>Syntax : &lt;top&gt; &lt;right&gt; &lt;bottom&gt; &lt;left&gt;</code> </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>padding</code> </td>
 * 		<td> <code>0&nbsp;0&nbsp;0&nbsp;0</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the padding. <code>Syntax : &lt;top&gt; &lt;right&gt; &lt;bottom&gt; &lt;left&gt;</code> </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>span</code> </td>
 * 		<td> <code>1&nbsp;1</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the col and row span. <code>Syntax : &lt;colspan&gt; &lt;rowspan&gt;</code> </td>
 * 	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>weight</code> </td>
 * 		<td> <code>0&nbsp;0</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the weight x and y. <code>Syntax : &lt;weightx&gt; &lt;weighty&gt;</code>  </td>
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
 * </table>
 * 
 * @author bbeaulant
 */
public class Widget {
	
	/**
	 * This class represent a bind instruction for a specific attribute.
	 */
	protected class BindInstruction implements LinkedListItem {
		
		private final String attribute;
		private final String[] bindedProperties;
		private final String pattern;
		
		protected BindInstruction previous;
		protected BindInstruction next;
		
		/**
		 * Construct a {@link BindInstruction}
		 *
		 * @param attribute
		 * @param pattern
		 */
		private BindInstruction(String attribute, String[] bindedProperties, String pattern) {
			this.attribute = attribute;
			this.bindedProperties = bindedProperties;
			this.pattern = pattern;
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.util.LinkedListItem#getNext()
		 */
		public LinkedListItem getNext() {
			return next;
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.util.LinkedListItem#getPrevious()
		 */
		public LinkedListItem getPrevious() {
			return previous;
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.util.LinkedListItem#setNext(org.kalmeo.util.LinkedListItem)
		 */
		public void setNext(LinkedListItem next) {
			this.next = (BindInstruction) next;
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.util.LinkedListItem#setPrevious(org.kalmeo.util.LinkedListItem)
		 */
		public void setPrevious(LinkedListItem previous) {
			this.previous = (BindInstruction) previous;
		}
		
		/**
		 * @param property
		 * @return <code>true</code> if given <code>property</code> is
		 *         binded by the instruction.
		 */
		protected boolean hasProperty(String property) {
			for (int i =0; i<bindedProperties.length; ++i) {
				if (bindedProperties[i].equals(property)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Process the bindInstruction
		 */
		protected void process() {
			if (dataProvider != null) {
				if (pattern != null) {
					if (!setAttribute(attribute, Kuix.processI18nPattern(processBindPropertyVariables(pattern)))) {
						throw new IllegalArgumentException(attribute);
					}
				} else if (bindedProperties != null && bindedProperties.length == 1) {
					if (!setObjectAttribute(attribute, dataProvider.getValue(bindedProperties[0]))) {
						throw new IllegalArgumentException(attribute);
					}
				}
			}
		}
		
		/**
		 * Process the bind property variables and replace them by their
		 * values.
		 * <p>Syntax : <code>@{varName[|Null text]}</code></p>
		 * <p>Example with var1="Hello" and var2=null</p>
		 * <p><code>@{var1}</code> is transform to <code>Hello</code></p>
		 * <p><code>@{var1|Nothing}</code> is transform to <code>Hello</code></p>
		 * <p><code>@{var2|Nothing}</code> is transform to <code>Nothing</code></p>
		 * 
		 * @param rawData
		 * @param propertyProvider
		 * @return The processed String
		 */
		private String processBindPropertyVariables(String rawData) {
			int posStart = rawData.indexOf(KuixConstants.BIND_PROPERTY_START_PATTERN);
			if (posStart != -1) {
				int posEnd = rawData.indexOf(KuixConstants.PROPERTY_END_PATTERN, posStart);
				if (posEnd != -1) {
					StringBuffer buffer = new StringBuffer(rawData.substring(0, posStart));
					String variable = rawData.substring(posStart + 2, posEnd);
					String variableValue = null;
					int posPipe = variable.indexOf(KuixConstants.PROPERTY_ALTERNATIVE_SEPRATOR_PATTERN);
					if (posPipe != -1) {
						if (dataProvider != null) {
							variableValue = dataProvider.getStringValue(variable.substring(0, posPipe));
						}
						if (variableValue == null) {
							variableValue = variable.substring(posPipe + 1);
						}
					} else if (dataProvider != null) {
						variableValue = dataProvider.getStringValue(variable);
					}
					if (variableValue != null) {
						buffer.append(variableValue);
					}
					return buffer.append(processBindPropertyVariables(rawData.substring(posEnd + 1))).toString();
				}
			}
			return rawData;
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.util.LinkedListItem#compareTo(org.kalmeo.util.LinkedListItem, int)
		 */
		public int compareTo(LinkedListItem item, int flag) {
			return 0;
		}

	}

	// Defaults
	private static final Layout DEFAULT_LAYOUT = new InlineLayout();
	protected static final Insets DEFAULT_MARGIN = new Insets();
	protected static final Insets DEFAULT_BORDER = new Insets();
	protected static final Insets DEFAULT_PADDING = new Insets();
	protected static final Gap DEFAULT_GAP = new Gap();
	protected static final Span DEFAULT_SPAN = new Span(1, 1);
	protected static final Weight DEFAULT_WEIGHT = new Weight(0, 0);
	protected static final Alignment DEFAULT_ALIGN = Alignment.TOP_LEFT;

	// The widget tag
	private final String tag;
	
	// Id of the widget (default = null)
	private String id;

	// Style Classes of the widget (default = null)
	private String[] styleClasses;

	// The parent widget
	public Widget parent;

	// The privious widget in the linked list
	public Widget previous;

	// The next widget in the linked list
	public Widget next;

	// The first child widget
	private Widget child;
	private Widget lastChild;

	// The x position of the widget
	private int x;

	// The y position of the widget
	private int y;

	// The width of the widget
	private int width;

	// The height of the widget
	private int height;
	
	// Used for focus navigation
	private int visualCenterX;
	private int visualCenterY;

	// The widget style defined by the xml author
	private Style authorStyle;
	
	// Define if the widget will be paint
	private boolean visible = true;
	
	// Shortcuts are represented by a byte array that embed : keyCodes mask (4 bytes), and a list of keyCode (4 bytes) / action (2 + length bytes)
	private byte[] pressedShortcutActions = null;
	private byte[] repeatedShortcutActions = null;
	private byte[] releasedShortcutActions = null;
	
	// List of data binds instructions (key:property / value:attribute)
	protected LinkedList bindInstructions = null;
	
	// Th associated DataProvider (generaly null if no binds defined)
	private DataProvider dataProvider = null;
	
	// Define if the widget need to be relayout
	private boolean invalidated = true;
	
	// Use in constraints computation optimization to know if it's nesesary to recompute widget's preferedSize
	private int lastPreferredWidth = -1;
	
	// The cached objects
	private Vector cachedStyles = null;
	private Insets cachedInsets = null;
	private Metrics cachedMetrics = null;
	
	/**
	 * Construct a {@link Widget}
	 */
	public Widget() {
		this(KuixConstants.DEFAULT_WIDGET_TAG); 
	}
	
	/**
	 * Construct a {@link Widget}
	 * 
	 * @param tag
	 */
	public Widget(String tag) {
		this.tag = tag; 
	}
	
	/**
	 * Returns the first internal child instance corresponding to the given
	 * <code>tag</code>.
	 * 
	 * @param tag
	 * @return the child instance if it exists.
	 */
	public Widget getInternalChildInstance(String tag) {
		return null;
	}

	/**
	 * Set the <code>value</code> to the specified attribute representing by
	 * the <code>name</code>
	 * 
	 * @param name
	 * @param value
	 * @return <code>true</code> if the attribute exists
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.ID_ATTRIBUTE.equals(name)) {
			setId(value);
			return true;
		}
		if (KuixConstants.CLASS_ATTRIBUTE.equals(name)) {
			setStyleClasses(KuixConverter.convertStyleClasses(value));
			return true;
		}
		if (KuixConstants.STYLE_ATTRIBUTE.equals(name)) {
			parseAuthorStyle(value);
			return true;
		}
		if (KuixConstants.VISIBLE_ATTRIBUTE.equals(name)) {
			setVisible(BooleanUtil.parseBoolean(value));
			return true;
		}
		if (KuixConstants.SHORTCUTS_ATTRIBUTE.equals(name) || KuixConstants.PRESSED_SHORTCUTS_ATTRIBUTE.equals(name)) {
			setShortcuts(value, KuixConstants.KEY_PRESSED_EVENT_TYPE);
			return true;
		}
		if (KuixConstants.REPEATED_SHORTCUTS_ATTRIBUTE.equals(name)) {
			setShortcuts(value, KuixConstants.KEY_REPEATED_EVENT_TYPE);
			return true;
		}
		if (KuixConstants.RELEASED_SHORTCUTS_ATTRIBUTE.equals(name)) {
			setShortcuts(value, KuixConstants.KEY_RELEASED_EVENT_TYPE);
			return true;
		}
		return false;
	}
	
	/**
	 * Set the Object <code>value</code> to the specified attribute
	 * representing by the <code>name</code>.
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public boolean setObjectAttribute(String name, Object value) {
		return false;
	}
	
	/**
	 * Check if the given attribute require an Object value.
	 * 
	 * @param name
	 * @return <code>true</code> if the given attribute require an Object
	 *         value
	 */
	public boolean isObjectAttribute(String name) {
		return false;
	}
	
	/**
	 * Returns the value of the specified attribute
	 * 
	 * @param name
	 * @return The value of the specified attribute
	 */
	public Object getAttribute(String name) {
		if (KuixConstants.ID_ATTRIBUTE.equals(name)) {
			return getId();
		}
		if (KuixConstants.DATAPROVIDER_ATTRIBUTE.equals(name)) {
			return getDataProvider();
		}
		return null;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the styleClasses
	 */
	public String[] getStyleClasses() {
		return styleClasses;
	}

	/**
	 * @param styleClasses the styleClasses to set
	 */
	public void setStyleClasses(String[] styleClasses) {
		this.styleClasses = styleClasses;
	}
	
	/**
	 * @param styleClass the styleClass to set
	 */
	public void setStyleClass(String styleClass) {
		if (styleClasses != null && styleClasses.length == 1) {
			styleClasses[0] = styleClass;
		} else {
			styleClasses = new String[] { styleClass };
		}
	}
	
	/**
	 * @return the pseudoClasses list
	 */
	public String[] getAvailablePseudoClasses() {
		return null;
	}

	/**
	 * @return <code>true</code> if this widget is compatible with peudo class
	 */
	public boolean isPseudoClassCompatible(String pseudoClass) {
		return false;
	}

	/**
	 * @return the child
	 */
	public Widget getChild() {
		return child;
	}
	
	/**
	 * @return the lastChild
	 */
	public Widget getLastChild() {
		return lastChild;
	}

	/**
	 * Returns the <code>x</code> coordinate of this widget in its parent
	 * coordinate system.
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the <code>y</code> coordinate of this widget in its parent
	 * coordinate system.
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the <code>x</code> coordinate of this widget in the display
	 * coordinate system.
	 * 
	 * @return the x coordinate
	 */
	public int getDisplayX() {
		return ((parent != null) ? parent.getDisplayX() : 0) + x;
	}
	
	/**
	 * Returns the <code>y</code> coordinate of this widget in the display
	 * coordinate system.
	 * 
	 * @return the y coordinate
	 */
	public int getDisplayY() {
		return ((parent != null) ? parent.getDisplayY() : 0) + y;
	}
	
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the widget's bounds
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void setBounds(int x, int y, int width, int height) {
		if (invalidated || this.x != x || this.y != y || this.width != width || this.height != height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			Insets margin = getMargin();
			visualCenterX = (width - margin.left - margin.right) / 2 + margin.left;
			visualCenterY = (height - margin.top - margin.bottom) / 2 + margin.top;
			doLayout();
		}
	}
	
	/**
	 * @return the authorStyle
	 */
	public Style getAuthorStyle() {
		return authorStyle;
	}

	/**
	 * @param authorStyle the authorStyle to set
	 */
	public void parseAuthorStyle(String rawAuthorStyle) {
		Style[] styles = Kuix.extractStyleSheets(Kuix.getConverter(), getTag(), rawAuthorStyle);
		if (styles.length > 0) {
			this.authorStyle = styles[0];
		}
	}
	
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		if (visible && parent != null) {
			return parent.isVisible();
		}
		return visible;
	}
	
	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/**
	 * @return <code>true</code> if the widget is in widget tree of desktop
	 */
	public boolean isInWidgetTree() {
		if (parent != null) {
			return parent.isInWidgetTree();
		}
		return false;
	}
	
	/**
	 * @return <code>true</code> if one or more data bind instructions are defined
	 */
	public boolean hasBindInstruction() {
		return bindInstructions != null && !bindInstructions.isEmpty();
	}
	
	/**
	 * Set a BindInstruction for the specified <code>attribute</code>.
	 * 
	 * @param attribute
	 * @param properties
	 * @param pattern
	 */
	public void setAttributeBindInstruction(String attribute, String[] properties, String pattern) {
		if (bindInstructions == null) {
			bindInstructions = new LinkedList();
		} else {
			removeAttributeBindInstruction(attribute);
		}
		bindInstructions.add(new BindInstruction(attribute, properties, pattern));
	}
	
	/**
	 * Remove the bindInstruction for a specific attribute
	 * 
	 * @param attribute
	 */
	public void removeAttributeBindInstruction(String attribute) {
		if (hasBindInstruction()) {
			for (BindInstruction bindInstruction = (BindInstruction) bindInstructions.getFirst(); bindInstruction != null; bindInstruction = bindInstruction.next) {
				if (bindInstruction.attribute.equals(attribute)) {
					bindInstructions.remove(bindInstruction);
					return;
				}
			}			
		}
	}
	
	/**
	 * Define the widget's associated shortcuts. A shortcut could be a couple of
	 * <code>kuixKeyCode</code> and <code>action</code>, or simply a
	 * <code>kuixKeyCode</code>. Multiple shortcuts could be assigned to one
	 * widget. This method do not accept directly <code>kuixKeyCode</code> but
	 * only kuix key representation like <code>left</code>, <code>*</code>,
	 * <code>1</code>.
	 * 
	 * @param rawShortcuts a string that contains keys and / or action.
	 * @param eventType the event type. <code>KuixConstants.KEY_PRESSED_EVENT_TYPE,
	 *            KuixConstants.KEY_REPEATED_EVENT_TYPE or
	 *            KuixConstants.KEY_RELEASED_EVENT_TYPE</code>
	 */
	public void setShortcuts(String rawShortcuts, byte eventType) {
		byte[] shortcuts = KuixConverter.convertShortcuts(rawShortcuts);
		switch (eventType) {
			case KuixConstants.KEY_PRESSED_EVENT_TYPE:
				pressedShortcutActions = shortcuts;
				break;
			case KuixConstants.KEY_REPEATED_EVENT_TYPE:
				repeatedShortcutActions = shortcuts;
				break;
			case KuixConstants.KEY_RELEASED_EVENT_TYPE:
				releasedShortcutActions = shortcuts;
				break;
		}
	}
	
	/**
	 * Define the shortcuts key codes intercepted by this widget.<br>
	 * This method override all previous shortcuts and shortcut's actions defined
	 * by <code>setShortcut</code> method.
	 * 
	 * @param shortcutKeyCodes
	 * @param eventType the event type. <code>KuixConstants.KEY_PRESSED_EVENT_TYPE,
	 *            KuixConstants.KEY_REPEATED_EVENT_TYPE or
	 *            KuixConstants.KEY_RELEASED_EVENT_TYPE</code>
	 */
	public void setShortcutKeyCodes(int shortcutKeyCodes, byte eventType) {
		switch (eventType) {
			case KuixConstants.KEY_PRESSED_EVENT_TYPE:
				pressedShortcutActions = NumberUtil.toBytes(shortcutKeyCodes);
				break;
			case KuixConstants.KEY_REPEATED_EVENT_TYPE:
				repeatedShortcutActions = NumberUtil.toBytes(shortcutKeyCodes);
				break;
			case KuixConstants.KEY_RELEASED_EVENT_TYPE:
				releasedShortcutActions = NumberUtil.toBytes(shortcutKeyCodes);
				break;
		}
	}

	/**
	 * @return <code>true</code> if shortcuts are set for all most one event type
	 */
	public boolean hasShortcuts() {
		return (pressedShortcutActions != null || repeatedShortcutActions != null || releasedShortcutActions != null);
	}
	
	/**
	 * @param eventType the event type. <code>KuixConstants.KEY_PRESSED_EVENT_TYPE,
	 *            KuixConstants.KEY_REPEATED_EVENT_TYPE or
	 *            KuixConstants.KEY_RELEASED_EVENT_TYPE</code>
	 * @return <code>true</code> if shortcuts are set for a specified event
	 *         type
	 */
	public boolean hasShortcutKeyCodes(byte eventType) {
		switch (eventType) {
			case KuixConstants.KEY_PRESSED_EVENT_TYPE:
				return pressedShortcutActions != null;
			case KuixConstants.KEY_REPEATED_EVENT_TYPE:
				return repeatedShortcutActions != null;
			case KuixConstants.KEY_RELEASED_EVENT_TYPE:
				return releasedShortcutActions != null;
		}
		return false;
	}

	/**
	 * Check if a <code>kuixKeyCode</code> is compatible with widget's shortcuts.
	 * 
	 * @param kuixKeyCode
	 * @param eventType the event type. <code>KuixConstants.KEY_PRESSED_EVENT_TYPE,
	 *            KuixConstants.KEY_REPEATED_EVENT_TYPE or
	 *            KuixConstants.KEY_RELEASED_EVENT_TYPE</code>
	 * @return <code>true</code> if the given <code>kuixKeyCode</code> is
	 *         compatible with this widget's shortcuts
	 */
	public boolean isShortcutKeyCodeCompatible(int kuixKeyCode, byte eventType) {
		switch (eventType) {
			case KuixConstants.KEY_PRESSED_EVENT_TYPE:
				return (NumberUtil.toInt(pressedShortcutActions, 0) & kuixKeyCode) == kuixKeyCode;
			case KuixConstants.KEY_REPEATED_EVENT_TYPE:
				return (NumberUtil.toInt(repeatedShortcutActions, 0) & kuixKeyCode) == kuixKeyCode;
			case KuixConstants.KEY_RELEASED_EVENT_TYPE:
				return (NumberUtil.toInt(releasedShortcutActions, 0) & kuixKeyCode) == kuixKeyCode;
		}
		return false;
	}
	
	/**
	 * @return the dataProvider
	 */
	public DataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * @param dataProvider the dataProvider to set
	 */
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	/**
	 * @return the layout
	 */
	public Layout getLayout() {
		return (Layout) getStylePropertyValue(KuixConstants.LAYOUT_STYLE_PROPERTY, false);
	}
	
	/**
	 * @return the layoutData
	 */
	public LayoutData getLayoutData() {
		return (LayoutData) getStylePropertyValue(KuixConstants.LAYOUT_DATA_STYLE_PROPERTY, false);
	}
	
	/**
	 * @return the margin
	 */
	public Insets getMargin() {
		return (Insets) getStylePropertyValue(KuixConstants.MARGIN_STYLE_PROPERTY, false);
	}

	/**
	 * @return the border
	 */
	public Insets getBorder() {
		return (Insets) getStylePropertyValue(KuixConstants.BORDER_STYLE_PROPERTY, false);
	}

	/**
	 * @return the padding
	 */
	public Insets getPadding() {
		return (Insets) getStylePropertyValue(KuixConstants.PADDING_STYLE_PROPERTY, false);
	}

	/**
	 * @return The insets
	 */
	public Insets getInsets() {
		Insets margin = getMargin();
		Insets border = getBorder();
		Insets padding = getPadding();
		if (cachedInsets == null) {
			cachedInsets = new Insets();
		}
		cachedInsets.top = margin.top + border.top + padding.top;
		cachedInsets.left = margin.left + border.left + padding.left;
		cachedInsets.bottom = margin.bottom + border.bottom + padding.bottom;
		cachedInsets.right = margin.right + border.right + padding.right;
		return cachedInsets;
	}
	
	/**
	 * @return the gap
	 */
	public Gap getGap() {
		return (Gap) getStylePropertyValue(KuixConstants.GAP_STYLE_PROPERTY, false);
	}

	/**
	 * @return the span
	 */
	public Span getSpan() {
		return (Span) getStylePropertyValue(KuixConstants.SPAN_STYLE_PROPERTY, false);
	}

	/**
	 * @return the weight
	 */
	public Weight getWeight() {
		return (Weight) getStylePropertyValue(KuixConstants.WEIGHT_STYLE_PROPERTY, false);
	}

	/**
	 * @return the alignment
	 */
	public Alignment getAlign() {
		return (Alignment) getStylePropertyValue(KuixConstants.ALIGN_STYLE_PROPERTY, false);
	}

	/**
	 * @return The {@link Desktop}
	 */
	public Desktop getDesktop() {
		return (parent != null) ? parent.getDesktop() : null;
	}

	/**
	 * Returns the {@link Widget} witch correspond to the <code>id</code>, or
	 * <code>null</code>
	 * 
	 * @param id
	 * @return The {@link Widget} witch correspond to the <code>id</code>
	 */
	public Widget getWidget(String id) {
		if (this.id != null && this.id.equals(id)) {
			return this;
		}
		for (Widget child = this.child; child != null; child = child.next) {
			Widget widget = child.getWidget(id);
			if (widget != null) {
				return widget;
			}
		}
		return null;
	}

	/**
	 * Returns the child widget under mx, my point
	 * 
	 * @param mx
	 * @param my
	 * @return the child {@link Widget} under mx, my point
	 */
	public Widget getWidgetAt(int mx, int my) {
		return getWidgetAt(mx, my, x, y, width, height);
	}
	
	/**
	 * Returns the child widget under mx, my point and specify the x, y, width and height of search
	 * 
	 * @param mx
	 * @param my
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return the child {@link Widget} under mx, my point
	 */
	public Widget getWidgetAt(int mx, int my, int x, int y, int width, int height) {
		// Do not use 'isVisible()' instead of 'visible' because of recurcive call of getWidgetAt. 
		// But calling this method if widget's parent is not visible could return wrong.
		if (visible && (mx >= x) && (my >= y) && (mx < x + width) && (my < y + height)) {
			Widget inside = getWidgetAt(getChild(), mx - x, my - y);
			return (inside != null) ? inside : this;
		}
		return null;
	}

	/**
	 * Returns the next child widget under mx, my point
	 * 
	 * @param widget
	 * @param mx
	 * @param my
	 * @return the next child widget under mx, my point
	 */
	private Widget getWidgetAt(Widget widget, int mx, int my) {
		if (widget == null) {
			return null;
		}
		Widget inside = getWidgetAt(widget.next, mx, my);
		return (inside != null) ? inside : widget.getWidgetAt(mx, my);
	}

	/**
	 * Check if this {@link Widget} is focusable.
	 * 
	 * @return <code>true</code> if the widget is focusable
	 */
	public boolean isFocusable() {
		return false;
	}

	/**
	 * Check if this {@link Widget} is focused.
	 * 
	 * @return <code>true</code> is the widget is focused
	 */
	public boolean isFocused() {
		return false;
	}
	
	/**
	 * Check if this {@link Widget} is a child of a focused widget.
	 * 
	 * @return <code>true</code> if this {@link Widget} is a child of a
	 *         focused widget
	 */
	public boolean isFocusWidgetChild() {
		if (!isFocused() && parent != null) {
			return parent.isFocusWidgetChild();
		}
		return isFocused();
	}

	/**
	 * Used for popup like widgets
	 * 
	 * @return The special FocusManager
	 */
	public FocusManager getFocusManager() {
		return (parent != null) ? parent.getFocusManager() : null;
	}
	
	/**
	 * Returns the prefered size of this {@link Widget}
	 * 
	 * @param preferredWidth
	 * @return The prefered size of this {@link Widget}
	 */
	public Metrics getPreferredSize(int preferredWidth) {
		Metrics metrics = getCachedMetrics();
		if (needToComputePreferredSize(preferredWidth)) {
			Layout layout = getLayout();
			if (layout == null) {
				Insets insets = getInsets();
				metrics.width = insets.left + insets.right;
				metrics.height = insets.top + insets.bottom;
			} else {
				layout.measurePreferredSize(this, preferredWidth, metrics);
			}
			lastPreferredWidth = preferredWidth;
		}
		return metrics;
	}
	
	/**
	 * @param preferredWidth
	 * @return <code>true</code> if the preferredSize computation is needed
	 */
	protected boolean needToComputePreferredSize(int preferredWidth) {
		return invalidated || preferredWidth != lastPreferredWidth;
	}

	/**
	 * @return The cached {@link Metrics}
	 */
	protected Metrics getCachedMetrics() {
		if (cachedMetrics == null) {
			cachedMetrics = new Metrics(this);
		}
		cachedMetrics.next = null;	// To be sure the last one is not linked to an other
		return cachedMetrics;
	}
	
	/**
	 * Add a {@link Widget} to this as child
	 * 
	 * @param widget The {@link Widget} to add
	 * @return This {@link Widget}
	 */
	public Widget add(Widget widget) {
		return add(widget, lastChild, true);
	}
	
	/**
	 * Add a {@link Widget} to this as child before or after
	 * <code>referenceWidget</code> child.
	 * The referenceWidget need to be a child of this widget.
	 * 
	 * @param widget
	 * @param referenceWidget
	 * @param after
	 * @return This {@link Widget}
	 */
	public Widget add(Widget widget, Widget referenceWidget, boolean after) {
		if (referenceWidget != null && referenceWidget.parent != this) {
			// The referenceWidget need to be a child of this widget
			return this;
		}
		if (lastChild == null) {
			lastChild = child = widget;
		} else {
			if (referenceWidget == null) {
				// by default the widget is appended
				referenceWidget = lastChild;
				after = true;
			}
			if (after) {
				Widget next = referenceWidget.next;
				referenceWidget.next = widget;
				widget.previous = referenceWidget;
				widget.next = next;
				if (next != null) {
					next.previous = widget;
				} else {
					lastChild = widget;
				}
			} else {
				Widget previous = referenceWidget.previous;
				referenceWidget.previous = widget;
				widget.previous = previous;
				widget.next = referenceWidget;
				if (previous != null) {
					previous.next = widget;
				} else {
					child = widget;
				}
			}
		}
		if (widget.parent != null) {
			widget.remove();
		}
		widget.parent = this;
		if (widget.hasShortcuts()) {
			FocusManager focusManager = widget.getFocusManager();
			if (focusManager != null) {
				focusManager.addShortcutHandler(widget);
			}
		}
		invalidate();
		onChildAdded(widget);
		widget.onAdded(this);
		return this;
	}
	
	/**
	 * Bring the <code>widget</code> to the front of the orthers.
	 * 
	 * @param widget
	 */
	public void bringToFront(Widget widget) {
		if (widget != null && widget.parent == this && child != widget) {
			
			// Remove from previous depth
			extractWidgetFromWidgetTree(widget);
			
			// Bring to front
			child.previous = widget;
			widget.next = child;
			child = widget;
			
			invalidate();
			
		}
	}
	
	/**
	 * Bring the <code>widget</code> to the back of the others.
	 * 
	 * @param widget
	 */
	public void bringToBack(Widget widget) {
		if (widget != null && widget.parent == this && lastChild != widget) {
			
			// Remove from previous depth
			extractWidgetFromWidgetTree(widget);
			
			// Bring to back
			lastChild.next = widget;
			widget.previous = lastChild;
			lastChild = widget;
			
			invalidate();
			
		}
	}
	
	/**
	 * @param widget
	 * @param referenceWidget
	 * @param after
	 */
	public void bringNear(Widget widget, Widget referenceWidget, boolean after) {
		if (widget == null || referenceWidget == null
				|| widget.parent != this 
				|| referenceWidget.parent != this
				|| after && referenceWidget.next == widget
				|| !after && referenceWidget.previous == widget) {
			return;
		}
		
		// Remove from previous depth
		extractWidgetFromWidgetTree(widget);
		
		// Add to new depth
		if (after) {
			Widget next = referenceWidget.next;
			referenceWidget.next = widget;
			widget.previous = referenceWidget;
			widget.next = next;
			if (next != null) {
				next.previous = widget;
			} else {
				lastChild = widget;
			}
		} else {
			Widget previous = referenceWidget.previous;
			referenceWidget.previous = widget;
			widget.previous = previous;
			widget.next = referenceWidget;
			if (previous != null) {
				previous.next = widget;
			} else {
				child = widget;
			}
		}
		invalidate();
	}

	/**
	 * Internal use only method.
	 * 
	 * @param widget
	 */
	private void extractWidgetFromWidgetTree(Widget widget) {
		if (child == widget) {
			child = widget.next;
		}
		if (lastChild == widget) {
			lastChild = widget.previous;
		}
		if (widget.previous != null) {
			widget.previous.next = widget.next;
		}
		if (widget.next != null) {
			widget.next.previous = widget.previous;
			widget.next = null;
		}
		widget.previous = null;
	}
	
	/**
	 * Remove the current {@link Widget} from its parent.<br/><b>Caution</b> :
	 * this method do NOT cleanUp the widgets. It only remove widget from widget
	 * tree.
	 */
	public void remove() {
		if (parent == null) {
			return;
		}
		if (parent.child == this) {
			parent.child = next;
		}
		if (parent.lastChild == this) {
			parent.lastChild = previous;
		}
		if (previous != null) {
			previous.next = next;
		}
		if (next != null) {
			next.previous = previous;
			next = null;
		}
		previous = null;
		invalidate();	// Caution : This call should stay before parent = null;
		Widget prevParent = parent;
		parent = null;
		
		// Call remove events
		prevParent.onChildRemoved(this);
		onRemoved(prevParent);
		
		System.gc();
	}

	/**
	 * Remove all childs. <br/> <b>Caution</b> : this method do NOT cleanUp
	 * child widgets. It only remove widgets from widget tree.
	 */
	public void removeAll() {
		if (child != null) {
			Widget focusedWidget = null;
			FocusManager focusManager = getFocusManager();
			if (focusManager != null) {
				focusedWidget = focusManager.getFocusedWidget();
			}
			for (Widget widget = child; widget != null; widget = widget.next) {
				widget.next = widget.previous = widget.parent = null;
				if (widget == focusedWidget) {
					focusManager.requestFocus(null);
				}
				onChildRemoved(widget);
				widget.onRemoved(this);
			}
			child = null;
			lastChild = null;
			invalidate();
		}
		System.gc();
	}
	
	/**
	 * CleanUp all widget's link.<br/><b>Caution</b> : This method do NOT
	 * remove the widget from widget tree. It only clean external references
	 * like dataBinding, menu cache, etc...
	 */
	public void cleanUp() {
		
		// Remove data bindings
		if (dataProvider != null) {
			dataProvider.unbind(this);
		}
		
		// Propagate cleanUp to all children
		cleanUpAll();
		
	}
	
	/**
	 * CleanUp all children.<br/><b>Caution</b> : This method do NOT remove
	 * child widgets from widget tree. It only clean external references like
	 * dataBinding, etc...
	 */
	public void cleanUpAll() {
		// dispose to children
		for (Widget widget = child; widget != null; widget = widget.next) {
			widget.cleanUp();
		}
	}

	/**
	 * Invalidate the widget and propagate the information to its parent
	 */
	public void invalidate() {
		invalidated = true;
		if (parent != null && !parent.invalidated) {
			parent.invalidate();
		}
	}
	
	/**
	 * Mark this widget as validate
	 */
	protected void markAsValidate() {
		invalidated = false;
	}

	/**
	 * Layout the widget if its layout is defined.
	 */
	protected void doLayout() {
		Layout layout = getLayout();
		if (layout != null) {
			layout.doLayout(this);
		}
		markAsValidate();
	}
	
	/**
	 * Invalidate full widget's region.
	 */
	public void invalidateAppearance() {
		invalidateAppearanceRegion(0, 0, width, height);
	}
	
	/**
	 * Invalidate a specific region. The given region is translated by this
	 * widget x and y coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void invalidateAppearanceRegion(int x, int y, int width, int height) {
		if (parent != null) {
			parent.invalidateAppearanceRegion(x + this.x, y + this.y, width, height);
		}
	}
	
	/**
	 * Paint the widget.
	 * 
	 * @param g
	 */
	public void paint(Graphics g) {
		paintBackground(g);
		paintBorder(g);
	}

	/**
	 * Paint the background of this {@link Widget}
	 * 
	 * @param g
	 */
	public void paintBackground(Graphics g) {
		Insets margin = getMargin();
		Insets border = getBorder();
		drawBackground(	g, 
						margin.left + border.left, 
						margin.top + border.top, 
						getWidth() - margin.left - border.left - border.right - margin.right, 
						getHeight() - margin.top - border.top - border.bottom - margin.bottom);
	}

	/**
	 * Draw the background
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void drawBackground(Graphics g, int x, int y, int width, int height) {
		
		// Background Color
		Object backgroundColorValue = getStylePropertyValue(KuixConstants.BACKGROUND_COLOR_STYLE_PROPERTY, false);
		if (backgroundColorValue != null) {
			g.setColor(((Color) backgroundColorValue).getRGB());
			g.fillRect(x, y, width, height);
		}
		
		// Background Image
		Object backgroundImageValue = getStylePropertyValue(KuixConstants.BACKGROUND_IMAGE_STYLE_PROPERTY, false);
		if (backgroundImageValue != null) {
			Alignment alignment = Alignment.TOP_LEFT;
			Object backgroundAlignValue = getStylePropertyValue(KuixConstants.BACKGROUND_ALIGN_STYLE_PROPERTY, false);
			if (backgroundAlignValue != null) {
				alignment = (Alignment) backgroundAlignValue;
			}
			int repeatX = Integer.MAX_VALUE;
			int repeatY = Integer.MAX_VALUE;
			Object backgroundRepeatValue = getStylePropertyValue(KuixConstants.BACKGROUND_REPEAT_STYLE_PROPERTY, false);
			if (backgroundRepeatValue != null) {
				int[] backgroundRepeat = (int[]) backgroundRepeatValue;
				if ((backgroundRepeat[0] > 0)) {
					repeatX = backgroundRepeat[0];
				}
				if ((backgroundRepeat[1] > 0)) {
					repeatY = backgroundRepeat[1];
				}
			}
			
			paintMosaicImage(g, (Image) backgroundImageValue, x, y, width, height, alignment, repeatX, repeatY);
		}
		
	}

	/**
	 * Paint the border of this {@link Widget}
	 * 
	 * @param g
	 */
	public void paintBorder(Graphics g) {
		Insets margin = getMargin();
		drawBorder(	g, 
					margin.left, 
					margin.top, 
					getWidth() - margin.left - margin.right, 
					getHeight() - margin.top - margin.bottom);
	}

	/**
	 * Draw the border
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void drawBorder(Graphics g, int x, int y, int width, int height) {
		
		// Border Color
		Object borderColorValue = getStylePropertyValue(KuixConstants.BORDER_COLOR_STYLE_PROPERTY, false);
		if (borderColorValue != null) {
			g.setColor(((Color) borderColorValue).getRGB());
			Insets border = getBorder();
			if (border.top == 1 && border.left == 1 && border.bottom == 1 && border.right == 1) {
				Object strokeValue = getStylePropertyValue(KuixConstants.BORDER_STROKE_STYLE_PROPERTY, false);
				if (strokeValue != null) {
					g.setStrokeStyle(((Integer) strokeValue).intValue());
				} else {
					g.setStrokeStyle(Graphics.SOLID);
				}
				g.drawRect(x, y, width - 1, height - 1);
			} else {
				g.fillRect(x, y, border.left, height);
				g.fillRect(x, y, width, border.top);
				g.fillRect(x, y + height - border.bottom, width, border.bottom);
				g.fillRect(x + width - border.right, y, border.right, height);
			}
		}
		
		// Border Images
		Object borderImagesValue = getStylePropertyValue(KuixConstants.BORDER_IMAGES_STYLE_PROPERTY, false);
		if (borderImagesValue != null) {
			
			Insets border = getBorder();
			Image[] images = (Image[]) borderImagesValue;
			
			// Top
			if (images[0] != null) {
				paintMosaicImage(g, images[0], x + border.left, y, width - border.left - border.right, border.top);
			}
			
			// Top right
			if (images[1] != null) {
				paintMosaicImage(g, images[1], x + width - border.right, y, border.right, border.top);
			}
			
			// Right
			if (images[2] != null) {
				paintMosaicImage(g, images[2], x + width - border.right, y + border.top, border.right, height - border.top - border.bottom);
			}
			
			// Bottom right
			if (images[3] != null) {
				paintMosaicImage(g, images[3], x + width - border.right, y + height - border.bottom, border.right, border.bottom);
			}
			
			// Bottom
			if (images[4] != null) {
				paintMosaicImage(g, images[4], x + border.left, y + height - border.bottom, width - border.left - border.right, border.bottom);
			}
			
			// Bottom left
			if (images[5] != null) {
				paintMosaicImage(g, images[5], x, y + height - border.bottom, border.left, border.bottom);
			}
			
			// Left
			if (images[6] != null) {
				paintMosaicImage(g, images[6], x, y + border.top, border.left, height - border.top - border.bottom);
			}
			
			// Top left
			if (images[7] != null) {
				paintMosaicImage(g, images[7], x, y, border.left, border.top);
			}
			
		}
		
	}
	
	/**
	 * Paint a cliped zone with mosaic image
	 * 
	 * @param g
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	protected void paintMosaicImage(Graphics g, Image image, int x, int y, int width, int height) {
		paintMosaicImage(g, image, x, y, width, height, Alignment.TOP_LEFT, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}
	
	/**
	 * Paint a cliped zone with mosaic image
	 * 
	 * @param g
	 * @param image
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param alignment
	 * @param repeatX
	 * @param repeatY
	 */
	protected void paintMosaicImage(Graphics g, Image image, int x, int y, int width, int height, Alignment alignment, int repeatX, int repeatY) {
		
		// Save current clip attributes
		int currentClipX1 = g.getClipX();
		int currentClipX2 = currentClipX1 + g.getClipWidth();
		int currentClipY1 = g.getClipY();
		int currentClipY2 = currentClipY1 + g.getClipHeight();
		
		// Intersect clip rectangles
		int clipX1 = x;
		int clipX2 = x + width;
		int clipY1 = y;
		int clipY2 = y + height;
		if (clipX1 < currentClipX1) clipX1 = currentClipX1;
		if (clipY1 < currentClipY1) clipY1 = currentClipY1;
		if (clipX2 > currentClipX2) clipX2 = currentClipX2;
		if (clipY2 > currentClipY2) clipY2 = currentClipY2;
		
		// Define clip
		g.setClip(	clipX1, 
					clipY1, 
					clipX2 - clipX1, 
					clipY2 - clipY1);
		
		// Draw images
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		int imax = Math.min(repeatX, MathFP.toInt(MathFP.ceil(MathFP.div(width, imageWidth))));
		int jmax = Math.min(repeatY, MathFP.toInt(MathFP.ceil(MathFP.div(height, imageHeight))));
		byte dx = 1;
		byte dy = 1;
		
		if (alignment.isVerticalCenter()) {
			y += (height - imageHeight * jmax) / 2;
		} else if (alignment.isBottom()) {
			y += height - imageHeight;
			dy = -1;
		}
		
		if (alignment.isHorizontalCenter()) {
			x += (width - imageWidth * imax) / 2;
		} else if (alignment.isRight()) {
			x += width - imageWidth;
			dx = -1;
		}
		
		for (int i = 0; i<imax; ++i) {
			for (int j = 0; j<jmax; ++j) {
				g.drawImage(	image, 
								x + i * imageWidth * dx, 
								y + j * imageHeight * dy, 
								Graphics.SOLID);
			}				
		}
		
		// Restore widget clip
		g.setClip(	currentClipX1, 
					currentClipY1, 
					currentClipX2 - currentClipX1, 
					currentClipY2 - currentClipY1);
		
	}
	
	/**
	 * Paint the implementation of the widget
	 * 
	 * @param g
	 */
	public void paintImpl(Graphics g) {
		if (!visible) {	// Do not use 'isVisible()' instead of 'visible' because of recurcive call of paintImpl. 
			return;
		}
		int clipWidth = g.getClipWidth();
		int clipHeight = g.getClipHeight();
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		if ((width > 0) && (height > 0)) {
			if ((clipX + clipWidth <= x) || (clipX >= x + width) || (clipY + clipHeight <= y) || (clipY >= y + height)) {
				// All regions outside the clip rect region are ignored
				return;
			}
		}
		g.translate(x, y);
		
		// Paint itself
		if ((width > 0) && (height > 0)) {
			g.clipRect(0, 0, width, height);
		}
		paint(g);
		
		// Paint children
		Insets insets = getInsets();
		g.clipRect(insets.left, insets.top, width - insets.left - insets.right, height - insets.top - insets.bottom);
		paintChildrenImpl(g);
		
		g.translate(-x, -y);
		g.setClip(clipX, clipY, clipWidth, clipHeight);
	}
	
	/**
	 * Call the <code>paintImpl</code> method on widget's children.
	 * 
	 * @param g
	 */
	protected void paintChildrenImpl(Graphics g) {
		for (Widget widget = child; widget != null; widget = widget.next) {
			widget.paintImpl(g);
		}
	}

	/**
	 * Returns the list of {@link Style} that correspond to this {@link Widget}
	 * or <code>null</code> if no style is registered for this widget
	 * 
	 * @return The list of {@link Style} that correspond to this {@link Widget}
	 */
	protected Vector getStyles() {
		if (cachedStyles == null) {
			cachedStyles = Kuix.getStyles(this);
		}
		return cachedStyles;
	}

	/**
	 * Clear the cachedStyle
	 */
	public void clearCachedStyle() {
		cachedStyles = null;
		invalidate();
	}

	/**
	 * Return the specified style property value repr�senting by the
	 * <code>name</code>, or <code>null</code>.
	 * 
	 * @param name
	 * @param inherited Specify if the property value is inherited from widget's
	 *            parent
	 * @return The specified style property value
	 */
	protected Object getStylePropertyValue(String name, boolean inherited) {
		Vector styles = getStyles();
		if (styles != null) {
			for (int i = 0; i < styles.size(); ++i) {
				Style style = (Style) styles.elementAt(i);
				
				if (!checkStyleCompatibility(style)) {
					continue;
				}
				
				StyleProperty styleAttribute = style.getProperty(name);
				if (styleAttribute != null) {
					Object value = styleAttribute.getValue();
					if (value != null) {
						return value;
					}
					return getDefaultStylePropertyValue(name);
				}
			}
		}
		if (inherited && parent != null) {
			Object parentStyleProperty = parent.getStylePropertyValue(name, inherited);
			if (parentStyleProperty != null) {
				return parentStyleProperty;
			}
		}
		return getDefaultStylePropertyValue(name);
	}

	/**
	 * Returns the de default style property value for <code>name</code>
	 * property, or <code>null</code>.
	 * 
	 * @param name
	 * @return The de default style property value for <code>name</code>
	 *         property
	 */
	protected Object getDefaultStylePropertyValue(String name) {
		if (KuixConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_LAYOUT;
		}
		if (KuixConstants.MARGIN_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_MARGIN;
		}
		if (KuixConstants.BORDER_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_BORDER;
		}
		if (KuixConstants.PADDING_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_PADDING;
		}
		if (KuixConstants.GAP_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_GAP;
		}
		if (KuixConstants.SPAN_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_SPAN;
		}
		if (KuixConstants.WEIGHT_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_WEIGHT;
		}
		if (KuixConstants.ALIGN_STYLE_PROPERTY.equals(name)) {
			return DEFAULT_ALIGN;
		}
		if (KuixConstants.COLOR_STYLE_PROPERTY.equals(name)) {
			return Color.BLACK;
		}
		return null;
	}
	
	/**
	 * @param style
	 * @return <code>true</code> if the {@link Style} is compatible with this widget
	 */
	private boolean checkStyleCompatibility(Style style) {
		Widget widget = this;
		for (StyleSelector selector = style.getSelector(); selector != null; selector = selector.parent) {
			widget = getCompatibleWidget(selector, widget);
			if (widget == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param selector
	 * @param widget
	 * @return The parent compatible widget with the style selector
	 */
	private Widget getCompatibleWidget(StyleSelector selector, Widget widget) {
		for (; widget != null; widget = widget.parent) {
			if (selector.hasTag()) {
				if (!selector.getTag().equals(widget.getTag())) {
					continue;
				}
			}
			if (selector.hasId()) {
				if (!selector.getId().equals(widget.getId())) {
					continue;
				}
			}
			if (selector.hasClass()) {
				String[] styleClasses = widget.getStyleClasses();
				if (styleClasses != null) {
					boolean isCompatible = false;
					for (int i = 0; i<styleClasses.length; ++i) {
						if (selector.getStyleClass().equals(styleClasses[i])) {
							isCompatible = true;
							break;
						}
					}
					if (!isCompatible) {
						continue;
					}
				} else {
					continue;
				}
			}
			if (selector.hasPseudoClass()) {
				String[] pseudoClasses = selector.getPseudoClasses();
				int i = pseudoClasses.length - 1;
				for (; i>=0; --i) {
					if (!widget.isPseudoClassCompatible(pseudoClasses[i])) {
						break;
					}
				}
				if (i != -1) {
					continue;
				}
			}
			return widget;
		}
		return null;
	}

	/**
	 * Requests the focus of this {@link Widget}
	 */
	public void requestFocus() {
	}

	/**
	 * Requests the focus of the backward focusable {@link Widget}. Starts from this.
	 */
	public void requestBackwardFocus() {
		FocusManager focusManager = getFocusManager();
		if (focusManager != null) {
			focusManager.requestOtherFocus(this, false, null);
		}
	}
	
	/**
	 * Requests the focus of the forward focusable {@link Widget}. Starts from this.
	 */
	public void requestForwardFocus() {
		FocusManager focusManager = getFocusManager();
		if (focusManager != null) {
			focusManager.requestOtherFocus(this, true, null);
		}
	}
	
	/**
	 * Returns the next focusable {@link Widget}
	 * 
	 * @return The next focusable {@link Widget}
	 */
	public Widget getOtherFocus(Widget root, Widget focusedWidget, Widget nearestFocusableWidget, boolean forward, Alignment direction, boolean checkChild, boolean checkParent) {
		boolean isVisible = isVisible();
		if (root != this && !isFocused() && isFocusable() && isVisible) {
			if (focusedWidget == null || direction == null) {
				return this;
			}
			if (isNearest(focusedWidget, nearestFocusableWidget, direction)) {
				nearestFocusableWidget = this;
			}
		} else if (checkChild && isVisible) {
			// Children scan
			Widget childWidget = forward ? child : lastChild;
			if (childWidget != null) {
				nearestFocusableWidget = childWidget.getOtherFocus(root, focusedWidget, nearestFocusableWidget, forward, direction, true, false);
				if ((focusedWidget == null || direction == null) && nearestFocusableWidget != null) {
					return nearestFocusableWidget;
				}
			}
		}
		if (root != this) {
			// Brother scan
			Widget otherWidget = forward ? next : previous;
			if (otherWidget != null) {
				nearestFocusableWidget = otherWidget.getOtherFocus(root, focusedWidget, nearestFocusableWidget, forward, direction, true, false);
				if ((focusedWidget == null || direction == null) && nearestFocusableWidget != null) {
					return nearestFocusableWidget;
				}
			}
			// Parent scan
			if (checkParent && parent != null && isVisible) {
				nearestFocusableWidget = parent.getOtherFocus(root, focusedWidget, nearestFocusableWidget, forward, direction, false, true);
				if ((focusedWidget == null || direction == null) && nearestFocusableWidget != null) {
					return nearestFocusableWidget;
				}
			}
		}
		return nearestFocusableWidget;
	}
	
	/**
	 * @param originWidget
	 * @param nearestWidget
	 * @param direction
	 * @return <code>true</code> if this widget is nearest
	 *         <code>originWidget</code> than <code>bestWidget</code>
	 *         according to <code>direction</code>.
	 */
	private boolean isNearest(Widget originWidget, Widget nearestWidget, Alignment direction) {
		if (originWidget != null && direction != null) {
			int origX = originWidget.getDisplayX() + originWidget.visualCenterX;
			int origY = originWidget.getDisplayY() + originWidget.visualCenterY;
			int dx = getDisplayX() + visualCenterX - origX;
			int dy = getDisplayY() + visualCenterY - origY;
			if (nearestWidget != null) {
				int nearestDx = nearestWidget.getDisplayX() + nearestWidget.visualCenterX - origX;
				int nearestDy = nearestWidget.getDisplayY() + nearestWidget.visualCenterY - origY;
				if (direction.isTop() || direction.isBottom()) {
					if (Math.abs(dy) <= Math.abs(nearestDy) && dy != 0 && (direction.isBottom() && dy > 0 || direction.isTop() && dy < 0)) {
						if (Math.abs(dx) <= Math.abs(nearestDx)) {
							return Math.abs(dx) + Math.abs(dy) < Math.abs(nearestDx) + Math.abs(nearestDy);
						}
					}
				} else {
					if (Math.abs(dx) <= Math.abs(nearestDx) && dx != 0 && (direction.isRight() && dx > 0 || direction.isLeft() && dx < 0)) {
						if (Math.abs(dy) <= Math.abs(nearestDy)) {
							return Math.abs(dx) + Math.abs(dy) < Math.abs(nearestDx) + Math.abs(nearestDy);
						}
					}
				}
			} else {
				if (direction.isTop() || direction.isBottom()) {
					return dy != 0 && (direction.isBottom() && dy > 0 || direction.isTop() && dy < 0);
				} else {
					return dx != 0 && (direction.isRight() && dx > 0 || direction.isLeft() && dx < 0);
				}
			}
		}
		return false;
	}
	
	/**
	 * Process a key event
	 * 
	 * @param type
	 * @param kuixKeyCode
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		return false;
	}
	
	/**
	 * Process a shortcut key event
	 * 
	 * @param type
	 * @param kuixKeyCode
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processShortcutKeyEvent(byte type, int kuixKeyCode) {
		if (isShortcutKeyCodeCompatible(kuixKeyCode, type)) {
			byte[] shortcutActions = null;
			switch (type) {
				case KuixConstants.KEY_PRESSED_EVENT_TYPE:
					shortcutActions = pressedShortcutActions;
					break;
				case KuixConstants.KEY_REPEATED_EVENT_TYPE:
					shortcutActions = repeatedShortcutActions;
					break;
				case KuixConstants.KEY_RELEASED_EVENT_TYPE:
					shortcutActions = releasedShortcutActions;
					break;
			}
			if (shortcutActions != null && shortcutActions.length > 4) {
				int index = 4;
				int keyCode;
				while (index < shortcutActions.length) {
					keyCode = NumberUtil.toInt(shortcutActions, index);
					index += 4;
					int actionLength = NumberUtil.toShort(shortcutActions, index);
					index += 2;
					String action = new String(shortcutActions, index, actionLength);
					if (keyCode == kuixKeyCode) {
						// Action found, execute
						Kuix.callActionMethod(Kuix.parseMethod(action, this, getDesktop()));
						return true;
					}
					index += actionLength;
				}
			}
		}
		return false;
	}
	
	/**
	 * Process a pointer event
	 * 
	 * @param type
	 * @param y
	 * @param y
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		if (parent != null) {
			return parent.processPointerEvent(type, x, y);	// By default a widget does not handle pointer events
		}
		return false;
	}
	
	/**
	 * Process a focus event
	 * 
	 * @param type
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processFocusEvent(byte type) {
		return false;
	}
	
	/**
	 * Process action event
	 * 
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processActionEvent() {
		return false;
	}
	
	/**
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processDataBindEvent() {
		boolean success = false;
		if (dataProvider != null && hasBindInstruction()) {
			for (BindInstruction bindInstruction = (BindInstruction) bindInstructions.getFirst(); bindInstruction != null; bindInstruction = bindInstruction.next) {
				bindInstruction.process();
			}
			invalidate();
			success = true;
		}
		return success;
	}
	
	/**
	 * Process a model update event.
	 * 
	 * @param property
	 * 
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processModelUpdateEvent(String property) {
		boolean success = false;
		if (dataProvider != null && hasBindInstruction()) {
			for (BindInstruction bindInstruction = (BindInstruction) bindInstructions.getFirst(); bindInstruction != null; bindInstruction = bindInstruction.next) {
				if (bindInstruction.hasProperty(property)) {
					bindInstruction.process();
					invalidate();
					success = true;
				}
			}
		}
		return success;
	}
	
	/**
	 * Process an items model update event.
	 * 
	 * @param type
	 * @param property
	 * @param item
	 * @param itemsEnumeration
	 * 
	 * @return <code>true</code> if the event is treated by the widget
	 */
	public boolean processItemsModelUpdateEvent(byte type, String property, DataProvider item, LinkedListEnumeration itemsEnumeration) {
		return false;
	}
	
	/**
	 * Propagate focus lost or gain event to widget's children.
	 * 
	 * @param focusedWidget
	 * @param lost
	 */
	protected void propagateFocusEvent(Widget focusedWidget, boolean lost) {
		if (lost) {
			onLostFocus(focusedWidget);
		} else {
			onFocus(focusedWidget);
		}
		for (Widget widget = child; widget != null; widget = widget.next) {
			widget.propagateFocusEvent(focusedWidget, lost);
		}
	}
	
	/**
	 * Event called when the <code>widjet</code> is added to <code>parent</code>.
	 */
	protected void onAdded(Widget parent) {
	}
	
	/**
	 * Event called when the <code>widjet</code> is removed from <code>parent</code>.
	 */
	protected void onRemoved(Widget parent) {
	}
	
	/**
	 * Event called when the child <code>widjet</code> is added.
	 * 
	 * @param widget The widget child witch is added
	 */
	protected void onChildAdded(Widget widget) {
	}

	/**
	 * Event called when the child <code>widjet</code> is removed.
	 * 
	 * @param widget The widget child witch is removed
	 */
	protected void onChildRemoved(Widget widget) {
	}
	
	/**
	 * Call when the widget or one of its parents gain the focus.
	 * 
	 * @param focusedWidget
	 */
	protected void onFocus(Widget focusedWidget) {
	}
	
	/**
	 * Call when the widget or one of its parents lost the focus.
	 * 
	 * @param focusedWidget
	 */
	protected void onLostFocus(Widget focusedWidget) {
	}
	
}
