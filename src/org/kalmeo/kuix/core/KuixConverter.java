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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import org.kalmeo.kuix.layout.BorderLayout;
import org.kalmeo.kuix.layout.BorderLayoutData;
import org.kalmeo.kuix.layout.FlowLayout;
import org.kalmeo.kuix.layout.GridLayout;
import org.kalmeo.kuix.layout.InlineLayout;
import org.kalmeo.kuix.layout.Layout;
import org.kalmeo.kuix.layout.StaticLayout;
import org.kalmeo.kuix.layout.StaticLayoutData;
import org.kalmeo.kuix.layout.TableLayout;
import org.kalmeo.kuix.transition.FadeTransition;
import org.kalmeo.kuix.transition.SlideTransition;
import org.kalmeo.kuix.transition.Transition;
import org.kalmeo.kuix.util.Alignment;
import org.kalmeo.kuix.util.Color;
import org.kalmeo.kuix.util.Gap;
import org.kalmeo.kuix.util.Insets;
import org.kalmeo.kuix.util.Span;
import org.kalmeo.kuix.util.Weight;
import org.kalmeo.kuix.widget.Button;
import org.kalmeo.kuix.widget.CheckBox;
import org.kalmeo.kuix.widget.Choice;
import org.kalmeo.kuix.widget.DragAndDropContainer;
import org.kalmeo.kuix.widget.Gauge;
import org.kalmeo.kuix.widget.List;
import org.kalmeo.kuix.widget.ListItem;
import org.kalmeo.kuix.widget.Menu;
import org.kalmeo.kuix.widget.MenuItem;
import org.kalmeo.kuix.widget.Picture;
import org.kalmeo.kuix.widget.PopupMenu;
import org.kalmeo.kuix.widget.RadioButton;
import org.kalmeo.kuix.widget.RadioGroup;
import org.kalmeo.kuix.widget.ScrollBar;
import org.kalmeo.kuix.widget.ScrollContainer;
import org.kalmeo.kuix.widget.TabFolder;
import org.kalmeo.kuix.widget.TabItem;
import org.kalmeo.kuix.widget.Text;
import org.kalmeo.kuix.widget.TextArea;
import org.kalmeo.kuix.widget.TextField;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.BooleanUtil;
import org.kalmeo.util.MathFP;
import org.kalmeo.util.StringTokenizer;

/**
 * @author bbeaulant
 */
public class KuixConverter {

