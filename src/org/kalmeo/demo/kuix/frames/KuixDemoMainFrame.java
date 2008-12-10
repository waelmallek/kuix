/*
 * This file is part of org.kalmeo.demo.kuix.
 * 
 * org.kalmeo.demo.kuix is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * org.kalmeo.demo.kuix is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with org.kalmeo.demo.kuix.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Creation date : 10 mar. 2008
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 */

package org.kalmeo.demo.kuix.frames;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import org.kalmeo.demo.kuix.KuixDemo;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.widget.Gauge;
import org.kalmeo.kuix.widget.PopupBox;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.MathFP;
import org.kalmeo.util.frame.Frame;
import org.kalmeo.util.worker.Worker;
import org.kalmeo.util.worker.WorkerTask;

/**
 * @author omarino
 */
public class KuixDemoMainFrame implements Frame {

	// Static frame instance
	public static final KuixDemoMainFrame instance = new KuixDemoMainFrame();
	
	private final Screen screen = Kuix.loadScreen("/xml/screen.xml", null);

	/**
	 * Show the main screen of the application
	 */
	public void showScreen() {
		screen.setCurrent();
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onMessage(java.lang.Object, java.lang.Object[])
	 */
	public boolean onMessage(Object identifier, Object[] arguments) {
		if ("demo".equals(identifier)) {
			Kuix.getFrameHandler().pushFrame(KuixDemoDemosFrame.instance);
			if (arguments[0] instanceof String) {
				Kuix.getFrameHandler().processMessage(arguments[0], null);
			}
			return false;
		}
		if ("apps".equals(identifier)) {
			Kuix.getFrameHandler().pushFrame(KuixDemoAppsFrame.instance); 
			if (arguments[0] instanceof String) {
				Kuix.getFrameHandler().processMessage(arguments[0], null);
			}
			return false;
		}
		if ("progress".equals(identifier)) {
			final Gauge gauge = new Gauge();
			final PopupBox progressBox = Kuix.showPopupBox(null, -1, gauge, Kuix.getMessage(KuixConstants.OK_I18N_KEY), null, null, null, null);
			Worker.instance.pushTask(new WorkerTask() {
				
				private final int PROGRESS_INCREMENT = 1;
				private final int MAX_PROGRESS = 100;
				private int progress = 0;

				public boolean run() {
					gauge.setValue(MathFP.div(progress, MAX_PROGRESS));
					progress += PROGRESS_INCREMENT;
					if (progress > MAX_PROGRESS) {
						progressBox.remove();
						return true;
					}
					return false;
				}
				
			});
		}
		if ("threadProgress".equals(identifier)) {
			final Gauge gauge = new Gauge();
			final PopupBox progressBox = Kuix.showPopupBox(null, -1, gauge, Kuix.getMessage(KuixConstants.OK_I18N_KEY), null, null, null, null);
			new Thread() {

				private final int PROGRESS_INCREMENT = 1;
				private final int MAX_PROGRESS = 100;
				private int progress = 0;
				
				/* (non-Javadoc)
				 * @see java.lang.Thread#run()
				 */
				public void run() {
					while (true) {
						gauge.setValue(MathFP.div(progress, MAX_PROGRESS));
						progress += PROGRESS_INCREMENT;
						if (progress > MAX_PROGRESS) {
							progressBox.remove();
							return;
						}
						try {
							Thread.sleep(Worker.instance.getFrameDuration());
						} catch (InterruptedException e) {
							break;
						}
					}
				}
				
			}.start();
		}
		if ("showMainScreen".equals(identifier)) {
			showScreen();
			return false;
		}
		if ("goMidpForm".equals(identifier)) {
			Form form = new Form("MIDP Form");
			form.append(new TextField("Label", "", 10, 0));
			form.append(new javax.microedition.lcdui.Gauge("Gauge", true, 100, 50));
			final Command backToKuixCommand = new Command("Back", Command.BACK, 0);
			form.addCommand(backToKuixCommand);
			form.setCommandListener(new CommandListener() {

				/* (non-Javadoc)
				 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
				 */
				public void commandAction(Command c, Displayable d) {
					if (c == backToKuixCommand) {
						KuixDemo.getDefault().getDisplay().setCurrent(Kuix.getCanvas());
					}
				}
				
			});
			KuixDemo.getDefault().getDisplay().setCurrent(form);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onAdded()
	 */
	public void onAdded() {
		Widget splash = Kuix.loadWidget("/xml/splash.xml", null);
		Kuix.splash(2000, splash, "showMainScreen");
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.util.frame.Frame#onRemoved()
	 */
	public void onRemoved() {
	}
}
