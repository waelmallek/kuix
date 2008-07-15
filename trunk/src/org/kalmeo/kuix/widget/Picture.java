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

import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Metrics;
import org.kalmeo.util.resource.ImageManager;

/**
 * This class represent a picture.
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
 * 		<td> <code>src</code> </th>
 * 		<td> <code>No</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the image source file path. By default if the path is relative (do not start by '/'), the path is relative to '/img/'. This attribute is not compatible with <code>imagedata</code>. </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>imagedata</code> </th>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>Yes</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> Define the image data source. The data could be extract from image file stream, like png, gif or jpeg.<br/> Implementations are required to support images stored in the PNG format. This attribute is not compatible with <code>src</code>.  </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td colspan="5"> Inherited attributes : see {@link Widget} </td>
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
 * 		<td> <code>align</code> </th>
 * 		<td> <code>center</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> see {@link Widget} </td>
 *	</tr>
 * 	<tr class="TableRowColor">
 * 		<td> <code>layout</code> </th>
 * 		<td> <code>null</code> </td>
 * 		<td> <code>No</code> </td>
 * 		<td> <b>Uneditable</b>, see {@link Widget} </td>
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
 * 		<td colspan="2"> Inherited internal widgets : see {@link Widget} </td>
 * 	</tr>
 * </table>
 * 
 * @author bbeaulant
 */
public class Picture extends Widget {

	// The default Picture's aligment
	private static final Alignment PICTURE_DEFAULT_ALIGN = Alignment.CENTER;

	// The picture's image
	private Image image;
	
	// Image coordinates
	protected int imageX;
	protected int imageY;

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
		return super.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#setObjectAttribute(java.lang.String, java.lang.Object)
	 */
	public boolean setObjectAttribute(String name, Object value) {
		if (KuixConstants.IMAGE_DATA_ATTRIBUTE.equals(name)) {
			if (value instanceof Image) {
				setImageData((Image) value);
			} else if (value instanceof byte[]) {
				setImageData((byte[]) value);
			} else {
				boolean needToInvalidate = image == null;
				image = null;
				if (needToInvalidate) {
					invalidate();
				}
			}
			return true;
		}
		return super.setObjectAttribute(name, value);
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#isObjectAttribute(java.lang.String)
	 */
	public boolean isObjectAttribute(String name) {
		if (KuixConstants.IMAGE_DATA_ATTRIBUTE.equals(name)) {
			return true;
		}
		return super.isObjectAttribute(name);
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
		image = ImageManager.instance.getImage(source);
		invalidate();
		return this;
	}
	
	/**
	 * Define the image data. The data could be extract from image file stream,
	 * like png, gif or jpeg.<br/> Implementations are required to support
	 * images stored in the PNG format.
	 * 
	 * @param imageData the array of image data in a supported image format
	 * @return The instance of this {@link Picture}
	 */
	public Picture setImageData(byte[] imageData) {
		if (imageData == null) {
			image = null;
		} else {
			image = Image.createImage(imageData, 0, imageData.length);
		}
		invalidate();
		return this;
	}
	
	/**
	 * Define the image data. The data is a preloaded {@link Image} object instance.
	 * 
	 * @param image the {@link Image} instance to use in this {@link Picture}.
	 * @return The instance of this {@link Picture}
	 */
	public Picture setImageData(Image image) {
		this.image = image;
		invalidate();
		return this;
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
		
		Insets insets = getInsets();
		imageX = insets.left; 
		imageY = insets.top;
		
		if (image != null) {
			Alignment alignment = getAlign();
			if (alignment != null) {
				imageX += alignment.alignX(getWidth() - insets.left - insets.right, image.getWidth());
				imageY += alignment.alignY(getHeight() - insets.top - insets.bottom, image.getHeight());
			}

		}
		
		markAsValidate();
		
	}

	/* (non-Javadoc)
	 * @see org.kalmeo.kuix.widget.Widget#paintChildrenImpl(javax.microedition.lcdui.Graphics)
	 */
	protected void paintChildrenImpl(Graphics g) {
		if (image != null) {
			g.drawImage(image, 
						imageX, 
						imageY, 
						Graphics.SOLID);
		}
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

}
