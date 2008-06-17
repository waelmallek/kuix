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
 * Creation date : 5 juin 08
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.widget;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.layout.GridLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Gap;

/**
 * TODO
 * - retrieve value on comboBox
 * - caution to screen auto cleanup
 * - permit style like (choice radiogroup ...)
 * 
 * @author bbeaulant
 */
public class Choice extends AbstractActionWidget {

	// The internal choice container (hold the selected radio button content)
	private final Widget choiceContainer;

	// The screen that is displayed to list choices.
	private final Screen screen;
	private final RadioGroup radioGroup;

	// The screen where this choice is attached
	private Screen ownerScreen;
	
	private RadioButton lastSelectedRadioButton = null;

	/**
	 * Construct a {@link Choice}
	 */
	public Choice() {
		super(KuixConstants.CHOICE_WIDGET_TAG);

		// Create the inner comb container
		choiceContainer = new Widget(KuixConstants.CONTAINER_WIDGET_TAG) {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getAlign()
			 */
			public Alignment getAlign() {
				if (lastSelectedRadioButton != null) {
					return lastSelectedRadioButton.getAlign();
				}
				return super.getAlign();
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getGap()
			 */
			public Gap getGap() {
				if (lastSelectedRadioButton != null) {
					return lastSelectedRadioButton.getGap();
				}
				return super.getGap();
			}

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Widget#getLayout()
			 */
			public Layout getLayout() {
				if (lastSelectedRadioButton != null) {
					if (getChild() == null) {
						catchChildrenFrom(lastSelectedRadioButton);
					}
					return lastSelectedRadioButton.getLayout();
				}
				return super.getLayout();
			}
			
		};
		super.add(choiceContainer);
		
		// Create the inner screen
		screen = new Screen() {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.Screen#processMenuAction(org.kalmeo.kuix.widget.Menu, boolean, boolean)
			 */
			protected boolean processMenuAction(Menu menu, boolean internal, boolean isFirst) {
				if (isFirst) {
					FocusManager focusManager = getDesktop().getCurrentFocusManager();
					if (focusManager != null) {
						focusManager.processKeyEvent(KuixConstants.KEY_PRESSED_EVENT_TYPE, KuixConstants.KUIX_KEY_FIRE);
						return true;
					}
				}
				restoreOwnerScreen(getDesktop());
				return true;
			}
			
		};
		screen.switchToInternalMenus();
		screen.setTitle(Kuix.getMessage(KuixConstants.PLEASE_SELECT_I18N_KEY));
		
		// Create the inner scroll container
		ScrollContainer scrollContainer = new ScrollContainer();
		scrollContainer.setUseMarkers(false);
		screen.add(scrollContainer);

		// Create the inner radio group
		radioGroup = new RadioGroup() {

			/* (non-Javadoc)
			 * @see org.kalmeo.kuix.widget.RadioGroup#setSelectedRadioButton(org.kalmeo.kuix.widget.RadioButton)
			 */
			public void setSelectedRadioButton(RadioButton radioButton) {
				// Check if selection has changed
				if (radioButton != lastSelectedRadioButton) {
					
					// Restore last selected button content
					if (lastSelectedRadioButton != null && lastSelectedRadioButton.getChild() == null) {
						lastSelectedRadioButton.catchChildrenFrom(choiceContainer);
					}
					
					lastSelectedRadioButton = radioButton;
				}
				
				// Invalidate the choice container
				choiceContainer.invalidate();
				
				// Restore the owner screen if not current
				restoreOwnerScreen(getDesktop());
				
				super.setSelectedRadioButton(radioButton);
			}

		};
		scrollContainer.add(radioGroup);

	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getInternalChildInstance(java.lang.String)
	 */
	public Widget getInternalChildInstance(String tag) {
		if (KuixConstants.RADIO_GROUP_WIDGET_TAG.equals(tag)) {
			return radioGroup;
		}
		return super.getInternalChildInstance(tag);
	}

	/**
	 * @return the radioGroup
	 */
	public RadioGroup getRadioGroup() {
		return radioGroup;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return GridLayout.instanceOneByOne;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#cleanUp()
	 */
	public void cleanUp() {
		super.cleanUp();
		screen.cleanUp();
	}
	
	/**
	 * Restore the owner screen.
	 * 
	 * @param desktop
	 */
	private void restoreOwnerScreen(Desktop desktop) {
		if (ownerScreen != null && desktop != null) {
			desktop.setCurrentScreen(ownerScreen);
			ownerScreen = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractActionWidget#processActionEvent()
	 */
	public boolean processActionEvent() {
		Desktop desktop = getDesktop();
		if (desktop != null) {
			
			if (lastSelectedRadioButton != null) {
				lastSelectedRadioButton.catchChildrenFrom(choiceContainer);
			}
			
			ownerScreen = desktop.getCurrentScreen();
			desktop.setCurrentScreen(screen);
		}
		return super.processActionEvent();
	}

}
