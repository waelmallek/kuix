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

package org.kalmeo.kuix.core;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import org.kalmeo.kuix.widget.Desktop;
import org.kalmeo.kuix.widget.PopupBox;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.worker.Worker;

/**
 * This class derived the J2ME {@link MIDlet} and is the base of all Kuix
 * applications.
 * Simply derived this class to start your own Kuix application.
 * 
 * @author bbeaulant
 */
public abstract class KuixMIDlet extends MIDlet implements CommandListener {
	
	// Fatal alert exit command
	private static final Command FATAL_EXIT_COMMAND = new Command("Exit", Command.EXIT, 0);

	// The Midlet instance
	private static KuixMIDlet defaultInstance;

	// Associated KuixCanvas
	private KuixCanvas canvas;

	// Associated Display
	private Display display = Display.getDisplay(this);

	// Specify if the MIDlet is paused
	private boolean paused = false;

	/**
	 * Construct a {@link KuixMIDlet}
	 */
	public KuixMIDlet() {
		defaultInstance = this;
	}

	/**
	 * Returns the {@link KuixCanvas} object instance.
	 * 
	 * @return the canvas
	 */
	public KuixCanvas getCanvas() {
		return canvas;
	}

	/**
	 * Returns the display object instance.
	 * 
	 * @return the display
	 */
	public Display getDisplay() {
		return display;
	}
	
	/**
	 * @return the default instance
	 */
	public static KuixMIDlet getDefault() {
		return defaultInstance;
	}
	
	/**
	 * Overide this method to customize your {@link KuixMIDlet} fullscreen
	 * attribute. By default the value is <code>true</code>.
	 * 
	 * @return <code>true</code> if the midlet is full screen
	 */
	protected boolean isFullscreen() {
		return true;
	}
	
	/**
	 * @return the initalization background color
	 * @since 0.9.1
	 */
	protected int getInitializationBackgroundColor() {
		return 0xFFFFFF;
	}
	
	/**
	 * @return the initalization message color
	 * @since 0.9.1
	 */
	protected int getInitializationMessageColor() {
		return 0x000000;
	}
	
	/**
	 * @return the initalization message
	 * @since 0.9.1
	 */
	protected String getInitializationMessage() {
		return "Loading";
	}

	/**
	 * @return the initalization image file full path
	 * @since 0.9.1
	 */
	protected String getInitializationImageFile() {
		return null;
	}
	
	/**
	 * Init the Desktop's styles.
	 * @since 0.9.1
	 */
	protected abstract void initDesktopStyles();

	/**
	 * Init the Desktop's content. This method is call during the initialization
	 * process, then it is preferable to load the first screen there.
	 * 
	 * @param desktop
	 * @since 0.9.1
	 */
	protected abstract void initDesktopContent(Desktop desktop);
	
	/**
	 * Destroyed the MIDlet implementation
	 */
	public void destroyImpl() {
		try {
			destroyApp(false);
			notifyDestroyed();
		} catch (MIDletStateChangeException mste) {
		}
	}
	
	// Fatal ////////////////////////////////////////////////////////////////////////////////////
	 
	/** 
	 * Display a basic lcdui fatal error alert popup with the given
	 * <code>message</code>. After displaying
	 * the message the application will be closed.
	 * 
	 * @param message
	 * @since 1.0.1
	 */
	public void fatal(String message) {
		fatal(message, null);
	}
	
