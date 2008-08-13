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
import org.kalmeo.kuix.layout.BorderLayout;
import org.kalmeo.kuix.layout.BorderLayoutData;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayout;

/**
 * This class represents a tab folder. <br>
 * <br>
 * <strong>For further informations, visit the <a
 * href="http://www.kalmeo.org/files/kuix/widgetdoc/index.html"
 * target="new">Kuix widgets reference page</a></strong>.
 * 
 * @author bbeaulant
 */
public class TabFolder extends List {

	/**
	 * This class represents a tab button.
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
		 * Set the label of this tab button.
		 * 
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
		 * Set the icon of this tab button.
		 * 
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
		
		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#onRemoved(org.kalmeo.kuix.widget.Widget)
		 */
		protected void onRemoved(Widget parent) {
			tabItem = null;
		}
		
	}
	
	// Internal widgets
	private final ScrollPane buttonsContainer;
	private final Widget container;
	private TabItem currentTabItem;
	
	// The default widget visible if there's no valid tabs
	private TabItem defaultTabItem;

	/**
	 * Construct a {@link TabFolder}
	 */
	public TabFolder() {
		super(KuixConstants.TAB_FOLDER_WIDGET_TAG);
		
		buttonsContainer = new ScrollPane(KuixConstants.TAB_FOLDER_BUTTONS_CONTAINER_WIDGET_TAG) {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getDefaultStylePropertyValue(java.lang.String)
			 */
			protected Object getDefaultStylePropertyValue(String name) {
				if (KuixConstants.LAYOUT_DATA_STYLE_PROPERTY.equals(name)) {
					return BorderLayoutData.instanceNorth;
				}
				return super.getDefaultStylePropertyValue(name);
			}

		};
		buttonsContainer.setHorizontal(true);
		buttonsContainer.setShowScrollBar(false);
		buttonsContainer.setUseMarkers(false);
		super.add(buttonsContainer);
		
		container = new Widget(KuixConstants.TAB_FOLDER_CONTAINER_WIDGET_TAG) {

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

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#onChildRemoved(org.kalmeo.kuix.widget.Widget)
			 */
			protected void onChildRemoved(Widget widget) {
				if (widget == currentTabItem) {
					selectOtherTab(true, true);
				}
			}
			
		};
		super.add(container);
		
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
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getInternalChildInstance(java.lang.String)
	 */
	public Widget getInternalChildInstance(String tag) {
		if (KuixConstants.TAB_FOLDER_DEFAULT_TAB_ITEM_WIDGET_TAG.equals(tag)) {
			return getDefaultTabItem();
		}
		if (KuixConstants.TAB_FOLDER_BUTTONS_CONTAINER_WIDGET_TAG.equals(tag)) {
			return buttonsContainer;
		}
		if (KuixConstants.TAB_FOLDER_CONTAINER_WIDGET_TAG.equals(tag)) {
			return container;
		}
		return super.getInternalChildInstance(tag);
	}

	/**
	 * @return the defaultTabItem
	 */
	public Widget getDefaultTabItem() {
		if (defaultTabItem == null) {
			defaultTabItem = new TabItem();
			initDefaultTabItem();
		}
		return defaultTabItem;
	}
	
	/**
	 * @return the buttonsContainer
	 */
	public ScrollPane getButtonsContainer() {
		return buttonsContainer;
	}

	/**
	 * @return the container
	 */
	public Widget getContainer() {
		return container;
	}

	/**
	 * @return the currentTabItem
	 */
	public TabItem getCurrentTabItem() {
		return currentTabItem;
	}
	
	/**
	 * Set the current {@link TabItem} (only if this instance is a child of the
	 * {@link TabFolder})
	 * 
	 * @param tabItem
	 */
	public void setCurrentTabItem(TabItem tabItem) {
		if (tabItem != null && tabItem.parent != container) {
			return;
		}
		if (currentTabItem != null) {
			if (currentTabItem.tabButton != null) {
				currentTabItem.tabButton.setSelected(false);
			}
			currentTabItem.selected = false;
			currentTabItem.setVisible(false);
		}
		currentTabItem = tabItem;
		if (tabItem != null) {
			if (tabItem.tabButton != null) {
				tabItem.tabButton.setSelected(true);
			}
			tabItem.selected = true;
			tabItem.setVisible(true);
			buttonsContainer.bestScrollToChild(tabItem.tabButton, false);
		}
		if (defaultTabItem != null) {
			defaultTabItem.setVisible(currentTabItem == null);
		}
	}
	
	/**
	 * Initialize the defaultTabItem
	 */
	private void initDefaultTabItem() {
		if (defaultTabItem != null) {
			container.add(defaultTabItem);
			defaultTabItem.setVisible(currentTabItem == null);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.List#newItemWidgetInstance(org.kalmeo.kuix.core.model.DataProvider)
	 */
	protected Widget newItemWidgetInstance(DataProvider item) {
		return new TabItem(item);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		if (widget instanceof TabItem) {
			addTabItem((TabItem) widget);
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
		if (tabItem != null && tabItem.parent != container) {
			
			// Create the tabButton
			TabButton tabButton = new TabButton(tabItem);
			tabItem.tabButton = tabButton;
			buttonsContainer.add(tabButton);
			
			// Add tabItem
			container.add(tabItem);
			if ((currentTabItem == null || tabItem.isSelected()) && tabItem.isEnabled()) {
				setCurrentTabItem(tabItem);
			} else {
				tabItem.setVisible(false);
			}
			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#removeAll()
	 */
	public void removeAll() {
		container.removeAll();
		initDefaultTabItem();
		setCurrentTabItem(null);			// No tabItem : setCurrent to null
	}
	
	/**
	 * Select an other enabled tab.
	 * 
	 * @param forward
	 * @param unselectIfNoOther
	 */
	protected void selectOtherTab(boolean forward, boolean unselectIfNoOther) {
		Widget currentTab = currentTabItem != null ? currentTabItem.tabButton : (forward ? buttonsContainer.getContainer().getChild() : buttonsContainer.getContainer().getLastChild());
		Widget tab = currentTab;
		while (tab != null) {
			tab = forward ? tab.next : tab.previous;
			if (tab == null) {
				tab = (forward ? buttonsContainer.getContainer().getChild() : buttonsContainer.getContainer().getLastChild());
			}
			if (tab != null) {
				if (tab == currentTab) {
					break;
				}
				if (((CheckBox) tab).isEnabled()) {
					tab.processActionEvent();
					return;
				}
			}
		}
		if (unselectIfNoOther) {
			setCurrentTabItem(null);
		}
	}

	/**
	 * Select the previous enabled tab.
	 */
	public void selectPreviousTab() {
		selectOtherTab(false, false);
	}
	
	/**
	 * Select the next enabled tab.
	 */
	public void selectNextTab() {
		selectOtherTab(true, false);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processKeyEvent(byte, int)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		if (currentTabItem != null) {
			
			// Tab navigation
			if (type == KuixConstants.KEY_PRESSED_EVENT_TYPE	
					|| type == KuixConstants.KEY_REPEATED_EVENT_TYPE) {
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
		
			// Default key process
			return currentTabItem.getFocusManager().processKeyEvent(type, kuixKeyCode);
			
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
		}
	}

}