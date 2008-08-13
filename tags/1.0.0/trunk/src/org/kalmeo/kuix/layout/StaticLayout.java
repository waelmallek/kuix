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

package org.kalmeo.kuix.layout;

import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.MathFP;

/**
 * @author bbeaulant
 */
public class StaticLayout implements Layout {

	// Static instance of a StaticLayout
	public static final StaticLayout instance = new StaticLayout();
	
	/**
	 * Construct a {@link StaticLayout}
	 */
	private StaticLayout() {
		// Constructor is private because only the static instance could be use
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.layout.Layout#computePreferredSize(org.kalmeo.kuix.widget.Widget, int, org.kalmeo.kuix.util.Metrics)
	 */
	public void measurePreferredSize(Widget target, int preferredWidth, Metrics metrics) {
		measure(target, false, preferredWidth, metrics);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.layout.Layout#doLayout(org.kalmeo.kuix.widget.Widget)
	 */
	public void doLayout(Widget target) {
		measure(target, true, target.getWidth(), null);
	}
	
	/**
	 * Measure <code>target</code> children layout
	 * 
	 * @param target
	 * @param layout
	 * @param preferredWidth
	 * @param metrics
	 */
	private void measure(Widget target, boolean layout, int preferredWidth, Metrics metrics) {
		
		Insets insets = target.getInsets();
		Metrics minSize = target.getMinSize();
		int width = preferredWidth - insets.left - insets.right;
		int height = target.getHeight() - insets.top - insets.bottom;
		
		int maxWidth = 0;
		int maxHeight = 0;
		
		for (Widget widget = target.getChild(); widget != null; widget = widget.next) {
			
			int x = 0;
			int y = 0;
			int widgetWidth = 0;
			int widgetHeight = 0;
			
			LayoutData layoutData = widget.getLayoutData();
			if (layoutData != null && layoutData instanceof StaticLayoutData) {
				
				StaticLayoutData staticLayoutData = (StaticLayoutData) layoutData;
				Alignment alignment = staticLayoutData.alignment;
				
				x = staticLayoutData.x;
				y = staticLayoutData.y;
				
				if (staticLayoutData.width < 0 || staticLayoutData.height < 0) {
					Metrics preferredSize = widget.getPreferredSize(width);
					if (staticLayoutData.width < 0) {
						widgetWidth = preferredSize.width;
					}
					if (staticLayoutData.height < 0) {
						widgetHeight = preferredSize.height;
					}
				}
				if (widgetWidth == 0) {
					if (staticLayoutData.width > MathFP.ONE) {
						widgetWidth = staticLayoutData.width;
					} else {
						widgetWidth = MathFP.mul(MathFP.toFP(width), staticLayoutData.width);
					}
					widgetWidth = MathFP.toInt(widgetWidth);
				}
				if (widgetHeight == 0) {
					if (staticLayoutData.height > MathFP.ONE) {
						widgetHeight = staticLayoutData.height;
					} else {
						widgetHeight = MathFP.mul(MathFP.toFP(height), staticLayoutData.height);
					}
					widgetHeight = MathFP.toInt(widgetHeight);
				}
				
				if (alignment != null) {
					if (alignment.isRight()) {
						x += width - widgetWidth;
					} else if (alignment.isHorizontalCenter()) {
						x += (width - widgetWidth) / 2;
					}
					if (alignment.isBottom()) {
						y += height - widgetHeight;
					} else if (alignment.isVerticalCenter()) {
						y += (height - widgetHeight) / 2;
					}
				}
				
			} else {
				
				Metrics preferredSize = widget.getPreferredSize(width);
				widgetWidth = preferredSize.width;
				widgetHeight = preferredSize.height;
				
			}
			
			if (layout) {
				widget.setBounds(	insets.left + x, 
									insets.top + y, 
									widgetWidth, 
									widgetHeight);
			}
			
			maxWidth = Math.max(maxWidth, widgetWidth);
			maxHeight = Math.max(maxHeight, widgetHeight);
		}
		
		if (!layout) {
			metrics.width = insets.left + Math.max(minSize.width, maxWidth) + insets.right;
			metrics.height = insets.top + Math.max(minSize.height, maxHeight) + insets.bottom;
		}
		
	}

}
