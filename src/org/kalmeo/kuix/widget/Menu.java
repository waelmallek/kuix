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

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.util.Metrics;

/**
 * This class represents a menu. <br>
 * <br>
 * <strong>For further informations, visit the <a
 * href="http://www.kalmeo.org/files/kuix/widgetdoc/index.html"
 * target="new">Kuix widgets reference page</a></strong>.
 * 
 * @author bbeaulant
 */
public class Menu extends MenuItem {
	
	/**
	 * This class represents a menu popup
	 */
	public class MenuPopup extends List {

		// The associated FocusManager
		private final FocusManager focusManager;
		
		// Instance of PopupMenu StaticLayoutData
		private final StaticLayoutData layoutData = new StaticLayoutData(null, -1, -1);
		
		/**
		 * Construct a {@link MenuPopup}
		 */
		public MenuPopup() {
			super(KuixConstants.MENU_POPUP_WIDGET_TAG);
			
			// Used as a workaround CLDC 1.0 limitation on PopupMenu.class
			if (menuPopupClass == null) {
				menuPopupClass = getClass();
			}
			
			// Create the associated focusManager
			focusManager = new FocusManager(this, true) {

				/* (non-Javadoc)
				 * @see org.kalmeo.kuix.core.focus.FocusManager#processKeyEvent(byte, int)
				 */
				public boolean processKeyEvent(byte type, int kuixKeyCode) {
					switch (type) {

						case KuixConstants.KEY_PRESSED_EVENT_TYPE:
						case KuixConstants.KEY_REPEATED_EVENT_TYPE: {

							switch (kuixKeyCode) {
								
								case KuixConstants.KUIX_KEY_BACK:
								case KuixConstants.KUIX_KEY_LEFT:
									hidePopupMenu();
									return true;

								case KuixConstants.KUIX_KEY_RIGHT:
									Widget widget;
									// Search forward first
									for (widget = focusedWidget; widget != null; widget = widget.next) {
										if (widget instanceof Menu) {
											requestFocus(widget);
											((Menu) widget).processActionEvent();
											break;
										}
									}
									// Search backward
									for (widget = focusedWidget; widget != null; widget = widget.previous) {
										if (widget instanceof Menu) {
											requestFocus(widget);
											((Menu) widget).processActionEvent();
											break;
										}
									}
									return true;

							}
						}
					}
					if (!super.processKeyEvent(type, kuixKeyCode)) {
						return processSoftKeyEvent(type, kuixKeyCode);
					}
					return true;
				}
				
				/* (non-Javadoc)
				 * @see org.kalmeo.kuix.core.focus.FocusManager#processPointerEvent(byte, int, int)
				 */
				public boolean processPointerEvent(byte type, int x, int y) {
					boolean superProcess = super.processPointerEvent(type, x, y);
					if (type == KuixConstants.POINTER_RELEASED_EVENT_TYPE) {
						if (!superProcess) {
							hidePopupMenu();
							return true;
						}
					}
					return superProcess;
				}	
				
				/**
				 * Hide the associated {@link PopupMenu}
				 */
				private void hidePopupMenu() {
					MenuPopup menuPopup = ((MenuPopup) rootWidget);
					if (getMenu() != null && !(getMenu().parent instanceof MenuPopup)) {
						getMenu().hideMenuTree();
					} else {
						menuPopup.hide();
					}
				}
				
			};
			
		}
		
