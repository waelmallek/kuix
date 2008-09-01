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
import org.kalmeo.util.worker.WorkerErrorListener;
import org.kalmeo.util.worker.WorkerTask;

/**
 * This class derived the J2ME {@link MIDlet} and is the base of all Kuix
 * applications.
 * Simply derived this class to start your own Kuix application.
 * 
 * @author bbeaulant
 */
public abstract class KuixMIDlet extends MIDlet implements KuixInitializer, WorkerErrorListener, CommandListener {
	
	// The Midlet instance
	private static KuixMIDlet defaultInstance;

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
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#getMIDlet()
	 */
	public MIDlet getMIDlet() {
		return this;
	}

	/**
	 * Returns the {@link KuixCanvas} object instance.
	 * 
	 * @return the canvas
	 * @deprecated use <code>Kuix.getCanvas()</code>
	 */
	public KuixCanvas getCanvas() {
		return Kuix.getCanvas();
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
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#getInitializationBackgroundColor()
	 */
	public int getInitializationBackgroundColor() {
		return 0xFFFFFF;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#getInitializationMessageColor()
	 */
	public int getInitializationMessageColor() {
		return 0x000000;
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#getInitializationMessage()
	 */
	public String getInitializationMessage() {
		return "Loading";
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#getInitializationImageFile()
	 */
	public String getInitializationImageFile() {
		return null;
	}
	
	/**
	 * Returns a new KuixConverter object instance.<br>
	 * Override this method to propose your own {@link KuixConverter} derived
	 * converter.
	 * 
	 * @return the converter object instance
	 * @since 1.0.1
	 */
	protected KuixConverter createNewConverterInstance() {
		return new KuixConverter();
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#initDesktopStyles()
	 */
	public abstract void initDesktopStyles();

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#initDesktopContent(org.kalmeo.kuix.widget.Desktop)
	 */
	public abstract void initDesktopContent(Desktop desktop);
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#destroyImpl()
	 */
	public void destroyImpl() {
		if (Worker.instance.isRunning()) {
			Worker.instance.pushTask(new WorkerTask() {

				/* (non-Javadoc)
				 * @see org.kalmeo.util.worker.WorkerTask#run()
				 */
				public boolean run() {
					destroyApp(false);
					notifyDestroyed();
					return true;
				}
				
			});
		} else {
			destroyApp(false);
			notifyDestroyed();
		}
	}
	
	// Fatal ////////////////////////////////////////////////////////////////////////////////////
	 
	// Fatal alert exit command
	private static final Command FATAL_EXIT_COMMAND = new Command("Exit", Command.EXIT, 0);

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
		alert.setString(Kuix.composeAltertMessage(message, throwable));
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
	 * @deprecated use <code>Kuix.showPopupBox(styleClass, duration, content, progress, buttonTexts, buttonShortcutKeyCodes, buttonOnActions, onCloseAction)</code>
	 */
	public static PopupBox showPopupBox(String styleClass, int duration, Object content, int progress, String[] buttonTexts, int[] buttonShortcutKeyCodes, String[] buttonOnActions, String onCloseAction) {
		return Kuix.showPopupBox(styleClass, duration, content, progress, buttonTexts, buttonShortcutKeyCodes, buttonOnActions, onCloseAction);
	}

	// Splash ////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Display a splash {@link PopupBox}
	 * 
	 * @param duration the duration of the splash (in ms)
	 * @param content the splash widget content
	 * @return The {@link PopupBox} instance
	 * @deprecated use <code>Kuix.splash(duration, content, onCloseAction)</code>
	 */
	public static PopupBox splash(int duration, Widget content, String onCloseAction) {
		return Kuix.splash(duration, content, onCloseAction);
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
	 * @deprecated use <code>Kuix.alert(message, options, okAction, yesAction, noAction, cancelAction)</code>
	 */
	public static PopupBox alert(String message, int options, String okAction, String yesAction, String noAction, String cancelAction) {
		return Kuix.alert(message, options, okAction, yesAction, noAction, cancelAction);
	}
	
	/**
	 * Open an alert box with options. This alert is a {@link PopupBox} with a
	 * single text message an single OK button (mapped to FIRE key). If you try
	 * to use other buttons with <code>options</code>, they will be ignored.
	 * 
	 * @param message the message to display
	 * @param options {@see KuixConstants}
	 * @return The {@link PopupBox} instance
	 * @deprecated use <code>Kuix.alert(message, options)</code>
	 */
	public PopupBox alert(String message, int options) {
		return Kuix.alert(message, options);
	}
	
	/**
	 * Open an alert box with the message text and default style class.
	 * 
	 * @param message the message to display
	 * @return The {@link PopupBox} instance
	 * @deprecated use <code>Kuix.alert(message)</code>
	 */
	public static PopupBox alert(String message) {
		return Kuix.alert(message);
	}
	
	/**
	 * Open an alert box with the {@link Throwable} object message and 'alerterror'
	 * style class.
	 * 
	 * @param message the message to display
	 * @param throwable the {@link Throwable} to get message or class name
	 * @return The {@link PopupBox} instance
	 * @deprecated use <code>Kuix.alert(message, throwable)</code>
	 */
	public PopupBox alert(String message, Throwable throwable) {
		return Kuix.alert(message, throwable);
	}
	
	/**
	 * Open an alert box with the {@link Throwable} object message and
	 * 'alerterror' style class.
	 * 
	 * @param throwable the {@link Throwable} to get message or class name
	 * @return The {@link PopupBox} instance
	 * @deprecated use <code>Kuix.alert(throwable)</code>
	 */
	public PopupBox alert(Throwable throwable) {
		return Kuix.alert(throwable);
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command command, Displayable displayable) {
		if (command == FATAL_EXIT_COMMAND) {
			destroyImpl();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		
		// Canvas == null => first start : this code is a workaround for WTK emulator bug when pausing app twice
		if (!Kuix.isInitialized()) {
			
			// Try to extract the Worker frame duration from the JAD file
			String frameDurationValue = getAppProperty(KuixConstants.KUIX_FRAME_DURATION_APP_PROPERTY);
			if (frameDurationValue != null) {
				int frameDuration = Integer.valueOf(frameDurationValue).intValue();
				Worker.instance.setFrameDuration(frameDuration);
			}
			
		}
		
		// Start the worker
		Worker.instance.setWorkerErrorListener(this);
		Worker.instance.start();
			
		if (paused) {
			paused = false;
			if (Kuix.isInitialized()) {
				Kuix.getCanvas().repaintNextFrame();
			}
			onResumed();
		} else if (!Kuix.isInitialized()) {
			
			// Create a new KuixCanvas instance
			KuixCanvas canvas = new KuixCanvas(this, isFullscreen());
			
			// Init Kuix engine
			Kuix.initialize(display, canvas, createNewConverterInstance());
			
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
	protected void destroyApp(boolean unconditional) {
		
		// Stop the worker
		Worker.instance.stop();
		Worker.instance.removeAllTasks();
		
		// Cleanup Kuix
		Kuix.cleanUp();
		
		// Hide KuixCanvas
		display.setCurrent(null);
		
		onDestroy();
		
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixInitializer#processDebugInfosKeyEvent()
	 */
	public void processDebugInfosKeyEvent() {
		KuixCanvas canvas = Kuix.getCanvas();
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
	
	/* (non-Javadoc)
	 * @see org.kalmeo.util.worker.WorkerErrorTracker#onWorkerError(java.lang.Error)
	 */
	public void onWorkerError(WorkerTask task, Error error) {
		Kuix.alert(task != null ? task.toString() : null, error);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.util.worker.WorkerErrorTracker#onException(java.lang.Exception)
	 */
	public void onWorkerException(WorkerTask task, Exception exception) {
		Kuix.alert(task != null ? task.toString() : null, exception);
	}
	
}
