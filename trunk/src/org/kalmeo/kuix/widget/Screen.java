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
 * Creation date : 5 d�c. 07
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.widget;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.core.focus.ScreenFocusManager;
import org.kalmeo.kuix.layout.BorderLayout;
import org.kalmeo.kuix.layout.BorderLayoutData;
import org.kalmeo.kuix.layout.GridLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.LayoutData;
import org.kalmeo.kuix.layout.StaticLayout;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.transition.Transition;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Gap;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.util.BooleanUtil;

/**
 * This class represent a Kuix screen.
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
 * 		<td> <code>title</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the screen title. The value is a string text. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>focusloop</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the screen's focus manager 'loop' parameter. Default value is <code>false</code>. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>firstisleft</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define if the first menu is on the left or the right. Default value is <code>true</code> </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>cleanupwhenremoved</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define if the <code>cleanUp</code> method is called when this {@link Screen} is removed from the widget tree. Activate this parameter if you don't want to see this Screen any more. Default value is <code>false</code>. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="5"> Inherited attributes : see {@link AbstractActionWidget} </td>
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
 * 		<td> <code>gridlayout(1,1)</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout-data</code> </th>
 * 		<td> <code>bld(center)</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>transition</code> </th>
 * 		<td> <code>none</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the screen's appear transition. Default value is <code>none</code>. The value is a transition string (e.g. <code>slide(left)</code>). </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="4"> Inherited style properties : see {@link Widget} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link Widget} </td>
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
 * 		<td> <code>topbar</code> </th>
 * 		<td> The screen's top bar used by default to display title </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>bottombar</code> </th>
 * 		<td> The screen's bottom bar used by default to display first and second menu </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>firstmenu</code> </th>
 * 		<td> The screen's first menu, by default displayed on the left in the bottom bar </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>secondmenu</code> </th>
 * 		<td> The screen's second menu, by default displayed on the right in the bottom bar </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="2"> Inherited internal widgets : see {@link Widget} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class Screen extends Widget {

	/**
	 * This class represents the screen content container
	 */
	private class ScreenContainer extends Widget {

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getLayout()
		 */
		public Layout getLayout() {
			return (Layout) Screen.this.getStylePropertyValue(KuixConstants.LAYOUT_STYLE_PROPERTY, false);
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
		 */
		public LayoutData getLayoutData() {
			return BorderLayoutData.instanceCenter;
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getMargin()
		 */
		public Insets getMargin() {
			return (Insets) Screen.this.getStylePropertyValue(KuixConstants.MARGIN_STYLE_PROPERTY, false);
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getBorder()
		 */
		public Insets getBorder() {
			return (Insets) Screen.this.getStylePropertyValue(KuixConstants.BORDER_STYLE_PROPERTY, false);
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getPadding()
		 */
		public Insets getPadding() {
			return (Insets) Screen.this.getStylePropertyValue(KuixConstants.PADDING_STYLE_PROPERTY, false);
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getAlign()
		 */
		public Alignment getAlign() {
			return (Alignment) Screen.this.getStylePropertyValue(KuixConstants.ALIGN_STYLE_PROPERTY, false);
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getGap()
		 */
		public Gap getGap() {
			return (Gap) Screen.this.getStylePropertyValue(KuixConstants.GAP_STYLE_PROPERTY, false);
		}
		
	}
	
	/**
	 * This class represents a screen top or bottom bar (used for title and/or menu)
	 */
	public class ScreenBar extends Widget {
		
		private BorderLayoutData layoutData;
		
		/**
		 * Construct a {@link ScreenBar}
		 *
		 * @param tag
		 * @param layoutData
		 */
		public ScreenBar(String tag, BorderLayoutData layoutData) {
			super(tag);
			this.layoutData = layoutData;
		}

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
			return layoutData;
		}
		
	}
	
	/**
	 * This class represents a screen menu
	 */
	public class ScreenMenu extends Menu {

		private StaticLayoutData layoutData;
		private boolean internal;

		/**
		 * Construct a {@link ScreenMenu}
		 *
		 * @param tag
		 * @param layoutData
		 * @param internal
		 */
		public ScreenMenu(String tag, StaticLayoutData layoutData, boolean internal) {
			super(tag, null);
			this.layoutData = layoutData;
			this.internal = internal;
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Widget#getLayoutData()
		 */
		public LayoutData getLayoutData() {
			return layoutData;
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.AbstractFocusableWidget#isFocusable()
		 */
		public boolean isFocusable() {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.MenuItem#hideMenuTree()
		 */
		public boolean hideMenuTree() {
			switchToDefaultMenus();
			return super.hideMenuTree();
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Menu#showPopup()
		 */
		public void showPopup() {
			showPopup(getDisplayX(), getDisplayY());
		}

		/* (non-Javadoc)
		 * @see org.kalmeo.kuix.widget.Menu#processActionEvent()
		 */
		public boolean processActionEvent() {
			if (!processMenuAction(this, internal, internal && this == firstInternalMenu || !internal && this == firstMenu)) {
				return super.processActionEvent();
			}
			return true;
		}
		
	}
	
	// FocusManager
	private final ScreenFocusManager focusManager;

	// The content's widgets
	private final ScreenContainer container;
	private ScreenBar topBar;
	private ScreenBar bottomBar;
	
	// Text widget for title
	private Text title;
	
	// Used to determine if this screen call its cleanUp method when removed from its parent
	public boolean cleanUpWhenRemoved = false;
	
	// Used to determine if firstMenu is on the left and then the secondMenu onthe right
	public boolean firstIsLeft = true;
	
	// Menus 
	private ScreenMenu firstMenu;
	private ScreenMenu secondMenu;
	private ScreenMenu firstInternalMenu;
	private ScreenMenu secondInternalMenu;
	
	/**
	 * Construct a {Screen}
	 */
	public Screen() {
		super(KuixConstants.SCREEN_WIDGET_TAG);
		
		// Init content's widgets
		container = new ScreenContainer();
		super.add(container);
		
		// Init focusManagers
		focusManager = new ScreenFocusManager(this, false);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getInternalChildInstance(java.lang.String)
	 */
	public Widget getInternalChildInstance(String tag) {
		if (KuixConstants.TOP_BAR_WIDGET_TAG.equals(tag)) {
			return getTopBar();
		}
		if (KuixConstants.BOTTOM_BAR_WIDGET_TAG.equals(tag)) {
			return getBottomBar();
		}
		if (KuixConstants.FIRST_MENU_WIDGET_TAG.equals(tag)) {
			return getFirstMenu();
		}
		if (KuixConstants.SECOND_MENU_WIDGET_TAG.equals(tag)) {
			return getSecondMenu();
		}
		return super.getInternalChildInstance(tag);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.TITLE_ATTRIBUTE.equals(name)) {
			setTitle(value);
			return true;
		}
		if (KuixConstants.FOCUS_LOOP_ATTRIBUTE.equals(name)) {
			focusManager.setLoop(BooleanUtil.parseBoolean(value));
			return true;
		}
		if (KuixConstants.FIRST_IS_LEFT_ATTRIBUTE.equals(name)) {
			firstIsLeft = BooleanUtil.parseBoolean(value);
			return true;
		}
		if (KuixConstants.CLEAN_UP_WHEN_REMOVED.equals(name)) {
			cleanUpWhenRemoved = BooleanUtil.parseBoolean(value);
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
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return BorderLayout.instance;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getMargin()
	 */
	public Insets getMargin() {
		return Widget.DEFAULT_MARGIN;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getBorder()
	 */
	public Insets getBorder() {
		return Widget.DEFAULT_BORDER;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getPadding()
	 */
	public Insets getPadding() {
		return Widget.DEFAULT_PADDING;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getAlign()
	 */
	public Alignment getAlign() {
		return Widget.DEFAULT_ALIGN;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getGap()
	 */
	public Gap getGap() {
		return Widget.DEFAULT_GAP;
	}

	/**
	 * @return the transition
	 */
	public Transition getTransition() {
		Object transition = getStylePropertyValue(KuixConstants.TRANSITION_STYLE_PROPERTY, false);
		if (transition != null) {
			return (Transition) transition;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getDefaultStylePropertyValue(java.lang.String)
	 */
	protected Object getDefaultStylePropertyValue(String name) {
		if (KuixConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return GridLayout.instanceOneByOne;
		}
		return super.getDefaultStylePropertyValue(name);
	}
	
	/**
	 * Define the desktop title
	 * 
	 * @param text
	 */
	public void setTitle(String text) {
		if (text == null) {
			if (title != null) {
				title.remove();
				title = null;
			}
		} else {
			if (title == null) {
				title = new Text();
				getTopBar().add(title);
			}
			title.setText(text);
		}
	}
	
	/**
	 * Create the internal topBar instance if it doesn't exist and return it.
	 * 
	 * @return the internal topBar instance
	 */
	public ScreenBar getTopBar() {
		if (topBar == null) {
			topBar = new ScreenBar(KuixConstants.TOP_BAR_WIDGET_TAG, BorderLayoutData.instanceNorth);
			super.add(topBar);
		}
		return topBar;
	}
	
	/**
	 * Create the internal bottomBar instance if it doesn't exist and return it.
	 * 
	 * @return the internal bottomBar instance
	 */
	public ScreenBar getBottomBar() {
		if (bottomBar == null) {
			bottomBar = new ScreenBar(KuixConstants.BOTTOM_BAR_WIDGET_TAG, BorderLayoutData.instanceSouth);
			super.add(bottomBar);
		}
		return bottomBar;
	}
	
	/**
	 * Returns the {@link ScreenMenu} that correspond to the given
	 * <code>kuixKeyCode</code>.
	 * 
	 * @param kuixKeyCode
	 * @return the {@link ScreenMenu} that correspond to the given
	 *         <code>kuixKeyCode</code>
	 */
	public Menu getScreenMenu(int kuixKeyCode) {
		if (firstIsLeft && kuixKeyCode == KuixConstants.KUIX_KEY_SOFT_LEFT || !firstIsLeft && kuixKeyCode == KuixConstants.KUIX_KEY_SOFT_RIGHT) {
			if (firstMenu != null && firstMenu.isVisible()) {
				return firstMenu;
			}
			return firstInternalMenu;
		} else {
			if (secondMenu != null && secondMenu.isVisible()) {
				return secondMenu;
			}
			return secondInternalMenu;
		}
	}
	
	/**
	 * Create the firstMenu instance if it doesn't exist and return it.
	 * 
	 * @return the internal firstMenu instance
	 */
	public Menu getFirstMenu() {
		if (firstMenu == null) {
			firstMenu = new ScreenMenu(KuixConstants.FIRST_MENU_WIDGET_TAG, new StaticLayoutData(firstIsLeft ? Alignment.LEFT : Alignment.RIGHT), false);
			getBottomBar().add(firstMenu);
		}
		return firstMenu;
	}
	
	/**
	 * Create the secondMenu instance if it doesn't exist and return it.
	 * 
	 * @return the internal secondMenu instance
	 */
	public Menu getSecondMenu() {
		if (secondMenu == null) {
			secondMenu = new ScreenMenu(KuixConstants.SECOND_MENU_WIDGET_TAG, new StaticLayoutData(firstIsLeft ? Alignment.RIGHT : Alignment.LEFT), false);
			getBottomBar().add(secondMenu);
		}
		return secondMenu;
	}
	
	/**
	 * Create the internal firstMenu instance if it doesn't exist and return it.
	 * 
	 * @return the internal firstMenu instance
	 */
	protected ScreenMenu getFirstInternalMenu() {
		if (firstInternalMenu == null) {
			firstInternalMenu = new ScreenMenu(KuixConstants.FIRST_MENU_WIDGET_TAG, new StaticLayoutData(firstIsLeft ? Alignment.LEFT : Alignment.RIGHT), true);
			firstInternalMenu.add(new Text().setText(Kuix.getMessage(KuixConstants.SELECT_I18N_KEY)));
			getBottomBar().add(firstInternalMenu);
		}
		return firstInternalMenu;
	}
	
	/**
	 * Create the internal secondMenu instance if it doesn't exist and return it.
	 * 
	 * @return the internal secondMenu instance
	 */
	protected ScreenMenu getSecondInternalMenu() {
		if (secondInternalMenu == null) {
			secondInternalMenu = new ScreenMenu(KuixConstants.SECOND_MENU_WIDGET_TAG, new StaticLayoutData(firstIsLeft ? Alignment.RIGHT : Alignment.LEFT), true);
			secondInternalMenu.add(new Text().setText(Kuix.getMessage(KuixConstants.CANCEL_I18N_KEY)));
			getBottomBar().add(secondInternalMenu);
		}
		return secondInternalMenu;
	}
	
	/**
	 * Switch menu display from defaults menus to internal menus
	 */
	protected void switchToInternalMenus() {
		if (firstMenu != null) {
			firstMenu.setVisible(false);
		}
		if (secondMenu != null) {
			secondMenu.setVisible(false);
		}
		getFirstInternalMenu().setVisible(true);
		getSecondInternalMenu().setVisible(true);
	}
	
	/**
	 * Switch menu display from internal menus to default menus
	 */
	protected void switchToDefaultMenus() {
		if (firstInternalMenu != null) {
			firstInternalMenu.setVisible(false);
		}
		if (secondInternalMenu != null) {
			secondInternalMenu.setVisible(false);
		}
		if (firstMenu != null) {
			firstMenu.setVisible(true);
		}
		if (secondMenu != null) {
			secondMenu.setVisible(true);
		}
	}
	
	/**
	 * Set this {@link Screen} has current.
	 */
	public void setCurrent() {
		try {
			KuixMIDlet.getDefault().getCanvas().getDesktop().setCurrentScreen(this);
		} catch (Exception e) {
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		return container.add(widget);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#removeAll()
	 */
	public void removeAll() {
		container.removeAll();
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractActionWidget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		// Does nothing on pointer events
		return false;
	}
	
	/**
	 * Process an internal or default menu action.
	 * 
	 * @param menu
	 * @param internal
	 * @param isFirst
	 * @return <code>true</code> if the event is treated by the widget
	 */
	protected boolean processMenuAction(Menu menu, boolean internal, boolean isFirst) {
		if (internal) {
			boolean switchToDefaultMenu = true;
			if (isFirst) {
				FocusManager focusManager = getDesktop().getCurrentFocusManager();
				if (focusManager != null) {
					switchToDefaultMenu = focusManager.processKeyEvent(KuixConstants.KEY_PRESSED_EVENT_TYPE, KuixConstants.KUIX_KEY_FIRE) && !(focusManager.getFocusedWidget() instanceof Menu);
				}
			} else {
				Menu.hideAllPopupMenu();
			}
			if (switchToDefaultMenu) {
				switchToDefaultMenus();
			}
			return true;
		} else if (menu.popup != null) {
			switchToInternalMenus();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onRemoved(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onRemoved(Widget parent) {
		if (cleanUpWhenRemoved) {
			cleanUp();
		}
	}
	
}
