/*
 * This file is part of org.kalmeo.debug.kuix.validator.key.
 * 
 * org.kalmeo.debug.kuix.validator.key is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * org.kalmeo.debug.kuix.validator.key is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with org.kalmeo.debug.kuix.validator.key.  If not, see <http://www.gnu.org/licenses/>.
 *  
 * Creation date : 12 mars 08
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 */

package org.kalmeo.debug.kuix.validator.key;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.widget.Desktop;
import org.kalmeo.kuix.widget.FocusableWidget;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Text;

/**
 * @author bbeaulant
 */
public class KuixKeyValidator extends KuixMIDlet {

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixMIDlet#initDesktopStyles()
	 */
	public void initDesktopStyles() {
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixMIDlet#initDesktopContent(org.kalmeo.kuix.widget.Desktop)
	 */
	public void initDesktopContent(Desktop desktop) {
		Screen screen = new Screen();
		screen.setAttribute("style", "layout:inlinelayout(false,fill)");
		screen.setTitle("[ Press a key ]");
		screen.add(new Text().setText("plateformName=" + Kuix.getCanvas().getPlatformName()));
		screen.add(new Text().setText("screenSize=" + Kuix.getCanvas().getWidth() + "x" + Kuix.getCanvas().getHeight()));
		final Text eventTypeText = new Text();
		screen.add(eventTypeText);
		final Text kuixKeyCodeText = new Text();
		screen.add(kuixKeyCodeText);
		final Text kuixKeyNameText = new Text();
		screen.add(kuixKeyNameText);
		FocusableWidget focusableWidget = new FocusableWidget() {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#processKeyEvent(byte, int)
			 */
			public boolean processKeyEvent(byte type, int kuixKeyCode) {
				eventTypeText.setText("eventType=" + type);
				kuixKeyCodeText.setText("kuixKeyCode=" + kuixKeyCode);
				kuixKeyNameText.setText("kuixKeyName=" + getKeyName(kuixKeyCode));
				return true;
			}
			
		};
		screen.add(focusableWidget);
		screen.setCurrent();
	}
	
	private String getKeyName(int kuixKeyCode) {
		switch (kuixKeyCode) {

			case KuixConstants.KUIX_KEY_UP:
				return "up";
			case KuixConstants.KUIX_KEY_DOWN:
				return "down";
			case KuixConstants.KUIX_KEY_LEFT:
				return "left";
			case KuixConstants.KUIX_KEY_RIGHT:
				return "right";
			case KuixConstants.KUIX_KEY_FIRE:
				return "fire";
	
			case KuixConstants.KUIX_KEY_SOFT_LEFT:
				return "softleft";
			case KuixConstants.KUIX_KEY_SOFT_RIGHT:
				return "softright";
			case KuixConstants.KUIX_KEY_DELETE:
				return "delete";
			case KuixConstants.KUIX_KEY_BACK:
				return "back";
			case KuixConstants.KUIX_KEY_PENCIL:
				return "pencil";
				
			case KuixConstants.KUIX_KEY_0:
				return "0";
			case KuixConstants.KUIX_KEY_1:
				return "1";
			case KuixConstants.KUIX_KEY_2:
				return "2";
			case KuixConstants.KUIX_KEY_3:
				return "3";
			case KuixConstants.KUIX_KEY_4:
				return "4";
			case KuixConstants.KUIX_KEY_5:
				return "5";
			case KuixConstants.KUIX_KEY_6:
				return "6";
			case KuixConstants.KUIX_KEY_7:
				return "7";
			case KuixConstants.KUIX_KEY_8:
				return "8";
			case KuixConstants.KUIX_KEY_9:
				return "9";
			case KuixConstants.KUIX_KEY_POUND:
				return "#";
			case KuixConstants.KUIX_KEY_STAR:
				return "*";
				
		}
		return "NA";
		
	}

}
