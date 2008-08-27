/*
 * This file is part of org.kalmeo.demo.kuix.
 * 
 * org.kalmeo.demo.kuix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * org.kalmeo.demo.kuix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with org.kalmeo.demo.kuix.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Creation date : 10 mar. 2008
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 */

package org.kalmeo.demo.kuix.frames;

import java.util.Random;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.widget.Desktop;
import org.kalmeo.util.BooleanUtil;
import org.kalmeo.util.frame.Frame;

/**
 * @author omarino
 */
public class KuixDemoDemosFrame implements Frame {

	/**
	 * @author omarino
	 */
	public class Provider extends DataProvider {
	
		private static final String TEXT_TABITEM_ENABLED_PROPERTY = "bText";
		private static final String BUTTON_TABITEM_ENABLED_PROPERTY = "bButton";
		private static final String CHOICE_TABITEM_ENABLED_PROPERTY = "bChoice";
		private static final String LIST_TABITEM_ENABLED_PROPERTY = "bList";
		
		private boolean tabTextEnabled = true;
		private boolean tabButtonEnabled = true;
		private boolean tabChoiceEnabled = true;
		private boolean tabListEnabled = true;
		
		/**
		 * Enable or disable the Text TabItem in widget.xml
		 */
		public void switchTabTextEnabledState() {
			tabTextEnabled = !tabTextEnabled;
			dispatchUpdateEvent(TEXT_TABITEM_ENABLED_PROPERTY);
		}
		
		/**
		 * Enable or disable the Button TabItem in widget.xml
		 */
		public void switchTabButtonEnabledState() {
			tabButtonEnabled = !tabButtonEnabled;
			dispatchUpdateEvent(BUTTON_TABITEM_ENABLED_PROPERTY);
		}
		
		/**
		 * Enable or disable the Choice TabItem in widget.xml
		 */
		public void switchTabChoiceEnabledState() {
			tabChoiceEnabled = !tabChoiceEnabled;
			dispatchUpdateEvent(CHOICE_TABITEM_ENABLED_PROPERTY);
		}
		
		/**
		 * Enable or disable the List TabItem in widget.xml
		 */
		public void switchTabListEnabledState() {
			tabListEnabled = !tabListEnabled;
			dispatchUpdateEvent(LIST_TABITEM_ENABLED_PROPERTY);
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.core.model.DataProvider#getUserDefinedValue(java.lang.String)
		 */
		protected Object getUserDefinedValue(String property) {
			if (TEXT_TABITEM_ENABLED_PROPERTY.equals(property)) {
				return BooleanUtil.toString(tabTextEnabled);
			}
			if (BUTTON_TABITEM_ENABLED_PROPERTY.equals(property)) {
				return BooleanUtil.toString(tabButtonEnabled);
			}
			if (CHOICE_TABITEM_ENABLED_PROPERTY.equals(property)) {
				return BooleanUtil.toString(tabChoiceEnabled);
			}
			if (LIST_TABITEM_ENABLED_PROPERTY.equals(property)) {
				return BooleanUtil.toString(tabListEnabled);
			}
			return null;
		}
	}
	
	public static final KuixDemoDemosFrame instance = new KuixDemoDemosFrame();
	private Desktop desktop;

	private String[] xmlFiles = { "borderLayoutDemo.xml", "gridLayoutDemo.xml", "flowLayoutDemo.xml", "staticLayoutDemo.xml", "inLineLayoutDemo1.xml", "inLineLayoutDemo2.xml", "inLineLayoutDemo3.xml", "tableLayoutDemo.xml", "widgets.xml" };
	private int pos = -1;
	
	Random random = new Random(5);
	private Provider provider = new Provider();
	
	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onMessage(java.lang.Object, java.lang.Object[])
	 */
	public boolean onMessage(Object identifier, Object[] arguments) {
		// widget.xml menu
		if ("enableText".equals(identifier)) {
			provider.switchTabTextEnabledState();
			return false;
		}
		if ("enableButton".equals(identifier)) {
			provider.switchTabButtonEnabledState();
			return false;
		}
		if ("enableChoice".equals(identifier)) {
			provider.switchTabChoiceEnabledState();
			return false;
		}
		if ("enableList".equals(identifier)) {
			provider.switchTabListEnabledState();
			return false;
		}
		
		if ("goHome".equals(identifier)) {
			pos = -1;
		}
		
		// global
		if (identifier.equals("back")) {
			--pos;
		} else if (identifier.equals("next")) {
			++pos;
		} else {
			try {
				pos = Integer.parseInt((String) identifier);
			} catch (Exception e) {
			}
		}
		
		if (pos == -1) {
			Kuix.getFrameHandler().removeFrame(this);
			return false;
		}
		
		Kuix.loadScreen("/xml/demos/" + xmlFiles[pos], provider).setCurrent();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onAdded()
	 */
	public void onAdded() {
		desktop = Kuix.getCanvas().getDesktop();
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onRemoved()
	 */
	public void onRemoved() {
		desktop.getCurrentScreen().cleanUp();
		KuixDemoMainFrame.instance.showScreen();
	}

}
