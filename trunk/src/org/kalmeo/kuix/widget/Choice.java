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
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.core.focus.FocusManager;
import org.kalmeo.kuix.layout.GridLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Gap;

/**
 * This class represent a choice.
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
 * 		<td> false </td>
 * 		<td> see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="4"> Inherited style properties : see {@link AbstractActionWidget} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link AbstractActionWidget} </td>
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
 * 		<td> <code>choicecontainer</code> </th>
 * 		<td> The inner <code>container</code> widget used to hold the selected <code>radiobutton</code> content in the <code>choice</code> widget. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>screen</code> </th>
 * 		<td> The inner <code>screen</code> widget used to hold radiogroup. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>radiogroup</code> </th>
 * 		<td> The inner <code>radiogroup</code> widget used to hold choice values. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="2"> Inherited internal widgets : see {@link AbstractActionWidget} </td>
 * 	</tr>
 * </table>
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
	private boolean ownerScreenCleanUpWhenRemoved = false;
	
	// Keep there the lastest selected radio button (for internal use)
	private RadioButton lastSelectedRadioButton = null;

	/**
	 * Construct a {@link Choice}
	 */
	public Choice() {
		super(KuixConstants.CHOICE_WIDGET_TAG);

		// Create the inner comb container
		choiceContainer = new Widget(KuixConstants.CHOICE_CONTAINER_WIDGET_TAG) {

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
				restoreOwnerScreen();
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
			 * @see org.kalmeo.kuix.widget.RadioGroup#setSelectedRadioButton(org.kalmeo.kuix.widget.RadioButton, boolean)
			 */
			public void setSelectedRadioButton(RadioButton radioButton, boolean propagateChangeEvent) {
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
				restoreOwnerScreen();
				
				super.setSelectedRadioButton(radioButton, propagateChangeEvent);
			}

		};
		scrollContainer.add(radioGroup);
		
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getInternalChildInstance(java.lang.String)
	 */
	public Widget getInternalChildInstance(String tag) {
		if (KuixConstants.CHOICE_CONTAINER_WIDGET_TAG.equals(tag)) {
			return getScreen();
		}
		if (KuixConstants.SCREEN_WIDGET_TAG.equals(tag)) {
			return getScreen();
		}
		if (KuixConstants.RADIO_GROUP_WIDGET_TAG.equals(tag)) {
			return getRadioGroup();
		}
		return super.getInternalChildInstance(tag);
	}
	
	/**
	 * @return the choiceContainer
	 */
	public Widget getChoiceContainer() {
		return choiceContainer;
	}

	/**
	 * @return the screen
	 */
	public Screen getScreen() {
		return screen;
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
	 * @see org.kalmeo.kuix.widget.Widget#getWidget(java.lang.String)
	 */
	public Widget getWidget(String id) {
		Widget widget = screen.getWidget(id);
		if (widget == null) {
			return super.getWidget(id);
		}
		return widget;
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
	 */
	private void restoreOwnerScreen() {
		if (ownerScreen != null) {
			
			ownerScreen.setCurrent();
			
			// Restore the cleanUpWhenRemoved property
			ownerScreen.cleanUpWhenRemoved = ownerScreenCleanUpWhenRemoved;
			ownerScreen = null;
			
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractActionWidget#processActionEvent()
	 */
	public boolean processActionEvent() {
		Desktop desktop = KuixMIDlet.getDefault().getCanvas().getDesktop();
		if (desktop != null) {
			
			if (lastSelectedRadioButton != null) {
				lastSelectedRadioButton.catchChildrenFrom(choiceContainer);
			}
			
			// Retrieve the owner screen instance
			ownerScreen = desktop.getCurrentScreen();
			
			// Keep the cleanUpWhenRemoved property value
			ownerScreenCleanUpWhenRemoved = ownerScreen.cleanUpWhenRemoved;
			ownerScreen.cleanUpWhenRemoved = false;
			
			desktop.setCurrentScreen(screen);
			
		}
		return super.processActionEvent();
	}

}
