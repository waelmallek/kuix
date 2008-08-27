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

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.KuixConverter;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.util.resource.ImageManager;
import org.kalmeo.util.worker.Worker;
import org.kalmeo.util.worker.WorkerTask;

/**
 * This class represents a picture. <br>
 * <br>
 * <strong>For further informations, visit the <a
 * href="http://www.kalmeo.org/files/kuix/widgetdoc/index.html"
 * target="new">Kuix widgets reference page</a></strong>.
 * 
 * @author bbeaulant
 */
public class Picture extends Widget {

	// The default Picture's aligment
	private static final Alignment PICTURE_DEFAULT_ALIGN = Alignment.CENTER;
	
	// The picture's image
	private Image image;
	
	// Size of a frame
	private int frameWidth;
	private int frameHeight;
	
	// Animation sequence
	private int[] frameSequence;

	// Duration of each frame
	private int frameDuration;
	
	// Animated sprite
	private Sprite sprite;
	
	// The animation workerTask
	private WorkerTask animationWorkerTask;
	
	// Internal use
	private boolean needToResetSprite = true;
	private long lastFrameTime;
	
	/**
	 * Construct a {@link Picture}
	 */
	public Picture() {
		super(KuixConstants.PICTURE_WIDGET_TAG);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getLayout()
	 */
	public Layout getLayout() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setAttribute(java.lang.String, java.lang.String)
	 */
	public boolean setAttribute(String name, String value) {
		if (KuixConstants.SRC_ATTRIBUTE.equals(name)) {
			setSource(value);
			return true;
		}
		if (KuixConstants.FRAME_WIDTH_ATTRIBUTE.equals(name)) {
			setFrameWidth(Integer.parseInt(value));
			return true;
		}
		if (KuixConstants.FRAME_HEIGHT_ATTRIBUTE.equals(name)) {
			setFrameHeight(Integer.parseInt(value));
			return true;
		}
		if (KuixConstants.FRAME_SEQUENCE_ATTRIBUTE.equals(name)) {
			setFrameSequence(KuixConverter.convertIntArray(value, 1, ","));
			return true;
		}
		if (KuixConstants.FRAME_DURATION_ATTRIBUTE.equals(name)) {
			setFrameDuration(Integer.parseInt(value));
			return true;
		}
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setObjectAttribute(java.lang.String, java.lang.Object)
	 */
	public boolean setObjectAttribute(String name, Object value) {
		if (KuixConstants.IMAGE_ATTRIBUTE.equals(name)) {
			if (value instanceof Image) {
				setImage((Image) value);
			} else {
				setImage(null);
			}
			return true;
		}
		return super.setObjectAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isObjectAttribute(java.lang.String)
	 */
	public boolean isObjectAttribute(String name) {
		if (KuixConstants.IMAGE_ATTRIBUTE.equals(name)) {
			return true;
		}
		return super.isObjectAttribute(name);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String name) {
		if (KuixConstants.IMAGE_ATTRIBUTE.equals(name)) {
			getImage();
		}
		return super.getAttribute(name);
	}

	/**
	 * Define the image source. <br/>Returns the instance of this
	 * {@link Picture} Useful in this case :
	 * <code>Picture picture = new Picture().setSource("img.png");</code>
	 * 
	 * @param source the name of the resource containing the image data in one
	 *            of the supported image formats
	 * @return The instance of this {@link Picture}
	 */
	public Picture setSource(String source) {
		if ((source == null) || (source.length() == 0)) {
			return this;
		}
		if (!source.startsWith("/")) {
			source = new StringBuffer(KuixConstants.DEFAULT_IMG_RES_FOLDER).append(source).toString();
		}
		setImage(ImageManager.instance.getImage(source));
		return this;
	}
	
	/**
	 * @return the associated {@link Image} instance.
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Define the image data. The data is a preloaded {@link Image} object instance.
	 * 
	 * @param image the {@link Image} instance to use in this {@link Picture}.
	 * @return The instance of this {@link Picture}
	 */
	public Picture setImage(Image image) {
		this.image = image;
		if (image != null) {
			if (frameWidth == 0) {
				frameWidth = image.getWidth();
			}
			if (frameHeight == 0) {
				frameHeight = image.getHeight();
			}
		} else {
			frameWidth = 0;
			frameHeight = 0;
		}
		needToResetSprite = true;
		invalidate();
		return this;
	}
	
	/**
	 * @param frameWidth
	 * @since 1.0.1
	 */
	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
		needToResetSprite = true;
		invalidate();
	}

	/**
	 * @param frameHeight
	 * @since 1.0.1
	 */
	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
		needToResetSprite = true;
		invalidate();
	}

	/**
	 * @param frameSequence
	 * @since 1.0.1
	 */
	public void setFrameSequence(int[] frameSequence) {
		this.frameSequence = frameSequence;
		invalidate();
	}
	
	/**
	 * @param frameDuration
	 * @since 1.0.1
	 */
	public void setFrameDuration(int frameDuration) {
		this.frameDuration = frameDuration;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getPreferredSize(int)
	 */
	public Metrics getPreferredSize(int preferredWidth) {
		Metrics metrics;
		if (needToComputePreferredSize(preferredWidth)) {
			metrics = super.getPreferredSize(preferredWidth);
			if (image != null) {
				metrics.width += image.getWidth();
				metrics.height += image.getHeight();
			}
		} else {
			metrics = getCachedMetrics();
		}
		return metrics;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#getDefaultStyleAttributeValue(java.lang.String)
	 */
	protected Object getDefaultStylePropertyValue(String name) {
		if (KuixConstants.ALIGN_STYLE_PROPERTY.equals(name)) {
			return PICTURE_DEFAULT_ALIGN;
		}
		return super.getDefaultStylePropertyValue(name);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#add(org.kalmeo.kuix.widget.Widget)
	 */
	public Widget add(Widget widget) {
		// Impossible to add any child to a picture widget
		return null;
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#doLayout()
	 */
	protected void doLayout() {
		
		if (animationWorkerTask != null) {
			Worker.instance.removeTask(animationWorkerTask);
			animationWorkerTask = null;
		}
		if (image != null) {
			
			if (sprite == null) {
				sprite = new Sprite(image, frameWidth, frameHeight);
				needToResetSprite = false;
			} else if (needToResetSprite) {
				sprite.setImage(image, frameWidth, frameHeight);
				needToResetSprite = false;
			}
			
			if (frameSequence != null) {
				sprite.setFrameSequence(frameSequence);
			}
			
			Insets insets = getInsets();
			int spriteX = insets.left; 
			int spriteY = insets.top;
			
			Alignment alignment = getAlign();
			if (alignment != null) {
				spriteX += alignment.alignX(getWidth() - insets.left - insets.right, frameWidth);
				spriteY += alignment.alignY(getHeight() - insets.top - insets.bottom, frameHeight);
			}
			
			sprite.setPosition(spriteX, spriteY);
			
		}
		
		markAsValidate();
		
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#paintChildrenImpl(javax.microedition.lcdui.Graphics)
	 */
	protected void paintChildrenImpl(Graphics g) {
		if (sprite != null) {
			
			// Push the animation worker task if needed
			if (sprite.getFrameSequenceLength() > 1) {
				if (animationWorkerTask == null) {
					animationWorkerTask = new WorkerTask() {
	
						/* (non-Javadoc)
						 * @see org.kalmeo.util.worker.WorkerTask#run()
						 */
						public boolean run() {
							
							if (isVisible() && System.currentTimeMillis() - lastFrameTime > frameDuration) {
								sprite.nextFrame();
								lastFrameTime = System.currentTimeMillis();
								invalidateAppearance();
							}
							
							// Remove the WorkerTask if the widget is not in the widget tree
							return !isInWidgetTree();
						}
						
					};
				}
				Worker.instance.pushTask(animationWorkerTask);
			}
			
			sprite.paint(g);
		}
	}
	
}
