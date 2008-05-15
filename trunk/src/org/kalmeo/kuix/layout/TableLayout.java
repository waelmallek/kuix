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

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Gap;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.kuix.util.Span;
import org.kalmeo.kuix.util.Weight;
import org.kalmeo.kuix.widget.Widget;

/**
 * @author bbeaulant
 */
public class TableLayout implements Layout {

	// Static instance of a TableLayout
	public static final TableLayout instance = new TableLayout();
	
	/**
	 * Construct a {@link TableLayout}
	 */
	private TableLayout() {
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
		
		Alignment targetAlignment = target.getAlign();
		Insets insets = target.getInsets();
		Gap gap = target.getGap();
		
		int width = preferredWidth - insets.left - insets.right;
		int height = target.getHeight() - insets.top - insets.bottom;
		int[] colHeights = null;
		int col = 0;
		int row = 0;
		int ncol = 0;
		int nrow = 0;
		Metrics first = null;
		Metrics current = null;

		for (Widget widget = target.getChild(); widget != null; widget = widget.next) {
			if (KuixConstants.BREAK_WIDGET_TAG.equals(widget.getTag())) {
				if (col != 0) {
					col = 0;
					row++;
				}
				continue;
			}

			Metrics widgetMetrics = widget.getPreferredSize(width);
			if (first == null) {
				first = current = widgetMetrics;
			} else {
				current.next = widgetMetrics;
				current = widgetMetrics;
			}

			Span span = widget.getSpan();
			int colspan = span.colspan;
			int rowspan = span.rowspan;
			if (colHeights != null)
				for (int j = 0; j < colspan; j++) {
					if (colHeights[col + j] > row) {
						col += (j + 1);
						j = -1;
					}
				}

			widgetMetrics.x = col;
			widgetMetrics.y = row;
			ncol = Math.max(ncol, col + colspan);
			nrow = Math.max(nrow, row + rowspan);

			if (rowspan > 1) {
				if (colHeights == null) {
					colHeights = new int[ncol];
				} else if (colHeights.length < ncol) {
					int[] newheights = new int[ncol];
					System.arraycopy(colHeights, 0, newheights, 0, colHeights.length);
					colHeights = newheights;
				}
				for (int j = 0; j < colspan; j++) {
					colHeights[col + j] = row + rowspan;
				}
			}

			col += colspan;
		}

		int[] colWidths = new int[ncol];
		int[] colWeights = new int[ncol];
		int[] rowHeights = new int[nrow];
		int[] rowWeights = new int[nrow];
		
		align(first, colWeights, null, true);
		align(first, colWidths, colWeights, true);
		align(first, rowWeights, null, false);
		align(first, rowHeights, rowWeights, false);

		int contentWidth = sum(colWidths, 0, ncol, gap.horizontalGap);
		int contentHeight = sum(rowHeights, 0, nrow, gap.verticalGap);
		
		if (!layout) {
			metrics.width = insets.left + contentWidth + insets.right;
			metrics.height = insets.top + contentHeight + insets.bottom;
			return;
		}
		
		int contentX = 0;
		int contentY = 0;
		if (targetAlignment != null) {
			if (targetAlignment.isHorizontalCenter()) {
				contentX = (width - contentWidth) / 2;
			} else if (targetAlignment.isRight()) {
				contentX = width - contentWidth;
			}
			if (targetAlignment.isVerticalCenter()) {
				contentY = (height - contentHeight) / 2;
			} else if (targetAlignment.isBottom()) {
				contentY = height - contentHeight;
			}
		}

		for (Metrics widgetMetrics = first; widgetMetrics != null; widgetMetrics = widgetMetrics.next) {
			Widget widget = widgetMetrics.widget;
			Span widgetSpan = widget.getSpan();
			int x = contentX + insets.left + sum(colWidths, 0, widgetMetrics.x, gap.horizontalGap) + ((widgetMetrics.x > 0) ? gap.horizontalGap : 0);
			int y = contentY + insets.top + sum(rowHeights, 0, widgetMetrics.y, gap.verticalGap) + ((widgetMetrics.y > 0) ? gap.verticalGap : 0);
			int widgetWidth = sum(colWidths, widgetMetrics.x, widgetSpan.colspan, gap.horizontalGap);
			int widgetHeight = sum(rowHeights, widgetMetrics.y, widgetSpan.rowspan, gap.verticalGap);
			height = widgetMetrics.height;
			widget.setBounds(x, y, widgetWidth, widgetHeight);
		}

	}

	private static final void align(Metrics first, int[] values, int[] weights, boolean horizontal) {
		for (int size = 1, next = 0; size != 0; size = next, next = 0) {
			for (Metrics metrics = first; metrics != null; metrics = metrics.next) {
				Span span = metrics.widget.getSpan();
				int orientedSpan = horizontal ? span.colspan : span.rowspan;
				if (orientedSpan == size) {
					Weight weight = metrics.widget.getWeight();
					int value = (weights != null) ? (horizontal ? metrics.width : metrics.height) : (horizontal ? weight.weightx : weight.weighty);
					distribute(values, horizontal ? metrics.x : metrics.y, orientedSpan, (weights != null) ? weights : values, value);
				} else if ((orientedSpan > size) && ((next == 0) || (next > orientedSpan))) {
					next = orientedSpan;
				}
			}
		}
	}

	private static final void distribute(int[] values, int from, int length, int[] weights, int value) {
		if (length == 1) {
			values[from] = Math.max(values[from], value);
			return;
		}
		int diff = value, sum = 0;
		for (int i = from, n = from + length; i < n; i++) {
			if (diff <= values[i]) {
				return;
			}
			diff -= values[i];
			sum += weights[i];
		}
		for (int i = from, n = from + length - 1; i < n; i++) {
			if (weights[i] == 0) {
				continue;
			}
			int d = weights[i], inc = d * diff / sum;
			values[i] += inc;
			if (sum <= d) {
				return;
			}
			diff -= inc;
			sum -= d;
		}
		values[from + length - 1] += diff;
	}

	private static final int sum(int[] values, int from, int length, int gap) {
		int sum = 0;
		for (int i = 0; i < length; i++) {
			sum += values[from + i];
		}
		if (length > 1) {
			sum += (length - 1) * gap;
		}
		return sum;
	}
	
}
