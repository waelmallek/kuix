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

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Color;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.util.worker.Worker;
import org.kalmeo.util.worker.WorkerTask;

/**
 * This class represent a text.
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
 * 		<td colspan="5"> Inherited attributes : see {@link AbstractTextWidget} </td>
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
 * 		<td> <code>null</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="4"> Inherited style properties : see {@link AbstractTextWidget} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link AbstractTextWidget} </td>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link AbstractTextWidget} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class Text extends AbstractTextWidget {

	// Text coordinates
	protected int textX;
	protected int textY;
	protected int insetHeight;
	
	// Slide animation stuff
	private int originalTextX;
	private int slideTextIncrement = 1;
	private WorkerTask slideTextWorkerTask;
	
	/**
	 * Construct a {@link Text}
	 */
	public Text() {
		this(KuixConstants.TEXT_WIDGET_TAG);
	}
	
	/**
	 * Construct a {@link Text}
	 *
	 * @param tag
	 */
	public Text(String tag) {
		super(tag);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getPreferredSize(int)
	 */
	public Metrics getPreferredSize(int preferredWidth) {
		Metrics metrics;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth);
			String text = getText();
			Font font = getFont();
			if (font != null) {
				if (text != null) {
					metrics.width += font.stringWidth(text);
				} else {
					metrics.width += font.charWidth(' ');
				}
				metrics.height += font.getHeight();
			}
		} else {
			metrics = getCachedMetrics();
		}
		return metrics;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		// Impossible to add any child to a text widget
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#doLayout()
	 */
	protected void doLayout() {
		
		Insets insets = getInsets();
		insetHeight = getHeight() - insets.top - insets.bottom;
		textX = insets.left; 
		textY = insets.top;
		
		String text = getDisplayedText();
		if (text != null) {
			
			// Compute text size and position
			Font font = getFont();
			Alignment alignment = getAlign();
			int textWidth = font.stringWidth(text);
			int insetWidth = getWidth() - insets.left - insets.right;
			if (alignment != null) {
				textX += alignment.alignX(insetWidth, textWidth);
				textY += alignment.alignY(insetHeight, font.getHeight());
			}
			originalTextX = textX;
			
			// Slide animation if text is bigger than insetWidth
			if (slideTextWorkerTask != null) {
				Worker.instance.removeTask(slideTextWorkerTask);
				slideTextWorkerTask = null;
			}
			if (textWidth > insetWidth) {
				int offset = textWidth - insetWidth;
				final int minOffset = Math.min(textX, -offset + insets.left);
				final int maxOffset = minOffset + offset;
				slideTextIncrement = 1;
				slideTextWorkerTask = new WorkerTask() {

					/* (non-Javadoc)
					 * @see org.kalmeo.util.worker.WorkerTask#run()
					 */
					public boolean run() {
						
						if (isVisible()) {
							if (textX < minOffset || textX > maxOffset) {
								slideTextIncrement *= -1;
							}
							textX += slideTextIncrement;
							invalidateAppearance();
						}
						
						// Remove the WorkerTask if the widget is not in the widget tree
						return !isInWidgetTree();
					}
					
				};
				if (isFocusWidgetChild()) {
					Worker.instance.pushTask(slideTextWorkerTask);
				}
			}
			
		}
		
		markAsValidate();
		
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#paint(javax.microedition.lcdui.Graphics)
	 */
	protected void paint(Graphics g) {
		paintBackground(g);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#paintChildrenImpl(javax.microedition.lcdui.Graphics)
	 */
	protected void paintChildrenImpl(Graphics g) {
		String text = getDisplayedText();
		if (text != null) {

			Object colorValue = getStylePropertyValue(KuixConstants.COLOR_STYLE_PROPERTY, true);
			if (colorValue != null) {
				g.setColor(((Color) colorValue).getRGB());
			} else {
				g.setColor(0x000000);
			}
			g.setFont(getFont());
			g.drawString(text, textX, textY, 0);
			
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onFocus(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onFocus(Widget focusedWidget) {
		if (slideTextWorkerTask != null) {
			Worker.instance.removeTask(slideTextWorkerTask);
			Worker.instance.pushTask(slideTextWorkerTask);
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onLostFocus(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onLostFocus(Widget focusedWidget) {
		if (slideTextWorkerTask != null) {
			Worker.instance.removeTask(slideTextWorkerTask);
			textX = originalTextX;
			slideTextIncrement = 1;
		}
	}
	
}