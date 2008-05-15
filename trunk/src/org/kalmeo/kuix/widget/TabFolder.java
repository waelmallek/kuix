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
import org.kalmeo.kuix.layout.BorderLayout;
import org.kalmeo.kuix.layout.BorderLayoutData;
import org.kalmeo.kuix.layout.InlineLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayout;
import org.kalmeo.kuix.util.Alignment;

/**
 * This class represent a tab folder.
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
 * 		<td colspan="5"> Inherited attributes : see {@link AbstractFocusableWidget} </td>
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
 * 		<td colspan="4"> Inherited style properties : see {@link AbstractFocusableWidget} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link AbstractFocusableWidget} </td>
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
public class TabFolder extends AbstractFocusableWidget {

	/**
	 * Represents a tab button
	 */
	class TabButton extends CheckBox {
		
		private TabItem tabItem;
		private Text labelWidget;
		private Picture iconWidget;
		
		/**
		 * Construct a {@link TabButton}
		 */
		public TabButton(TabItem tabItem) {
			super(KuixConstants.TAB_BUTTON_WIDGET_TAG);
			this.tabItem = tabItem;
			
			if (tabItem != null) {
				setEnabled(tabItem.isEnabled());
				if (tabItem.getIcon() != null) {
					setIcon(tabItem.getIcon());
				}
				if (tabItem.getLabel() != null) {
					setLabel(tabItem.getLabel());
				}
			}
			
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.AbstractActionWidget#processActionEvent()
		 */
		public boolean processActionEvent() {
			setCurrentTabItem(tabItem);
			return true;
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.AbstractFocusableWidget#isFocusable()
		 */
		public boolean isFocusable() {
			return false;
		}
		
		/**
		 * @param label
		 */
		public void setLabel(String label) {
			if (labelWidget == null) {
				labelWidget = new Text();
				this.add(labelWidget);
			} else if (label == null) {
				labelWidget.remove();
				labelWidget = null;
				return;
			}
			labelWidget.setText(label);
		}
		
		/**
		 * @param icon
		 */
		public void setIcon(String icon) {
			if (iconWidget == null) {
				iconWidget = new Picture();
				this.add(iconWidget);
			} else if (icon == null) {
				iconWidget.remove();
				iconWidget = null;
				return;
			}
			iconWidget.setSource(icon);
		}
		
	}
	
	// Defaults
	private static final Layout DEFAULT_TAB_BUTTON_CONTAINER_LAYOUT = new InlineLayout(true, Alignment.FILL);

	// Internal widgets
	private Widget tabContainer;
	private Widget tabItemContainer;
	private TabItem currentTabItem;
	
	private Widget defaultTabItem;

	/**
	 * Construct a {@link TabFolder}
	 */
	public TabFolder() {
		super(KuixConstants.TAB_FOLDER_WIDGET_TAG);
		tabContainer = new Widget(KuixConstants.TAB_BUTTONS_CONTAINER_WIDGET_TAG) {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getDefaultStyleAttributeValue(java.lang.String)
			 */
			protected Object getDefaultStylePropertyValue(String name) {
				if (KuixConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
					return DEFAULT_TAB_BUTTON_CONTAINER_LAYOUT;
				}
				return super.getDefaultStylePropertyValue(name);
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
			 */
			public LayoutData getLayoutData() {
				return BorderLayoutData.instanceNorth;
			}

		};
		super.add(tabContainer);
		tabItemContainer = new Widget(KuixConstants.TAB_ITEM_CONTAINER_WIDGET_TAG) {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayout()
			 */
			public Layout getLayout() {
				return StaticLayout.instance;
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
			 */
			public LayoutData getLayoutData() {
				return BorderLayoutData.instanceCenter;
			}

		};
		super.add(tabItemContainer);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isFocusWidgetChild()
	 */
	public boolean isFocusWidgetChild() {
		return false;	// Special case for TabFolder focus stop recursion
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return BorderLayout.instance;
	}
	
	/**
	 * @return the currentTabItem
	 */
	public TabItem getCurrentTabItem() {
		return currentTabItem;
	}
	
	/**
	 * @param defaultTabItem the defaultTabItem to set
	 */
	public void setDefaultTabItem(TabItem defaultTabItem) {
		if (this.defaultTabItem != null) {
			this.defaultTabItem.cleanUp();
			this.defaultTabItem.remove();
		}
		this.defaultTabItem = defaultTabItem;
		if (defaultTabItem != null) {
			tabItemContainer.add(defaultTabItem);
			defaultTabItem.setVisible(currentTabItem == null);
		}
	}
	
	/**
	 * @return the defaultTabItem
	 */
	public Widget getDefaultTabItem() {
		return defaultTabItem;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		if (widget instanceof TabItem) {
			if (KuixConstants.TAB_ITEM_WIDGET_TAG.equals(widget.getTag())) {
				addTabItem((TabItem) widget);
			} else {
				setDefaultTabItem((TabItem) widget);
			}
		}
		return this;
	}

	/**
	 * Add a new {@link TabItem}
	 * 
	 * @param tabItem
	 * @return This {@link TabFolder}
	 */
	public void addTabItem(final TabItem tabItem) {
		if (tabItem != null) {
			
			// Create the tabButton
			TabButton tabButton = new TabButton(tabItem);
			tabItem.tabButton = tabButton;
			tabContainer.add(tabButton);
			
			// Add tabItem
			tabItemContainer.add(tabItem);
			if (currentTabItem == null && tabItem.isEnabled()) {
				setCurrentTabItem(tabItem);
			} else {
				tabItem.setVisible(false);
			}
			
		}
	}

	/**
	 * Set the current {@link TabItem} (only if this instance is a child of the
	 * {@link TabFolder})
	 * 
	 * @param tabItem
	 */
	public void setCurrentTabItem(TabItem tabItem) {
		if (tabItem != null && tabItem.parent != tabItemContainer) {
			return;
		}
		if (currentTabItem != null) {
			if (currentTabItem.tabButton != null) {
				currentTabItem.tabButton.setSelected(false);
			}
			currentTabItem.setVisible(false);
		}
		currentTabItem = tabItem;
		if (tabItem != null) {
			if (tabItem.tabButton != null) {
				tabItem.tabButton.setSelected(true);
			}
			tabItem.setVisible(true);
		}
		if (defaultTabItem != null) {
			defaultTabItem.setVisible(currentTabItem == null);
		}
	}
	
	/**
	 * Select the previous enabled tab
	 */
	public void selectPreviousTab() {
		Widget tab = currentTabItem.tabButton;
		while (tab != null) {
			tab = tab.previous;
			if (tab == null) {
				tab = currentTabItem.tabButton.parent.getLastChild();
			}
			if (tab != null && ((CheckBox) tab).isEnabled() || tab == currentTabItem.tabButton) {
				break;
			}
		}
		if (tab != null) {
			tab.processActionEvent();
		}
	}
	
	/**
	 * Select the next enabled tab
	 */
	public void selectNextTab() {
		Widget tab = currentTabItem.tabButton;
		while (tab != null) {
			tab = tab.next;
			if (tab == null) {
				tab = currentTabItem.tabButton.parent.getChild();
			}
			if (tab != null && ((CheckBox) tab).isEnabled() || tab == currentTabItem.tabButton) {
				break;
			}
		}
		if (tab != null) {
			tab.processActionEvent();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processKeyEvent(byte, int)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		if (!processShortcutKeyEvent(type, kuixKeyCode)) {
			if (currentTabItem != null) {
				return currentTabItem.getFocusManager().processKeyEvent(type, kuixKeyCode);
			}
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processShortcutKeyEvent(byte, int, int)
	 */
	public boolean processShortcutKeyEvent(byte type, int kuixKeyCode) {
		
		if (currentTabItem != null
				&& (type == KuixConstants.KEY_PRESSED_EVENT_TYPE	
						|| type == KuixConstants.KEY_REPEATED_EVENT_TYPE)) {
			
			switch (kuixKeyCode) {
				
				case KuixConstants.KUIX_KEY_LEFT: {
					selectPreviousTab();
					return true;
				}
				
				case KuixConstants.KUIX_KEY_RIGHT: {
					selectNextTab();
					return true;
				}
				
			}
			
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#propagateFocusEvent(org.kalmeo.kuix.widget.Widget, boolean)
	 */
	protected void propagateFocusEvent(Widget focusedWidget, boolean lost) {
		if (lost) {
			onLostFocus(focusedWidget);
		} else {
			onFocus(focusedWidget);
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onAdded(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onAdded(Widget parent) {
		FocusManager focusManager = getFocusManager();
		if (focusManager != null) {
			
			// By default the TabFolder catch the focus if its parent has a focusManager
			focusManager.requestFocus(this);
			
			// Define shortcut keys
			setShortcutKeyCodes(KuixConstants.KUIX_KEY_LEFT | KuixConstants.KUIX_KEY_RIGHT, KuixConstants.KEY_PRESSED_EVENT_TYPE);
			focusManager.addShortcutHandler(this);
		}
	}

}