	/**
	 * Returns the {@link Class} associated with the specified <code>tag</code>,
	 * or <code>null</code> if the tag is unknow.
	 * 
	 * @param tag
	 * @return the {@link Class} associated with the specified <code>tag</code>
	 */
	public Widget convertWidgetTag(String tag) {
		if (KuixConstants.TEXT_WIDGET_TAG.equals(tag)) {
			return new Text();
		}
		if (KuixConstants.TEXT_FIELD_WIDGET_TAG.equals(tag)) {
			return new TextField();
		}
		if (KuixConstants.TEXT_AREA_WIDGET_TAG.equals(tag)) {
			return new TextArea();
		}
		if (KuixConstants.BREAK_WIDGET_TAG.equals(tag)) {
			return new Widget(KuixConstants.BREAK_WIDGET_TAG);
		}
		if (KuixConstants.CONTAINER_WIDGET_TAG.equals(tag)) {
			return new Widget(KuixConstants.CONTAINER_WIDGET_TAG);
		}
		if (KuixConstants.SCROLL_CONTAINER_WIDGET_TAG.equals(tag)) {
			return new ScrollContainer();
		}
		if (KuixConstants.DRAG_AND_DROP_CONTAINER_WIDGET_TAG.equals(tag)) {
			return new DragAndDropContainer();
		}
		if (KuixConstants.PICTURE_WIDGET_TAG.equals(tag)) {
			return new Picture();
		}
		if (KuixConstants.BUTTON_WIDGET_TAG.equals(tag)) {
			return new Button();
		}
		if (KuixConstants.HYPERLINK_WIDGET_TAG.equals(tag)) {
			return new Button(KuixConstants.HYPERLINK_WIDGET_TAG);
		}
		if (KuixConstants.CHECKBOX_WIDGET_TAG.equals(tag)) {
			return new CheckBox();
		}
		if (KuixConstants.RADIO_BUTTON_WIDGET_TAG.equals(tag)) {
			return new RadioButton();
		}
		if (KuixConstants.RADIO_GROUP_WIDGET_TAG.equals(tag)) {
			return new RadioGroup();
		}
		if (KuixConstants.GAUGE_WIDGET_TAG.equals(tag)) {
			return new Gauge();
		}
		if (KuixConstants.LIST_WIDGET_TAG.equals(tag)) {
			return new List();
		}
		if (KuixConstants.LIST_ITEM_WIDGET_TAG.equals(tag)) {
			return new ListItem();
		}
		if (KuixConstants.TAB_FOLDER_WIDGET_TAG.equals(tag)) {
			return new TabFolder();
		}
		if (KuixConstants.TAB_ITEM_WIDGET_TAG.equals(tag)) {
			return new TabItem();
		}
		if (KuixConstants.DEFAULT_TAB_ITEM_WIDGET_TAG.equals(tag)) {
			return new TabItem(KuixConstants.DEFAULT_TAB_ITEM_WIDGET_TAG, null);
		}
		if (KuixConstants.MENU_WIDGET_TAG.equals(tag)) {
			return new Menu();
		}
		if (KuixConstants.POPUP_MENU_WIDGET_TAG.equals(tag)) {
			return new PopupMenu();
		}
		if (KuixConstants.MENU_ITEM_WIDGET_TAG.equals(tag)) {
			return new MenuItem();
		}
		if (KuixConstants.SCROLL_BAR_WIDGET_TAG.equals(tag)) {
			return new ScrollBar();
		}
		if (KuixConstants.CHOICE_WIDGET_TAG.equals(tag)) {
			return new Choice();
		}
		return null;
	}

	/**
	 * Convert a property raw data string into a specific object instance.
	 * 
	 * @param name
	 * @param rawData
	 * @return a specific object instance.
	 */
	public Object convertStyleProperty(String name, String rawData) throws IllegalArgumentException {

		// Color
		if (KuixConstants.COLOR_STYLE_PROPERTY.equals(name) 
				|| KuixConstants.BACKGROUND_COLOR_STYLE_PROPERTY.equals(name) 
				|| KuixConstants.BORDER_COLOR_STYLE_PROPERTY.equals(name)) {
			return convertColor(rawData);
		}

		// Font face
		if (KuixConstants.FONT_FACE_STYLE_PROPERTY.equals(name)) {
			return convertFontFace(rawData);
		}
		
		// Font style
		if (KuixConstants.FONT_STYLE_STYLE_PROPERTY.equals(name)) {
			return convertFontStyle(rawData);
		}
		
		// Font size
		if (KuixConstants.FONT_SIZE_STYLE_PROPERTY.equals(name)) {
			return convertFontSize(rawData);
		}
		
		// Stroke
		if (KuixConstants.BORDER_STROKE_STYLE_PROPERTY.equals(name)) {
			return convertStroke(rawData);
		}
		
		// Inset
		if (KuixConstants.MARGIN_STYLE_PROPERTY.equals(name) 
				|| KuixConstants.BORDER_STYLE_PROPERTY.equals(name) 
				|| KuixConstants.PADDING_STYLE_PROPERTY.equals(name)) {
			return convertInset(rawData);
		}

		// Gap
		if (KuixConstants.GAP_STYLE_PROPERTY.equals(name)) {
			return convertGap(rawData);
		}

		// Span
		if (KuixConstants.SPAN_STYLE_PROPERTY.equals(name)) {
			return convertSpan(rawData);
		}

		// Weight
		if (KuixConstants.WEIGHT_STYLE_PROPERTY.equals(name)) {
			return convertWeight(rawData);
		}

		// Align
		if (KuixConstants.ALIGN_STYLE_PROPERTY.equals(name)
				|| KuixConstants.BACKGROUND_ALIGN_STYLE_PROPERTY.equals(name)) {
			return convertAlignment(rawData);
		}
		if (KuixConstants.BORDER_ALIGN_STYLE_PROPERTY.equals(name)) {
			return convertAlignmentArray(rawData, 8);
		}

		// Image
		if (KuixConstants.BACKGROUND_IMAGE_STYLE_PROPERTY.equals(name)) {
			return convertImage(rawData);
		}
		if (KuixConstants.BORDER_IMAGES_STYLE_PROPERTY.equals(name)) {
			return convertImageArray(rawData, 8);
		}
		
		// Layout
		if (KuixConstants.LAYOUT_STYLE_PROPERTY.equals(name)) {
			return convertLayout(rawData);
		}
		if (KuixConstants.LAYOUT_DATA_STYLE_PROPERTY.equals(name)) {
			return convertLayoutData(rawData);
		}
		
		// Background repeat
		if (KuixConstants.BACKGROUND_REPEAT_STYLE_PROPERTY.equals(name)) {
			return convertIntArray(rawData, 2, " ");
		}
		
		// Transition
		if (KuixConstants.TRANSITION_STYLE_PROPERTY.equals(name)) {
			return convertTransition(rawData);
		}
		
		throw new IllegalArgumentException("Unknow style name " + name);
	}
	
