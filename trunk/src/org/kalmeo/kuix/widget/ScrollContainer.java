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

import javax.microedition.lcdui.Graphics;

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.layout.BorderLayout;
import org.kalmeo.kuix.layout.BorderLayoutData;
import org.kalmeo.kuix.layout.InlineLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Gap;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.util.BooleanUtil;
import org.kalmeo.util.MathFP;

/**
 * This class represent a scroll container.
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
 * 		<td> <code>usemarkers</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define if the top and bottom invisible but focusable markers are present. Set this attribute to <code>false</code> if the child of this scrollcontainer is a list or all other widget where its first and last child is focusable. </td>
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
 * 		<td> <code>align</code> </th>
 * 		<td> <code>fill-top</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout</code> </th>
 * 		<td> <code>inlinelayout(false,fill)</code> </td>
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
public class ScrollContainer extends Widget {

	// Defaults
	private static final Layout SCROLLCONTAINER_CONTAINER_LAYOUT = new InlineLayout(false, Alignment.FILL);
	private static final int MAX_INCREMENT_DIVIDER = 4;

	// Inner container widget
	protected Widget container;

	// ScrollBar widget
	private ScrollBar scrollBar;

	// attributes
	private boolean useMarkers = true;
	
	// y offset
	private int yOffset = 0;

	// Internal use
	private int visualHeight;
	private int contentHeight;
	private int maxIncrement;
	private int pressedY = 0;
	private int pressedYOffset = 0;
	
	/**
	 * Construct a {@link ScrollContainer}
	 */
	public ScrollContainer() {
		this(KuixConstants.SCROLL_CONTAINER_WIDGET_TAG);
	}
	
	/**
	 * Construct a {@link ScrollContainer}
	 *
	 * @param tag
	 */
	public ScrollContainer(String tag) {
		super(tag);
		container = new Widget() {
			
			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getDisplayY()
			 */
			public int getDisplayY() {
				return super.getDisplayY() - yOffset;
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayout()
			 */
			public Layout getLayout() {
				return SCROLLCONTAINER_CONTAINER_LAYOUT;
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getAlign()
			 */
			public Alignment getAlign() {
				return Alignment.FILL_TOP;
			}
			
			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getPadding()
			 */
			public Insets getPadding() {
				return (Insets) ScrollContainer.this.getStylePropertyValue(KuixConstants.PADDING_STYLE_PROPERTY, false);
			}
			
			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getGap()
			 */
			public Gap getGap() {
				return (Gap) ScrollContainer.this.getStylePropertyValue(KuixConstants.GAP_STYLE_PROPERTY, false);
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getWidgetAt(int, int)
			 */
			public Widget getWidgetAt(int mx, int my) {
				return getWidgetAt(mx, my + yOffset, getX(), getY(), getWidth(), contentHeight);
			}
			
			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#removeAll()
			 */
			public void removeAll() {
				if (isUseMarkers()) {
					if (getChild() != null) {
						for (Widget widget = getChild().next; widget != null && widget != getLastChild(); widget = widget.next) {
							widget.parent = null;
						}
					}
				} else {
					super.removeAll();
				}
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#doLayout()
			 */
			protected void doLayout() {
				super.doLayout();
				Insets inset = getInsets();
				visualHeight = getHeight() - inset.top - inset.bottom;
				contentHeight = getPreferredSize(getWidth()).height;
				maxIncrement = visualHeight / MAX_INCREMENT_DIVIDER;
				setYOffset(yOffset > contentHeight - container.getHeight() ? contentHeight - container.getHeight() : yOffset); // Adjust yOffset if contentHeight has changed
				updateScrollBarValues();
			}
			
			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#invalidateAppearanceRegion(int, int, int, int)
			 */
			protected void invalidateAppearanceRegion(int x, int y, int width, int height) {
				super.invalidateAppearanceRegion(x + getX(), y + getY() - yOffset, width, height);
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#paintChildrenImpl(javax.microedition.lcdui.Graphics)
			 */
			protected void paintChildrenImpl(Graphics g) {
				g.translate(0, -yOffset);
				super.paintChildrenImpl(g);
				g.translate(0, yOffset);
			}

		};
		super.add(container);
		scrollBar = new ScrollBar() {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getDefaultStylePropertyValue(java.lang.String)
			 */
			protected Object getDefaultStylePropertyValue(String name) {
				if (KuixConstants.LAYOUT_DATA_STYLE_PROPERTY.equals(name)) {
					return BorderLayoutData.instanceEast;
				}
				return super.getDefaultStylePropertyValue(name);
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#processPointerEvent(byte, int, int)
			 */
			public boolean processPointerEvent(byte type, int x, int y) {
				if (type == KuixConstants.POINTER_DRAGGED_EVENT_TYPE) {
					if (getHeight() != 0 && setYOffset(pressedYOffset + (y - pressedY) * contentHeight / getHeight())) {
						updateScrollBarValues();
						return true;
					}
					return false;
				}
				return super.processPointerEvent(type, x, y);
			}

		};
		super.add(scrollBar);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.USE_MARKERS_ATTRIBUTE.equals(name)) {
			useMarkers = BooleanUtil.parseBoolean(value);
			return true;
		}
		return super.setAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return BorderLayout.instance;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getPadding()
	 */
	public Insets getPadding() {
		return DEFAULT_PADDING;
	}
	
	/**
	 * @return <code>true</code> if the top and bottom markers are used
	 */
	public boolean isUseMarkers() {
		return useMarkers;
	}

	/**
	 * @param offset the yOffset to set
	 * @return <code>true</code> if the yOffset value has changed
	 */
	private boolean setYOffset(int offset) {
		if (contentHeight <= visualHeight) {
			yOffset = 0;
			return true;
		}
		int lastYOffset = yOffset;
		yOffset = Math.max(0, Math.min(contentHeight - container.getHeight(), offset));
		return lastYOffset != yOffset;
	}

	/**
	 * Arrange the scroll offset according to the child position
	 * 
	 * @param child
	 * @param useIncrementLimit
	 * @return <code>true</code> the child is visible after scroll, else <code>false</code>
	 */
	public boolean bestScrollToChild(Widget child, boolean useIncrementLimit) {
		int childY = child.getY() - this.container.getInsets().top;
		Widget container;
		for (container = child.parent; container != null && container != this.container; container = container.parent) {
			childY += container.getY();
		}
		if (container == null) {
			// This is not a child of this ScrollContainer
			return true;
		}
		boolean widgetIsVisible = true;
		if (childY < yOffset) {
			int increment = yOffset - childY;
			if (useIncrementLimit && increment > maxIncrement) {
				setYOffset(yOffset - maxIncrement);
				widgetIsVisible = false;
			} else {
				setYOffset(childY);
			}
		} else if (yOffset + visualHeight < childY + child.getHeight()) {
			int increment = childY + child.getHeight() - (yOffset + visualHeight);
			if (useIncrementLimit && increment > maxIncrement) {
				setYOffset(yOffset + maxIncrement);
				widgetIsVisible = false;
			} else {
				setYOffset(childY + child.getHeight() - visualHeight);
			}
		}
		updateScrollBarValues();
		return widgetIsVisible;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		if (container.getChild() == null && isUseMarkers()) {
			container.add(new AbstractFocusableWidget() {});	// TopMarker
			container.add(new AbstractFocusableWidget() {});	// BottomMarker
		}
		return container.add(widget, container.getLastChild(), !isUseMarkers());
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#removeAll()
	 */
	public void removeAll() {
		container.removeAll();
	}

	/**
	 * Apply scroll values to the scrollBar
	 */
	private void updateScrollBarValues() {
		if (contentHeight != 0) {
			scrollBar.setValue(MathFP.div(yOffset, contentHeight));
			scrollBar.setSelection(MathFP.div(container.getHeight(), contentHeight));
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		switch (type) {
			
			case KuixConstants.POINTER_PRESSED_EVENT_TYPE: {
				pressedY = y;
				pressedYOffset = yOffset;
				return true;
			}
			
			case KuixConstants.POINTER_DRAGGED_EVENT_TYPE: {
				if (setYOffset(pressedYOffset - (y - pressedY) * KuixConstants.SCROLL_BOOSTER_FACTOR)) {
					updateScrollBarValues();
					return true;
				}
				return false;
			}
			
		}
		return super.processPointerEvent(type, x, y);
	}

}