		/**
		 * @return the associated {@link Menu}
		 */
		public Menu getMenu() {
			return Menu.this;
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
			return layoutData;
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.List#newItemWidgetInstance(org.kalmeo.kuix.core.model.DataProvider)
		 */
		protected Widget newItemWidgetInstance(DataProvider item) {
			return new MenuItem(item);
		}

		/**
		 * Display the {@link PopupMenu}
		 * 
		 * @param desktop
		 * @param displayX
		 * @param displayY
		 */
		public void show(Desktop desktop, int displayX, int displayY) {
			if (desktop != null) {
				
				int desktopWidth = desktop.getWidth();
				int desktopHeight = desktop.getHeight();
				Metrics preferredSize = getPreferredSize(desktopWidth);
				int width = preferredSize.width;
				int height = Math.min(preferredSize.height, desktopHeight);
				
				int x = displayX;
				int y = displayY;
				
				y -= height;
				
				if (x + width > desktopWidth) {
					x = desktopWidth - width;
				}
				
				layoutData.x = x;
				layoutData.y = y;
				
				desktop.addPopup(this);
			}
		}
		
		/**
		 * Hide this popupMenu
		 */
		public void hide() {
			if (parent == null) {
				return;
			}
			for (Widget widget = getChild(); widget != null; widget = widget.next) {
				if (widget instanceof Menu) {
					((Menu) widget).hidePopup();
				}
			}
			remove();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.kalmeo.kuix.widget.Widget#onRemoved(org.kalmeo.kuix.widget.Widget)
		 */
		protected void onRemoved(Widget parent) {
			focusManager.reset();
		}
		
	}
	
	// Used as a workaround CLDC 1.0 limitation on PopupMenu.class
	private static Class menuPopupClass;

	// The associated menuPopup
	protected MenuPopup popup;
	
	/**
	 * Construct a {@link Menu}
	 */
	public Menu() {
		this(null);
	}
	
	/**
	 * Construct a {@link Menu}
	 *
	 * @param dataProvider
	 */
	public Menu(DataProvider dataProvider) {
		this(KuixConstants.MENU_WIDGET_TAG, dataProvider);
	}
	
	/**
	 * Construct a {@link Menu}
	 *
	 * @param tag
	 * @param dataProvider
	 */
	public Menu(String tag, DataProvider dataProvider) {
		super(tag, dataProvider);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getInternalChildInstance(java.lang.String)
	 */
	public Widget getInternalChildInstance(String tag) {
		if (KuixConstants.MENU_POPUP_WIDGET_TAG.equals(tag)) {
			return getPopup();
		}
		return super.getInternalChildInstance(tag);
	}

	/**
	 * @return the popup
	 */
	public MenuPopup getPopup() {
		if (popup == null) {
			popup = new MenuPopup();
		}
		return popup;
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		if (parent instanceof MenuPopup) {
			return ((MenuPopup) parent).getMenu().getDepth() + 1;
		}
		return 0;
	}

	/**
	 * Open the popupMenu
	 */
	public void showPopup() {
		showPopup(getDisplayX() + getWidth(), getDisplayY() + getHeight());
	}

	/**
	 * Open the popupMenu
	 * 
	 * @param displayX
	 * @param displayY
	 */
	public void showPopup(int displayX, int displayY) {
		if (getDepth() == 0) {
			hideAllPopupMenu();
		}
		if (popup != null) {
			popup.show(getDesktop(), displayX, displayY);
		}
	}
	
	/**
	 * Close the popupMenu
	 */
	public void hidePopup() {
		if (popup != null) {
			popup.hide();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.MenuItem#hideMenuTree()
	 */
	public boolean hideMenuTree() {
		if (!super.hideMenuTree()) {
			hidePopup();
			return true;
		}
		return false;
	}

	/**
	 * Hide all visible popupMenus
	 */
	protected static void hideAllPopupMenu() {
		if (menuPopupClass != null) {
			KuixMIDlet.getDefault().getCanvas().getDesktop().removeAllPopupInstanceOf(menuPopupClass);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#cleanUp()
	 */
	public void cleanUp() {
		super.cleanUp();
		hidePopup();
		if (popup != null) {
			popup.cleanUp();
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.MenuItem#processActionEvent()
	 */
	public boolean processActionEvent() {
		if (popup != null) {
			if (popup.parent == null) {
				showPopup();
			}
			return true;
		}
		return super.processActionEvent();
	}

}
