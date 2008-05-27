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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixCanvas;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.util.StringTokenizer;
import org.kalmeo.util.worker.Worker;
import org.kalmeo.util.worker.WorkerTask;

/**
 * This class represent a textfield.
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
 * 		<td> <code>tooltip</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the tooltip text. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>onchange</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> The change called method. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>constraints</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the textbox edit constraints. The value is a string representing the constraints ( <code>any, emailaddr, numeric, phonenumber, url, decimal, password, sensitive, non_predictive, initial_caps_work, initial_caps_sentence</code> ). The default value is <code>any</code>. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="5"> Inherited attributes : see {@link Text} </td>
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
 * 		<td colspan="4"> Inherited style properties : see {@link Text} </td>
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
 * 		<td colspan="2"> Inherited style pseudo-classes : see {@link Text} </td>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link Text} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class TextField extends Text {

	/**
	 * System text box used to enter text
	 */
	class SystemTextBox extends TextBox implements CommandListener {

		private Command validateCommand = new Command(Kuix.getMessage("VALIDATE"), Command.OK, 0);
		private Command cancelCommand = new Command(Kuix.getMessage("CANCEL"), Command.CANCEL, 0);

		/**
		 * Construct a {@link SystemTextBox}
		 */
		public SystemTextBox() {
			super("", "", 1000, javax.microedition.lcdui.TextField.ANY);
			addCommand(validateCommand);
			addCommand(cancelCommand);
			setCommandListener(this);
		}

		/* (non-Javadoc)
		 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
		 */
		public void commandAction(Command command, Displayable displayable) {
			final Desktop desktop = getDesktop();
			if (command == validateCommand) {
				hideTooltip();
				boolean changed = getString() != null && !getString().equals(getText());
				setText(getString());
				if (changed && onChange != null) {
					Worker.instance.pushTask(new WorkerTask() {

						public boolean run() {
							Kuix.callActionMethod(Kuix.parseMethod(onChange, TextField.this, desktop));
							return true;
						}
						
					});
				}
			}
			KuixCanvas canvas = desktop.getCanvas();
			Display display = canvas.getMidlet().getDisplay();
			if (display != null) {
				display.setCurrent(canvas);
			}
		}
	}

	// Allowed constraints
	public static final String ANY = "any";
	public static final String EMAILADDR = "emailaddr";
	public static final String NUMERIC = "numeric";
	public static final String PHONENUMBER = "phonenumber";
	public static final String URL = "url";
	public static final String DECIMAL = "decimal";
	public static final String PASSWORD = "password";
	public static final String SENSITIVE = "sensitive";
	public static final String NON_PREDICTIVE = "non_predictive";
	public static final String INITIAL_CAPS_WORD = "initial_caps_word";
	public static final String INITIAL_CAPS_SENTENCE = "initial_caps_sentence";

	// TextBox constraints
	private int constraints = javax.microedition.lcdui.TextField.ANY;

	// Tooltip
	private long tooltipTimer;
	private WorkerTask tooltipTask;
	private Text tooltipText;
	private boolean tooltipVisible;

	// The change method
	private String onChange;
	
	// Internal 
	private String displayedText = null;

	/**
	 * Construct a {@link TextField}
	 */
	public TextField() {
		super(KuixConstants.TEXT_FIELD_WIDGET_TAG);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Text#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.TOOLTIP_ATTRIBUTE.equals(name)) {
			if (tooltipText == null) {
				tooltipText = new Text();
				tooltipText.setStyleClass(KuixConstants.TEXT_FIELD_TOOLTIP_STYLE_CLASS);
			}
			tooltipText.setText(value);
			return true;
		}
		if (KuixConstants.ON_CHANGE_ATTRIBUTE.equals(name)) {
			onChange = value;
			return true;
		}
		if (KuixConstants.CONSTRAINTS_ATTRIBUTE.equals(name)) {
			constraints = javax.microedition.lcdui.TextField.ANY;
			StringTokenizer st = new StringTokenizer(value, ", ");
			while (st.hasMoreTokens()) {
				String constraint = st.nextToken();
				if (EMAILADDR.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.EMAILADDR;
				} else if (NUMERIC.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.NUMERIC;
				} else if (PHONENUMBER.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.PHONENUMBER;
				} else if (URL.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.URL;
				} else if (DECIMAL.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.DECIMAL;
				} else if (PASSWORD.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.PASSWORD;
				} else if (SENSITIVE.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.SENSITIVE;
				} else if (NON_PREDICTIVE.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.NON_PREDICTIVE;
				} else if (INITIAL_CAPS_WORD.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.INITIAL_CAPS_WORD;
				} else if (INITIAL_CAPS_SENTENCE.equals(constraint)) {
					constraints |= javax.microedition.lcdui.TextField.INITIAL_CAPS_SENTENCE;
				}
			}
			return true;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.FocusableWidget#isFocusable()
	 */
	public boolean isFocusable() {
		return enabled && focusable;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractTextWidget#setText(java.lang.String)
	 */
	public AbstractTextWidget setText(String text) {
		displayedText = null;
		return super.setText(text);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractTextWidget#getDisplayedText()
	 */
	protected String getDisplayedText() {
		if ((constraints & javax.microedition.lcdui.TextField.PASSWORD) == javax.microedition.lcdui.TextField.PASSWORD) {
			if (displayedText == null && text != null) {
				StringBuffer buffer = new StringBuffer();
				for(int i=0; i<text.length(); ++i) {
					buffer.append('*');
				}
				displayedText = buffer.toString();
			}
			return displayedText;
		}
		return super.getDisplayedText();
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#doLayout()
	 */
	protected void doLayout() {
		super.doLayout();
		if (tooltipText != null) {
			Metrics preferredSize = tooltipText.getPreferredSize(getWidth());
			int x = Alignment.CENTER.alignX(getWidth(), preferredSize.width);
			int y = Alignment.CENTER.alignY(getHeight(), preferredSize.height);
			tooltipText.setBounds(x, y, preferredSize.width, preferredSize.height);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Text#paint(javax.microedition.lcdui.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paintBorder(g);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Text#paintChildrenImpl(javax.microedition.lcdui.Graphics)
	 */
	protected void paintChildrenImpl(Graphics g) {
		super.paintChildrenImpl(g);
		if (isFocused()) {
			g.setColor(0);
			g.drawLine(textX, textY, textX, textY + insetHeight - 1);
		}
		if (tooltipText != null && tooltipVisible) {
			tooltipText.paintImpl(g);
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.AbstractFocusableWidget#processPointerEvent(byte, int, int)
	 */
	public boolean processPointerEvent(byte type, int x, int y) {
		if (isEnabled() && type == KuixConstants.POINTER_RELEASED_EVENT_TYPE) {
			processActionEvent();
		}
		return super.processPointerEvent(type, x, y);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processKeyEvent(byte, int)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		if (isEnabled() && type == KuixConstants.KEY_PRESSED_EVENT_TYPE && (
				kuixKeyCode == KuixConstants.KUIX_KEY_FIRE ||
				kuixKeyCode == KuixConstants.KUIX_KEY_1 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_2 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_3 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_4 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_5 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_6 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_7 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_8 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_9 ||
				kuixKeyCode == KuixConstants.KUIX_KEY_STAR ||
				kuixKeyCode == KuixConstants.KUIX_KEY_POUND ||
				kuixKeyCode == KuixConstants.KUIX_KEY_PENCIL
				)) {
			return processActionEvent();
		}
		return super.processKeyEvent(type, kuixKeyCode);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#processActionEvent()
	 */
	public boolean processActionEvent() {
		Desktop desktop = getDesktop();
		if (desktop != null) {
			Display display = desktop.getCanvas().getMidlet().getDisplay();
			if (display != null) {
	
				// Setup TextBox properties
				SystemTextBox systemTextBox = new SystemTextBox();
				systemTextBox.setConstraints(constraints);
				if (text != null) {
					systemTextBox.setString(text);
				}
	
				// Show TextBox
				display.setCurrent(systemTextBox);
	
			}
			return true;
		}
		return false;
	}

	/**
	 * Hide the TextField tooltip
	 */
	private void hideTooltip() {
		if (tooltipTask != null) {
			tooltipTimer = 0;
			tooltipTask = null;
			tooltipVisible = false;
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onFocus(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onFocus(Widget focusedWidget) {
		super.onFocus(focusedWidget);
		if (focusedWidget == this && tooltipText != null) {
			if (tooltipTimer != 0) {
				tooltipTimer = System.currentTimeMillis();
			} else {
				tooltipTimer = System.currentTimeMillis();
				tooltipTask = new WorkerTask() {

					/* (non-Javadoc)
					 * @see com.kalmeo.util.worker.WorkerTask#execute()
					 */
					public boolean run() {
						if (tooltipTimer == 0) {
							return true;
						}
						if (System.currentTimeMillis() - tooltipTimer > 2000) {
							tooltipVisible = true;
							invalidateAppearance();
							return true;
						}
						return false;
					}
					
				};
				Worker.instance.pushTask(tooltipTask);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#onLostFocus(org.kalmeo.kuix.widget.Widget)
	 */
	protected void onLostFocus(Widget focusedWidget) {
		super.onLostFocus(focusedWidget);
		hideTooltip();
	}

}