	/**
	 * @param rawData
	 * @return The converted {@link Transition}
	 */
	public Transition convertTransition(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		if ((rawParams = extractRawParams("slide", rawData)) != null) {
			Alignment alignment = convertAlignment(rawParams);
			return new SlideTransition(alignment);
		}
		if ((rawParams = extractRawParams("fade", rawData)) != null) {
			return new FadeTransition(Integer.parseInt(rawParams));
		}
		throw new IllegalArgumentException("Bad transition value");
	}
	
	/**
	 * @param rawData
	 * @return The converted {@link Color}
	 */
	protected Color convertColor(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		if (rawData != null && rawData.startsWith("#")) {
			return new Color(Integer.parseInt(rawData.substring(1), 16));
		}
		if ("red".equals(rawData)) {
			return Color.RED;
		}
		if ("green".equals(rawData)) {
			return Color.GREEN;
		}
		if ("blue".equals(rawData)) {
			return Color.BLUE;
		}
		if ("white".equals(rawData)) {
			return Color.WHITE;
		}
		if ("black".equals(rawData)) {
			return Color.BLACK;
		}
		throw new IllegalArgumentException("Bad color value");
	}

	/**
	 * @param rawData
	 * @return The converted font face
	 */
	protected Integer convertFontFace(String rawData) {
		int face = Font.FACE_SYSTEM;
			
		// Face (system|monospace|proportional)
		if ("monospace".equals(rawData)) {
			face = Font.FACE_MONOSPACE;
		} else if ("proportional".equals(rawData)) {
			face = Font.FACE_PROPORTIONAL;
		}
		
		return new Integer(face);
	}
	
	/**
	 * @param rawData
	 * @return The converted font style
	 */
	protected Integer convertFontStyle(String rawData) {
		StringTokenizer values = new StringTokenizer(rawData);
		int style = Font.STYLE_PLAIN;
		while (values.hasMoreTokens()) {
			
			String fontAttribute = ((String) values.nextElement()).toLowerCase();
			
			// Style (plain|bold|italic|underline)
			if ("bold".equals(fontAttribute)) {
				style |= Font.STYLE_BOLD;
			} else if ("italic".equals(fontAttribute)) {
				style |= Font.STYLE_ITALIC;
			} else if ("underlined".equals(fontAttribute)) {
				style |= Font.STYLE_UNDERLINED;
			}
			
		}
		return new Integer(style);
	}
	
