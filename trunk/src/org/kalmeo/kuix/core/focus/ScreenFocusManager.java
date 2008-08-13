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
 * Creation date : 5 déc. 07
 * Copyright (c) Kalmeo 2007-2008. All rights reserved.
 * http://www.kalmeo.org
 */

package org.kalmeo.kuix.core.focus;

import org.kalmeo.kuix.widget.Screen;

/**
 * @author bbeaulant
 */
public class ScreenFocusManager extends FocusManager {

	/**
	 * Construct a {@link ScreenFocusManager}
	 *
	 * @param screen
	 * @param loop
	 */
	public ScreenFocusManager(Screen screen, boolean loop) {
		super(screen, loop);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.core.focus.FocusManager#processKeyEvent(byte, int)
	 */
	public boolean processKeyEvent(byte type, int kuixKeyCode) {
		if (!super.processKeyEvent(type, kuixKeyCode)) {
			return processSoftKeyEvent(type, kuixKeyCode);
		}
		return true;	
	}

}