	/** 
	 * Display a basic lcdui fatal error alert popup with the given
	 * <code>message</code> and <code>throwable</code>. After displaying
	 * the message the application will be closed.
	 * 
	 * @param message
	 * @param throwable
	 * @since 1.0.1
	 */
	public void fatal(String message, Throwable throwable) {
		
		// Create and display the lcdui alert
		Alert alert = new Alert("Error");
		alert.setString(composeAltertMessage(message, throwable));
		alert.setType(AlertType.ERROR);
		alert.setCommandListener(this);
		alert.addCommand(FATAL_EXIT_COMMAND);
		alert.setTimeout(Alert.FOREVER);
		display.setCurrent(alert);
		
		// Print stack trace for debug
		if (throwable != null) {
			throwable.printStackTrace();
		}
		
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == FATAL_EXIT_COMMAND) {
			destroyImpl();
		}
	}
	
	// PopupBox ////////////////////////////////////////////////////////////////////////////////////
	
	/**
 	 * Create and display a {@link PopupBox}.
 	 * This method is a full feature of all {@link PopupBox} helpers like alert, splash.
 	 * 
 	 * @param styleClass The {@link PopupBox} style class
	 * @param duration the duration of the {@link PopupBox}
	 * @param content the content could be a {@link Widget} or a {@link String}
	 * @param progress a fixed-point integer representing progress value
	 * @param buttonTexts The ordered buttons text
	 * @param buttonShortcutKeyCodes The ordred buttons shortcut kuixKeyCode
	 * @param buttonOnActions The ordred buttons onAction
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox showPopupBox(String styleClass, int duration, Object content, int progress, String[] buttonTexts, int[] buttonShortcutKeyCodes, String[] buttonOnActions, String onCloseAction) {
		if (canvas != null) {
			
			// Construct the PopupBox
			PopupBox popupBox = new PopupBox();
			popupBox.setStyleClass(styleClass);
			popupBox.setDuration(duration);
			popupBox.setContent(content);
			popupBox.setProgress(progress);
			popupBox.setButtons(buttonTexts, buttonShortcutKeyCodes, buttonOnActions);
			popupBox.setOnAction(onCloseAction);
			
			// Add popupBox to desktop
			canvas.getDesktop().addPopup(popupBox);
			
			return popupBox;
		}
		return null;
	}

	// Splash ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Display a splash {@link PopupBox}
	 * 
	 * @param duration the duration of the splash (in ms)
	 * @param content the splash widget content
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox splash(int duration, Widget content, String onCloseAction) {
		return showPopupBox(KuixConstants.SPLASH_STYLE_CLASS, duration, content, -1, null, null, null, onCloseAction);
	}
	
	// Alert ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Create an open an alert box. An alert can only display text content. It
	 * is usful to display simple text message, error, ask question, etc...<br>
	 * The associated buttons are construct from the <code>options</code>
	 * parameter.
	 * <p>
	 * Example:
	 * </p>
	 * 
	 * <pre>
	 * alert(&quot;Hello world&quot;, KuixConstants.ALERT_OK | KuixConstants.ALERT_INFO, &quot;doOk&quot;, null, null, null);
	 * alert(&quot;Is it rainning ?&quot;, KuixConstants.ALERT_YES | KuixConstants.ALERT_NO | KuixConstants.ALERT_QUESTION, null, null, &quot;doYes&quot;, &quot;doNo&quot;);
	 * </pre>
	 * 
	 * @param message the text message to display
	 * @param options the options {@see KuixConstants}
	 * @param okAction the ok onAction name
	 * @param yesAction the yes onAction name
	 * @param noAction the no onAction name
	 * @param cancelAction the cancel onAction name
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox alert(String message, int options, String okAction, String yesAction, String noAction, String cancelAction) {
		
		// Determine alert style class
		String styleClass = KuixConstants.ALERT_DEFAULT_STYLE_CLASS;
		if ((options & KuixConstants.ALERT_DEBUG) == KuixConstants.ALERT_DEBUG) {
			styleClass = KuixConstants.ALERT_DEBUG_STYLE_CLASS;
		}
		if ((options & KuixConstants.ALERT_INFO) == KuixConstants.ALERT_INFO) {
			styleClass = KuixConstants.ALERT_INFO_STYLE_CLASS;
		}
		if ((options & KuixConstants.ALERT_WARNING) == KuixConstants.ALERT_WARNING) {
			styleClass = KuixConstants.ALERT_WARNING_STYLE_CLASS;
		}
		if ((options & KuixConstants.ALERT_ERROR) == KuixConstants.ALERT_ERROR) {
			styleClass = KuixConstants.ALERT_ERROR_STYLE_CLASS;
		}
		if ((options & KuixConstants.ALERT_QUESTION) == KuixConstants.ALERT_QUESTION) {
			styleClass = KuixConstants.ALERT_QUESTION_STYLE_CLASS;
		}
		
		// Determine alert buttons
		boolean[] buttons = new boolean[] 	{
												(options & KuixConstants.ALERT_OK) == KuixConstants.ALERT_OK,
												(options & KuixConstants.ALERT_YES) == KuixConstants.ALERT_YES,
												(options & KuixConstants.ALERT_NO) == KuixConstants.ALERT_NO,
												(options & KuixConstants.ALERT_CANCEL) == KuixConstants.ALERT_CANCEL
											};
		
		// If no button : add default OK button
		if (!buttons[0] && !buttons[1] && !buttons[2] && !buttons[3]) {
			buttons[0] = true;
		}
		
		int numButtons = 0;
		int i = 0;
		for (; i < 4; ++i) {
			if (buttons[i]) {
				numButtons++;
			}
		}
		
		String[] buttonTexts = new String[numButtons];
		int[] buttonShortcutKeyCodes = new int[numButtons];
		String[] buttonsOnActions = new String[numButtons];
		i = 0;
		if (buttons[0]) {
			buttonTexts[i] = Kuix.getMessage(KuixConstants.OK_I18N_KEY);
			buttonShortcutKeyCodes[i] = KuixConstants.KUIX_KEY_FIRE;
			buttonsOnActions[i++] = okAction;
		}
		if (buttons[1]) {
			buttonTexts[i] = Kuix.getMessage(KuixConstants.YES_I18N_KEY);
			buttonShortcutKeyCodes[i] = KuixConstants.KUIX_KEY_SOFT_LEFT | KuixConstants.KUIX_KEY_FIRE;
			buttonsOnActions[i++] = yesAction;
		}
		if (buttons[2]) {
			buttonTexts[i] = Kuix.getMessage(KuixConstants.NO_I18N_KEY);
			buttonShortcutKeyCodes[i] = KuixConstants.KUIX_KEY_SOFT_RIGHT | KuixConstants.KUIX_KEY_BACK;
			buttonsOnActions[i++] = noAction;
		}
		if (buttons[3]) {
			buttonTexts[i] = Kuix.getMessage(KuixConstants.CANCEL_I18N_KEY);
			buttonShortcutKeyCodes[i] = KuixConstants.KUIX_KEY_SOFT_RIGHT | KuixConstants.KUIX_KEY_BACK;
			buttonsOnActions[i] = cancelAction;
		}
		
		// Prepare the alert box
		PopupBox popupBox = showPopupBox(styleClass, -1, message, -1, buttonTexts, buttonShortcutKeyCodes, buttonsOnActions, null);
		if (popupBox == null) {
			System.out.println(message);
		}
		return popupBox;
		
	}
	
	/**
	 * Open an alert box with options. This alert is a {@link PopupBox} with a
	 * single text message an single OK button (mapped to FIRE key). If you try
	 * to use other buttons with <code>options</code>, they will be ignored.
	 * 
	 * @param message the message to display
	 * @param options {@see KuixConstants}
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox alert(String message, int options) {
		return alert(	message, 
						options, 
						null,
						null,
						null,
						null);
	}
	
	/**
	 * Open an alert box with the message text and default style class.
	 * 
	 * @param message the message to display
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox alert(String message) {
		return alert(message, KuixConstants.ALERT_DEFAULT);
	}
	
	/**
	 * Open an alert box with the {@link Throwable} object message and 'alerterror'
	 * style class.
	 * 
	 * @param message the message to display
	 * @param throwable the {@link Throwable} to get message or class name
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox alert(String message, Throwable throwable) {
		
		// Print stack trace for debug
		if (throwable != null) {
			throwable.printStackTrace();
		}
		
		return alert(composeAltertMessage(message, throwable), KuixConstants.ALERT_ERROR | KuixConstants.ALERT_OK);
	}
	
	/**
	 * Open an alert box with the {@link Throwable} object message and
	 * 'alerterror' style class.
	 * 
	 * @param throwable the {@link Throwable} to get message or class name
	 * @return The {@link PopupBox} instance
	 */
	public PopupBox alert(Throwable throwable) {
		return alert(null, throwable);
	}
	
	/**
	 * Compose an alert message by using the given <code>message</code> and
	 * <code>throwable</code>.
	 * 
	 * @param message
	 * @param throwable
	 * @return the composed String.
	 */
	private String composeAltertMessage(String message, Throwable throwable) {
		StringBuffer buffer = new StringBuffer();
		if (message != null) {
			buffer.append(message);
			if (throwable != null) {
				buffer.append(" : ");
			}
		}
		if (throwable != null) {
			if (throwable.getMessage() != null) {
				buffer.append(throwable.getMessage());
			} else {
				buffer.append(throwable.getClass().getName());
			}
		}
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		// Canvas == null => first start : this code is a workaround for WTK emulator bug when pausing app twice
		if (canvas == null) {
			
			// Try to extract the Worker frame duration from the JAD file
			String frameDurationValue = getAppProperty(KuixConstants.KUIX_FRAME_DURATION_APP_PROPERTY);
			if (frameDurationValue != null) {
				int frameDuration = Integer.valueOf(frameDurationValue).intValue();
				Worker.instance.setFrameDuration(frameDuration);
			}
			
		}
		
		// Start the worker
		Worker.instance.start();
			
		if (paused) {
			paused = false;
			if (canvas != null) {
				canvas.repaintNextFrame();
			}
			onResumed();
		} else if (canvas == null) {
			// Create an initialize a new KuixCanvas instance
			canvas = new KuixCanvas(this, isFullscreen());
			canvas.initialize();
			// Call the onStarted event
			onStarted();
		}
		
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		onPause();
		paused = true;
		Worker.instance.stop();
		notifyPaused();
	}

	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
		
		// Stop the worker
		Worker.instance.stop();
		Worker.instance.removeAllTasks();
		
		// Cleanup Kuix
		Kuix.cleanUp();
		
		// Hide KuixCanvas
		display.setCurrent(null);
		
		onDestroy();
		notifyDestroyed();
		
	}
	
	/**
	 * This method is called when a Kuix internal debug infos key event occured.<br>
	 * You can override this method to implement your own debug infos process.
	 */
	protected void processDebugInfosKeyEvent() {
		if (canvas != null) {
			canvas.setDebugInfosEnabled(!canvas.isDebugInfosEnabled());
		}
	}
	
	/**
	 * Call after start process. At this moment the midlet is initialized and
	 * the first screen is visible. Override this method if you want to do post
	 * start actions.
	 */
	protected void onStarted() {
	}

	/**
	 * Call before pause process. Override this method if you want to do pre
	 * pause actions.
	 */
	protected void onPause() {
	}

	/**
	 * Call after resume (unpause) process. Override this method if you want to
	 * do post resume actions.
	 */
	protected void onResumed() {
	}

	/**
	 * Call defore destroy process. Override this method if you want to do pre
	 * destroy actions.
	 */
	protected void onDestroy() {
	}

}
