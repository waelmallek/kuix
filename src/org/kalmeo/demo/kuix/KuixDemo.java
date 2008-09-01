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

package org.kalmeo.demo.kuix;

import org.kalmeo.demo.kuix.frames.KuixDemoMainFrame;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixMIDlet;
import org.kalmeo.kuix.widget.Desktop;

/**
 * @author omarino
 */
public class KuixDemo extends KuixMIDlet {

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixMIDlet#initDesktopStyles()
	 */
	public void initDesktopStyles() {
		Kuix.loadCss("/css/style.css");
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.KuixMIDlet#initDesktopContent(org.kalmeo.kuix.widget.Desktop)
	 */
	public void initDesktopContent(Desktop desktop) {
		// Push the MainMenuFrame
		Kuix.getFrameHandler().pushFrame(KuixDemoMainFrame.instance);
	}

}