	/**
	 * @param rawData
	 * @return The converted font size
	 */
	protected Integer convertFontSize(String rawData) {
		int size = Font.SIZE_MEDIUM;
			
		// Size (medium|large|small)
		if ("large".equals(rawData)) {
			size = Font.SIZE_LARGE;
		} else if ("small".equals(rawData)) {
			size = Font.SIZE_SMALL;
		}
		
		return new Integer(size);
	}
	
	/**
	 * @param rawData
	 * @return The converted stoke
	 */
	protected Integer convertStroke(String rawData) {
		int stroke = Graphics.SOLID;
		
		// Size (solid|dotted)
		if ("dotted".equals(rawData)) {
			stroke = Graphics.DOTTED;
		}
		
		return new Integer(stroke);
	}
	
	/**
	 * @param rawData
	 * @return The converted {@link Inset}
	 */
	protected Insets convertInset(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 1, StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			if (intValues.length == 1) {
				return new Insets(intValues[0], intValues[0], intValues[0], intValues[0]);
			} else {
				return new Insets(intValues[0], intValues[1], intValues[2], intValues[3]);
			}
		}
		throw new IllegalArgumentException("Bad inset value");
	}

	/**
	 * @param rawData
	 * @return The converted {@link Gap}
	 */
	protected Gap convertGap(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 1, StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			if (intValues.length == 1) {
				return new Gap(intValues[0], intValues[0]);
			} else {
				return new Gap(intValues[0], intValues[1]);
			}
		}
		throw new IllegalArgumentException("Bad gap value");
	}

	/**
	 * @param rawData
	 * @return The converted {@link Span}
	 */
	protected Span convertSpan(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 2, StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			return new Span(intValues[0], intValues[1]);
		}
		throw new IllegalArgumentException("Bad span value");
	}

	/**
	 * @param rawData
	 * @return The converted {@link Weight}
	 */
	protected Weight convertWeight(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		int[] intValues = convertIntArray(rawData, 2, StringTokenizer.DEFAULT_DELIM);
		if (intValues != null) {
			return new Weight(intValues[0], intValues[1]);
		}
		throw new IllegalArgumentException("Bad weight value");
	}

	/**
	 * @param rawData
	 * @return The converted {@link Alignment}
	 */
	protected Alignment convertAlignment(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData);
		while (values.hasMoreTokens()) {

			String alignmentValue = ((String) values.nextElement()).toLowerCase();

			if ("top-left".equals(alignmentValue)) {
				return Alignment.TOP_LEFT;
			} else if ("top".equals(alignmentValue)) {
				return Alignment.TOP;
			} else if ("top-right".equals(alignmentValue)) {
				return Alignment.TOP_RIGHT;
			} else if ("left".equals(alignmentValue)) {
				return Alignment.LEFT;
			} else if ("center".equals(alignmentValue)) {
				return Alignment.CENTER;
			} else if ("right".equals(alignmentValue)) {
				return Alignment.RIGHT;
			} else if ("bottom-left".equals(alignmentValue)) {
				return Alignment.BOTTOM_LEFT;
			} else if ("bottom".equals(alignmentValue)) {
				return Alignment.BOTTOM;
			} else if ("bottom-right".equals(alignmentValue)) {
				return Alignment.BOTTOM_RIGHT;
			} else if ("fill".equals(alignmentValue)) {
				return Alignment.FILL;
			} else if ("fill-top".equals(alignmentValue)) {
				return Alignment.FILL_TOP;
			} else if ("fill-left".equals(alignmentValue)) {
				return Alignment.FILL_LEFT;
			} else if ("fill-center".equals(alignmentValue)) {
				return Alignment.FILL_CENTER;
			} else if ("fill-right".equals(alignmentValue)) {
				return Alignment.FILL_RIGHT;
			} else if ("fill-bottom".equals(alignmentValue)) {
				return Alignment.FILL_BOTTOM;
			}
		}
		throw new IllegalArgumentException("Bad alignment value");
	}

	/**
	 * @param rawData
	 * @param wantedSize
	 * @return The converted Alignment[]
	 */
	protected Alignment[] convertAlignmentArray(String rawData, int wantedSize) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData);
		if (values.countTokens() >= wantedSize) {
			Alignment[] alignments = new Alignment[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					alignments[i] = convertAlignment((String) values.nextElement());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return alignments;
		}
		throw new IllegalArgumentException("Bad alignments value");
	}
	
	/**
	 * @param rawData
	 * @return The converted {@link Image}
	 */
	protected Image convertImage(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		if ((rawParams = extractRawParams("url", rawData)) != null) {
			StringTokenizer st = new StringTokenizer(rawParams, ", ");
			int numTokens = st.countTokens();
			if (numTokens >= 1) {
				Image fullImage = null;
				String imgSrc = st.nextToken();
				if (!imgSrc.startsWith("/")) {
					// By default the relative path point to /img
					imgSrc = new StringBuffer(KuixConstants.DEFAULT_IMG_RES_FOLDER).append(imgSrc).toString();
				}
				try {
					fullImage = Image.createImage(imgSrc);
				} catch (IOException e) {
					System.err.println("Error loading : " + imgSrc);
				}
				if (numTokens >= 5) {
					int x = Integer.parseInt(st.nextToken());
					int y = Integer.parseInt(st.nextToken());
					int width = Integer.parseInt(st.nextToken());
					int height = Integer.parseInt(st.nextToken());
					int transform = Sprite.TRANS_NONE;
					if (numTokens == 6) {
						String transformStr = st.nextToken();
						if (transformStr != null) {
							if (transformStr.equals("mirror")) {
								transform = Sprite.TRANS_MIRROR;
							} else if (transformStr.equals("mirror_rot270")) {
								transform = Sprite.TRANS_MIRROR_ROT270;
							} else if (transformStr.equals("mirror_rot180")) {
								transform = Sprite.TRANS_MIRROR_ROT180;
							} else if (transformStr.equals("mirror_rot90")) {
								transform = Sprite.TRANS_MIRROR_ROT90;
							} else if (transformStr.equals("rot270")) {
								transform = Sprite.TRANS_ROT270;
							} else if (transformStr.equals("rot180")) {
								transform = Sprite.TRANS_ROT180;
							} else if (transformStr.equals("rot90")) {
								transform = Sprite.TRANS_ROT90;
							}
						}
					}
					try {
						return Image.createImage(fullImage, x, y, width, height, transform);
					} catch (Exception e) {
						System.err.println("Error loading custom : " + imgSrc);
					}
				} else {
					return fullImage;
				}
			}
		}
		throw new IllegalArgumentException("Bad image value");
	}
	
	/**
	 * @param rawData
	 * @param wantedSize
	 * @return The converted Image[]
	 */
	protected Image[] convertImageArray(String rawData, int wantedSize) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData);
		if (values.countTokens() >= wantedSize) {
			Image[] images = new Image[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					images[i] = convertImage((String) values.nextElement());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return images;
		}
		throw new IllegalArgumentException("Bad images value");
	}
	
	/**
	 * @param rawData
	 * @return The converted {@link Layout}
	 */
	protected Layout convertLayout(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		if ((rawParams = extractRawParams("inlinelayout", rawData)) != null) {
			StringTokenizer st = new StringTokenizer(rawParams, ", ");
			if (st.countTokens() >= 2) {
				boolean horizontal = BooleanUtil.parseBoolean(st.nextToken());
				Alignment alignment = convertAlignment(st.nextToken());
				if (alignment != null) {
					return new InlineLayout(horizontal, alignment);
				} 
				return new InlineLayout(horizontal);
			}
			return new InlineLayout();
		} else if ((rawParams = extractRawParams("flowlayout", rawData)) != null) {
			Alignment alignment = convertAlignment(rawParams);
			if (alignment != null) {
				return new FlowLayout(alignment);
			} 
			return new FlowLayout();
		} else if (rawData.startsWith("tablelayout")) {
			return TableLayout.instance;
		} else if (rawData.startsWith("borderlayout")) {
			return BorderLayout.instance;
		} else if ((rawParams = extractRawParams("gridlayout", rawData)) != null) {
			StringTokenizer st = new StringTokenizer(rawParams, ", ");
			if (st.countTokens() >= 2) {
				int numCols = Integer.valueOf(st.nextToken()).intValue();
				int numRows = Integer.valueOf(st.nextToken()).intValue();
				return new GridLayout(numCols, numRows);
			}
		} else if (rawData.startsWith("staticlayout")) {
			return StaticLayout.instance;
		}
		throw new IllegalArgumentException("Bad layout value");
	}

	/**
	 * @param rawData
	 * @return The converted LayoutData
	 */
	protected Object convertLayoutData(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		String rawParams = null;
		// BorderLayouData
		if ((rawParams = extractRawParams("bld", rawData)) != null) {
			if ("north".equals(rawParams)) {
				return BorderLayoutData.instanceNorth;
			} else if ("west".equals(rawParams)) {
				return BorderLayoutData.instanceWest;
			} else if ("east".equals(rawParams)) {
				return BorderLayoutData.instanceEast;
			} else if ("south".equals(rawParams)) {
				return BorderLayoutData.instanceSouth;
			}
			return BorderLayoutData.instanceCenter;
		}
		// StaticLayoutData
		if ((rawParams = extractRawParams("sld", rawData)) != null) {
			int pos = rawParams.indexOf(",");
			if (pos != -1) {
				try {
					Alignment alignment = convertAlignment(rawParams.substring(0, pos));
					int[] values = convertFPArray(rawParams.substring(pos + 1), 2, ", ");
					if (values != null) {
						return new StaticLayoutData(alignment, values[0], values[1]);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		throw new IllegalArgumentException("Bad layout data value");
	}
	
	/**
	 * @param rawData
	 * @return The converted style classes
	 */
	public static String[] convertStyleClasses(String rawData) {
		if (isNone(rawData)) {
			return null;
		}
		StringTokenizer values = new StringTokenizer(rawData);
		if (values.hasMoreTokens()) {
			String[] styleClasses = new String[values.countTokens()];
			int i = 0;
			while (values.hasMoreTokens()) {
				styleClasses[i++] = values.nextToken().toLowerCase();
			}
			return styleClasses;
		} else {
			throw new IllegalArgumentException("Bad class value");
		}
	}

	/**
	 * Convert a shortcuts (like "left|right=mysAction|1|*") string definition
	 * to internal representation. The result is a byte array where first 4
	 * bytes represents all key codes masks, and other bytes represent a list of
	 * key code / action couple.
	 * 
	 * @param rawData
	 * @return The shortcut kuix key code converted byte array.
	 */
	public static byte[] convertShortcuts(String rawData) {
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);
		
		int shortcutKuixKeyCodes = 0;
		StringTokenizer values = new StringTokenizer(rawData, "|");
		String value = null;
		String action;
		int kuixKeyCode;
		while (values.hasMoreTokens()) {
			value = values.nextToken().trim();
			
			kuixKeyCode = 0;
			action = null;
			
			// Check if definition is like 'key=action'
			int equalityPos = value.indexOf('=');
			if (equalityPos != -1) {
				action = value.substring(equalityPos + 1);
				value = value.substring(0, equalityPos);
			}
			
			if ("0".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_0;
			} else if ("1".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_1;
			} else if ("2".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_2;
			} else if ("3".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_3;
			} else if ("4".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_4;
			} else if ("5".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_5;
			} else if ("6".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_6;
			} else if ("7".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_7;
			} else if ("8".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_8;
			} else if ("9".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_9;
			} else if ("*".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_STAR;
			} else if ("#".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_POUND;
			} else if ("softleft".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_SOFT_LEFT;
			} else if ("softright".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_SOFT_RIGHT;
			} else if ("up".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_UP;
			} else if ("left".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_LEFT;
			} else if ("right".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_RIGHT;
			} else if ("down".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_DOWN;
			} else if ("fire".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_FIRE;
			} else if ("back".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_BACK;
			} else if ("delete".equals(value)) {
				kuixKeyCode = KuixConstants.KUIX_KEY_DELETE;
			}
			
			if (kuixKeyCode != 0) {
				shortcutKuixKeyCodes |= kuixKeyCode;
				if (action != null) {
					try {
						outputStream.writeInt(kuixKeyCode);	// (4 bytes)
						outputStream.writeUTF(action);		// length (2 bytes) + action
					} catch (IOException e) {
					}
				}
			}
		}
		if (shortcutKuixKeyCodes != 0) {
			byte[] actions = byteArrayOutputStream.toByteArray();
			byteArrayOutputStream.reset();
			try {
				outputStream.writeInt(shortcutKuixKeyCodes);
				outputStream.write(actions);
			} catch (IOException e) {
				return null;
			}
			return byteArrayOutputStream.toByteArray();
		}
		return null;
	}
	
	/**
	 * Fill <code>binds</code> hastable with converted binds instructions.
	 * 
	 * @param rawData
	 * @return the <code>binds</code> hastable
	 */
	public Hashtable convertBinds(String rawData, Hashtable binds) {
		if (binds != null) {
			binds.clear();
		} else {
			binds = new Hashtable();
		}
		StringTokenizer bindInstructions = new StringTokenizer(rawData, "; ");
		while (bindInstructions.hasMoreTokens()) {
			String bindInstruction = bindInstructions.nextToken();
			if (bindInstruction.length() >= 3) {
				int separatorPos = bindInstruction.indexOf(':');
				if (separatorPos != -1) {
					String attribute = bindInstruction.substring(0, separatorPos);
					String property = bindInstruction.substring(separatorPos + 1);
					binds.put(property, attribute);
				}
			}
		}
		return binds;
	}
	
	/**
	 * Extract parameters from a prefix(params) syntaxed string
	 *  
	 * @param prefix
	 * @param rawData
	 * @return The paramters full string
	 */
	public static String extractRawParams(String prefix, String rawData) {
		if (rawData.startsWith(prefix)) {
			int posStart = rawData.indexOf(prefix + "(");
			int posEnd = rawData.indexOf(")");
			if (posStart != -1 && posEnd != -1 && posStart < posEnd) {
				return rawData.substring(posStart + prefix.length() + 1, posEnd);
			}
		}
		return null;
	}

	/**
	 * @param rawData
	 * @param wantedMinSize
	 * @param delim
	 * @return The converted int[]
	 */
	public int[] convertIntArray(String rawData, int wantedMinSize, String delim) {
		StringTokenizer values = new StringTokenizer(rawData, delim);
		if (values.countTokens() >= wantedMinSize) {
			int[] intValues = new int[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					intValues[i] = Integer.parseInt(values.nextToken());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return intValues;
		}
		return null;
	}

	/**
	 * @param rawData
	 * @param wantedSize
	 * @param delim
	 * @return The converted fixed-point int[]
	 */
	public int[] convertFPArray(String rawData, int wantedSize, String delim) {
		StringTokenizer values = new StringTokenizer(rawData, delim);
		if (values.countTokens() >= wantedSize) {
			int[] fpValues = new int[values.countTokens()];
			for (int i = 0; values.hasMoreTokens(); ++i) {
				try {
					fpValues[i] = MathFP.toFP(values.nextToken());
					continue;
				} catch (Exception e) {
					return null;
				}
			}
			return fpValues;
		}
		return null;
	}
	
	/**
	 * Check if the given <code>rawData</code> is 'none'
	 * 
	 * @param rawData
	 * @return <code>true</code> if rawData equals "none"
	 */
	protected static boolean isNone(String rawData) {
		return ("none".equals(rawData));
	}
	
}
