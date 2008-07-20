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
 * Creation date : 8 dev. 2007
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.widget;

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.layout.GridLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.widget.TabFolder.TabButton;
import org.kalmeo.util.BooleanUtil;

/**
 * This class represents a tab item. <br>
 * <br>
 * <strong>For further informations, visit the <a
 * href="http://www.kalmeo.org/files/kuix/widgetdoc/index.html"
 * target="new">Kuix widgets reference page</a></strong>.
 * 
 * @author bbeaulant
 */
public class TabItem extends Widget {

	// Defaults
	private static final Layout TAB_ITEM_DEFAULT_LAYOUT = GridLayout.instanceOneByOne;

	// Tab item properties
	private String label;
	private String icon;
	private boolean enabled = true;
	protected boolean selected = false;
	
	// The associated tabFolder
	private TabFolder tabFolder;
	
	// The associated TabButton
	protected TabButton tabButton;
	
	// FocusManager
	private final FocusManager focusManager;
	
	/**
	 * Construct a {@link TabItem}
	 */
	public TabItem() {
		this(null);
	}
	
	/**
	 * Construct a {@link TabItem}
	 *
	 * @param dataProvider
	 */
	public TabItem(DataProvider dataProvider) {
		this(KuixConstants.TAB_ITEM_WIDGET_TAG, dataProvider);
	}

	/**
	 * Construct a {@link TabItem}
	 *
	 * @param tag
	 */
	public TabItem(String tag, DataProvider dataProvider) {
		super(tag);
		setDataProvider(dataProvider);
		
		// Init focusManagers
		focusManager = new FocusManager(this, false);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.LABEL_ATTRIBUTE.equals(name)) {
			setLabel(value);
			return true;
		}
		if (KuixConstants.ICON_ATTRIBUTE.equals(name)) {
			setIcon(value);
			return true;
		}
		if (KuixConstants.ENABLED_ATTRIBUTE.equals(name)) {
			setEnabled(BooleanUtil.parseBoolean(value));
			return true;
		}
		if (KuixConstants.SELECTED_ATTRIBUTE.equals(name)) {
			setSelected(BooleanUtil.parseBoolean(value));
			return true;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getFocusManager()
	 */
	public FocusManager getFocusManager() {
		return focusManager;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
	 */
	public LayoutData getLayoutData() {
		return StaticLayoutData.instanceFull;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getDefaultStyleAttributeValue(java.lang.String)
	 */
	protected Object getDefaultStylePropertyValue(String name) {
		if (KuixConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return TAB_ITEM_DEFAULT_LAYOUT;
		}
		return super.getDefaultStylePropertyValue(name);
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		if (tabButton != null) {
			tabButton.setLabel(label);
		}
		this.label = label;
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		if (tabButton != null) {
			tabButton.setIcon(icon);
		}
		this.icon = icon;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		if (tabButton != null) {
			tabButton.setEnabled(enabled);
		}
		if (tabFolder != null) {
			if (enabled) {
				if (tabFolder.getCurrentTabItem() == null) {
					tabFolder.setCurrentTabItem(this);
				}
			} else if (tabFolder.getCurrentTabItem() == this) {
				tabFolder.selectNextTab();
				if (tabFolder.getCurrentTabItem() == this) {
					tabFolder.setCurrentTabItem(null);
				}
			}
		}
		this.enabled = enabled;
	}
	
	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		if (tabFolder != null) {
			if (selected) {
				tabFolder.setCurrentTabItem(this);
			} else {
				tabFolder.selectOtherTab(true, true);
			}
		}
		this.selected = selected;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onAdded(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onAdded(Widget parent) {
		if (parent.parent instanceof TabFolder) {
			tabFolder = (TabFolder) parent.parent;
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onRemoved(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onRemoved(Widget parent) {
		if (tabButton != null) {
			tabButton.remove();
			tabButton = null;
		}
		tabFolder = null;
	}

}
