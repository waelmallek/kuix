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

import org.kalmeo.demo.kuix.model.Calculator;
import org.kalmeo.demo.kuix.model.Media;
import org.kalmeo.demo.kuix.model.MediaList;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.widget.Desktop;
import org.kalmeo.kuix.widget.ListItem;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.frame.Frame;

/**
 * @author omarino
 */
public class KuixDemoAppsFrame implements Frame {

	public static final KuixDemoAppsFrame instance = new KuixDemoAppsFrame();
	private Desktop desktop;

	private String[] xmlFiles = { "form.xml", "calculator.xml", "library.xml" };
	private DataProvider[] providers = { new DataProvider(), new Calculator(), new MediaList() };
	private int pos = -1;

	/**
	 * Add a Media to the MediaList
	 * 
	 * @param arguments arguments comming from <code>add</code>,
	 *            <code>addBefore</code>, <code>addAfter</code> methods in
	 *            <code>library.xml</code> file
	 * @param reference the Media taken as reference to add the new Media
	 * @param after <code>true</code> to add after the reference, <code>false</code> else
	 */
	public void addMedia(Object[] arguments, DataProvider reference, boolean after) {
		Media media = new Media();

		if (arguments[0] != null) {
			media.type = Integer.parseInt((String) arguments[0]);
		} else {
			media.type = 0;
		}

		String title = (String) arguments[1];
		media.title = title;
		if (title != null && title != "") {
			providers[2].addItem(MediaList.LIST_PROPERTY, media, reference, after);
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onMessage(java.lang.Object, java.lang.Object[])
	 */
	public boolean onMessage(Object identifier, Object[] arguments) {
		// form.xml methods
		if ("setLang".equals(identifier)) {
			desktop.getCurrentScreen().cleanUp();
			Kuix.initI18nSupport((String) arguments[0]);
			desktop.setCurrentScreen(Kuix.loadScreen("/xml/apps/form.xml", null));
			return false;
		}
		if ("showPopup".equals(identifier)) {
			final String mr = Kuix.getMessage("MR");
			final String miss = Kuix.getMessage("MISS");
			
			String civility = mr + "/" + miss;
			String username = (String) arguments[0];
			String firstname = (String) arguments[1];

			if (arguments[2] != null) {
				if ("M".equals((String) arguments[2])) {
					civility = mr;
				} else {
					civility = miss;
				}
			}

			if (username == null || "".equals(username)) {
				username = Kuix.getMessage("NO_NAME");
			}
			if (firstname == null || "".equals(firstname)) {
				firstname = Kuix.getMessage("NO_FIRST_NAME");
			}

			String[] string = { civility, username, firstname };
			Kuix.alert(Kuix.getMessage("POPUP_TEXT", string));
			return false;
		}

		// calculator.xml methods
		Calculator calculator = (Calculator) providers[1];
		if ("updateValue1".equals(identifier)) {
			short value = 0;
			try {
				value = Short.parseShort((String) arguments[0]);
			} catch (NumberFormatException e) {
			}
			calculator.setValue1(value);
			calculator.calculates();
			return false;
		}
		if ("updateValue2".equals(identifier)) {
			short value = 0;
			try {
				value = Short.parseShort((String) arguments[0]);
			} catch (NumberFormatException e) {
			}
			calculator.setValue2(value);
			calculator.calculates();
			return false;
		}
		if ("updateOperator".equals(identifier)) {
			calculator.setOperator(((String) arguments[0]).charAt(0));
			calculator.calculates();
			return false;
		}

		// library.xml methods
		MediaList mediaList = (MediaList) providers[2];
		if ("askAdd".equals(identifier)) {
			Kuix.showPopupBox("/xml/apps/add_popup.xml", null);
			return false;
		}
		if ("add".equals(identifier)) {
			Widget widget;
			int position = Integer.valueOf((String) arguments[2]).intValue();
			switch (position) {
				case 1:
					addMedia(arguments, null, false);
					break;
				case 2:
					widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
					if (widget != null) {
						addMedia(arguments, widget.getDataProvider(), false);
					}
					break;
				case 3:
					widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
					if (widget != null) {
						addMedia(arguments, widget.getDataProvider(), true);
					}
					break;
			}
			return false;
		}
		if ("remove".equals(identifier)) {
			Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
			if (widget instanceof ListItem) {
				mediaList.removeItem(MediaList.LIST_PROPERTY, ((ListItem) widget).getDataProvider());
			}
			return false;
		}
		if ("removeAll".equals(identifier)) {
			mediaList.removeAllItems(MediaList.LIST_PROPERTY);
			return false;
		}
		if ("sortBy".equals(identifier)) {
			if (arguments[0].equals("title")) {
				mediaList.sortItems(MediaList.LIST_PROPERTY, Media.TITLE_FLAG);
			}
			if (arguments[0].equals("type")) {
				mediaList.sortItems(MediaList.LIST_PROPERTY, Media.TYPE_FLAG);
			}
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

		Kuix.loadScreen("/xml/apps/" + xmlFiles[pos], providers[pos]).setCurrent();
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
