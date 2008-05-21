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

/**
 * @author bbeaulant
 */
public interface KuixConstants {

	/////////////////////////////////////////////////////////////////////////////////////////
	// Commons
	
	public static final String VERSION										= "1.0.0RC3";
	
	public static final String DEFAULT_XML_RES_FOLDER						= "/xml/";
	public static final String DEFAULT_CSS_RES_FOLDER						= "/css/";
	public static final String DEFAULT_IMG_RES_FOLDER						= "/img/";
	public static final String DEFAULT_I18N_RES_FOLDER						= "/i18n/";
	
	public static final String KUIX_DEFAULT_I18N_MESSAGES_BUNDLE			= "/org/kalmeo/kuix/core/i18n/messages.properties";
	public static final String DEFAULT_I18N_MESSAGES_BUNDLE					= DEFAULT_I18N_RES_FOLDER + "messages.properties";
	public static final String DEFAULT_UNKNOWN_I18N_MESSAGE					= "???";
	
	public static final String PARSE_PROPERTY_START_PATTERN					= "${";
	public static final String BIND_PROPERTY_START_PATTERN					= "@{";
	public static final String PROPERTY_END_PATTERN							= "}";
	public static final String PROPERTY_ALTERNATIVE_SEPRATOR_PATTERN		= "|";
	
	public static final String DEFAULT_CHARSET_NAME							= "UTF-8";
	public static final int SCROLL_BOOSTER_FACTOR							= 2;
	
	public static final short ALERT_DEFAULT									= 0;
	public static final short ALERT_DEBUG									= 1 << 0;
	public static final short ALERT_INFO									= 1 << 1;
	public static final short ALERT_WARNING									= 1 << 2;
	public static final short ALERT_ERROR									= 1 << 3;
	public static final short ALERT_QUESTION								= 1 << 4;
	
	public static final short ALERT_OK										= 1 << 5;
	public static final short ALERT_YES										= 1 << 6;
	public static final short ALERT_NO										= 1 << 7;
	public static final short ALERT_CANCEL									= 1 << 8;
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// JAD app properties
	
	public static final String KUIX_LOCALE_APP_PROPERTY						= "KUIX-Locale";
	public static final String KUIX_FRAME_DURATION_APP_PROPERTY				= "KUIX-Frame-Duration";
	public static final String KUIX_DEBUG_INFOS_KEY_APP_PROPERTY			= "KUIX-Debug-Infos-Key";
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Event types
	
	// KeyEvent
	public static final byte KEY_PRESSED_EVENT_TYPE 						= 10;
	public static final byte KEY_RELEASED_EVENT_TYPE 						= 11;
	public static final byte KEY_REPEATED_EVENT_TYPE 						= 12;

	// PointerEvent
	public static final byte POINTER_PRESSED_EVENT_TYPE 					= 20;
	public static final byte POINTER_RELEASED_EVENT_TYPE 					= 21;
	public static final byte POINTER_DRAGGED_EVENT_TYPE 					= 22;
	public static final byte POINTER_DROPPED_EVENT_TYPE 					= 23;
	
	// PointerEvent
	public static final byte FOCUS_GAINED_EVENT_TYPE 						= 30;
	public static final byte FOCUS_LOST_EVENT_TYPE 							= 31;
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Widget attributes
	
	public static final String ID_ATTRIBUTE 								= "id";
	public static final String CLASS_ATTRIBUTE 								= "class";
	public static final String PACKAGE_ATTRIBUTE 							= "package";
	public static final String STYLE_ATTRIBUTE 								= "style";
	public static final String SRC_ATTRIBUTE 								= "src";
	public static final String IMAGE_DATA_ATTRIBUTE 						= "imagedata";
	public static final String SELECTED_ATTRIBUTE 							= "selected";
	public static final String VALUE_ATTRIBUTE 								= "value";
	public static final String INCREMENT_ATTRIBUTE 							= "increment";
	public static final String SELECTION_ATTRIBUTE 							= "selection";
	public static final String TEXT_ATTRIBUTE 								= "text";
	public static final String TOOLTIP_ATTRIBUTE 							= "tooltip";
	public static final String STYLED_ATTRIBUTE 							= "styled";
	public static final String CONSTRAINTS_ATTRIBUTE 						= "constraints";
	public static final String FOCUSABLE_ATTRIBUTE 							= "focusable";
	public static final String TITLE_ATTRIBUTE 								= "title";
	public static final String GROUP_ATTRIBUTE 								= "group";
	public static final String LABEL_ATTRIBUTE 								= "label";
	public static final String ICON_ATTRIBUTE 								= "icon";
	public static final String VISIBLE_ATTRIBUTE 							= "visible";
	public static final String ENABLED_ATTRIBUTE 							= "enabled";
	public static final String SHORTCUTS_ATTRIBUTE 							= "shortcuts";
	public static final String PRESSED_SHORTCUTS_ATTRIBUTE 					= "pressedshortcuts";
	public static final String REPEATED_SHORTCUTS_ATTRIBUTE 				= "repeatedshortcuts";
	public static final String RELEASED_SHORTCUTS_ATTRIBUTE 				= "releasedshortcuts";
	public static final String USE_MARKERS_ATTRIBUTE 						= "usemarkers";
	public static final String RENDERER_ATTRIBUTE 							= "renderer";
	public static final String ITEMS_ATTRIBUTE 								= "items";
	public static final String DATAPROVIDER_ATTRIBUTE 						= "dataprovider";
	public static final String FOCUS_LOOP_ATTRIBUTE 						= "focusloop";
	public static final String FIRST_IS_LEFT_ATTRIBUTE 						= "firstisleft";
	
	public static final String ON_FOCUS_ATTRIBUTE 							= "onfocus";
	public static final String ON_LOST_FOCUS_ATTRIBUTE 						= "onlostfocus";
	public static final String ON_ACTION_ATTRIBUTE 							= "onaction";
	public static final String ON_CHANGE_ATTRIBUTE 							= "onchange";
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Style properties
	
	public static final String COLOR_STYLE_PROPERTY 						= "color";
	public static final String FONT_FACE_STYLE_PROPERTY 					= "font-face";
	public static final String FONT_STYLE_STYLE_PROPERTY					= "font-style";
	public static final String FONT_SIZE_STYLE_PROPERTY 					= "font-size";
	public static final String BACKGROUND_COLOR_STYLE_PROPERTY 				= "bg-color";
	public static final String BACKGROUND_IMAGE_STYLE_PROPERTY 				= "bg-image";
	public static final String BACKGROUND_ALIGN_STYLE_PROPERTY 				= "bg-align";
	public static final String BACKGROUND_REPEAT_STYLE_PROPERTY 			= "bg-repeat";
	public static final String BORDER_COLOR_STYLE_PROPERTY 					= "border-color";
	public static final String BORDER_IMAGES_STYLE_PROPERTY 				= "border-images";
	public static final String BORDER_STROKE_STYLE_PROPERTY 				= "border-stroke";

	public static final String LAYOUT_STYLE_PROPERTY 						= "layout";
	public static final String LAYOUT_DATA_STYLE_PROPERTY 					= "layout-data";
	
	public static final String MARGIN_STYLE_PROPERTY 						= "margin";
	public static final String BORDER_STYLE_PROPERTY 						= "border";
	public static final String PADDING_STYLE_PROPERTY 						= "padding";
	public static final String GAP_STYLE_PROPERTY 							= "gap";
	public static final String SPAN_STYLE_PROPERTY 							= "span";
	public static final String WEIGHT_STYLE_PROPERTY 						= "weight";
	public static final String ALIGN_STYLE_PROPERTY 						= "align";
	public static final String TRANSITION_STYLE_PROPERTY					= "transition";
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Internal style classes
	
	public static final String TEXT_FIELD_TOOLTIP_STYLE_CLASS 				= "tooltip";
	
	public static final String SPLASH_STYLE_CLASS							= "splash";
	
	public static final String ALERT_DEFAULT_STYLE_CLASS					= "alertdefault";
	public static final String ALERT_DEBUG_STYLE_CLASS						= "alertdebug";
	public static final String ALERT_INFO_STYLE_CLASS						= "alertinfo";
	public static final String ALERT_WARNING_STYLE_CLASS					= "alertwarning";
	public static final String ALERT_ERROR_STYLE_CLASS						= "alerterror";
	public static final String ALERT_QUESTION_STYLE_CLASS					= "alertquestion";
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Styled TextArea tags
	
	public static final String IMAGE_TAG 									= "img";
	public static final String IMAGE_TAG_SRC_ATTRIBUTE						= "src";
	public static final String ANCROR_TAG 									= "a";
	public static final String ANCROR_TAG_HREF_ATTRIBUTE					= "href";
	public static final String BOLD_TAG 									= "b";
	public static final String STRONG_TAG 									= "strong";
	public static final String ITALIC_TAG 									= "i";
	public static final String UNDERLINE_TAG 								= "u";
	public static final String BREAD_RETURN_TAG								= "br";
	public static final String PARAGRAPH_TAG								= "p";
	public static final String DIV_TAG										= "div";
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// Widgets tags
	
	public static final String DEFAULT_WIDGET_TAG 							= "widget";
	public static final String BAR_WIDGET_TAG 								= "bar";
	public static final String BREAK_WIDGET_TAG 							= "break";
	public static final String BUTTON_WIDGET_TAG 							= "button";
	public static final String HYPERLINK_WIDGET_TAG 						= "hyperlink";
	public static final String CHECKBOX_WIDGET_TAG 							= "checkbox";
	public static final String CONTAINER_WIDGET_TAG 						= "container";
	public static final String DESKTOP_WIDGET_TAG 							= "desktop";
	public static final String DRAG_AND_DROP_CONTAINER_WIDGET_TAG 			= "dndcontainer";
	public static final String FIRST_MENU_WIDGET_TAG 						= "firstmenu";
	public static final String LIST_WIDGET_TAG 								= "list";
	public static final String LIST_ITEM_WIDGET_TAG 						= "listitem";
	public static final String MENU_WIDGET_TAG 								= "menu";
	public static final String MENU_ITEM_WIDGET_TAG 						= "menuitem";
	public static final String PICTURE_WIDGET_TAG 							= "picture";
	public static final String POPUP_BOX_WIDGET_TAG 						= "popupbox";
	public static final String POPUP_BOX_CONTAINER_WIDGET_TAG 				= "popupboxcontainer";
	public static final String POPUP_BOX_BUTTONS_CONTAINER_WIDGET_TAG 		= "popupboxbuttonscontainer";
	public static final String POPUP_MENU_WIDGET_TAG 						= "popupmenu";
	public static final String GAUGE_WIDGET_TAG 							= "gauge";
	public static final String RADIO_BUTTON_WIDGET_TAG 						= "radiobutton";
	public static final String RADIO_GROUP_WIDGET_TAG 						= "radiogroup";
	public static final String SCREEN_WIDGET_TAG 							= "screen";
	public static final String SCROLL_BAR_WIDGET_TAG 						= "scrollbar";
	public static final String SCROLL_CONTAINER_WIDGET_TAG 					= "scrollcontainer";
	public static final String SECOND_MENU_WIDGET_TAG 						= "secondmenu";
	public static final String TAB_BUTTON_WIDGET_TAG 						= "tabbutton";
	public static final String TAB_BUTTONS_CONTAINER_WIDGET_TAG 			= "tabbuttonscontainer";
	public static final String TAB_FOLDER_WIDGET_TAG 						= "tabfolder";
	public static final String TAB_ITEM_WIDGET_TAG 							= "tabitem";
	public static final String DEFAULT_TAB_ITEM_WIDGET_TAG 					= "defaulttabitem";
	public static final String TAB_ITEM_CONTAINER_WIDGET_TAG 				= "tabitemcontainer";
	public static final String TEXT_WIDGET_TAG 								= "text";
	public static final String TEXT_AREA_WIDGET_TAG 						= "textarea";
	public static final String TEXT_FIELD_WIDGET_TAG 						= "textfield";
	public static final String TOP_BAR_WIDGET_TAG 							= "topbar";
	public static final String BOTTOM_BAR_WIDGET_TAG 						= "bottombar";

	/////////////////////////////////////////////////////////////////////////////////////////
	// Constants for platforms  names
	
	public static final String PLATFORM_SUN 								= "sun";
	public static final String PLATFORM_MOTOROLA 							= "motorola";
	public static final String PLATFORM_NOKIA 								= "nokia";
	public static final String PLATFORM_SONY_ERICSSON 						= "sonyericsson";
	public static final String PLATFORM_SIEMENS 							= "siemens";
	public static final String PLATFORM_SAMSUNG 							= "samsung";
	public static final String PLATFORM_LG 									= "LG";
	public static final String PLATFORM_INTENT 								= "intent";	// Windows mobile
	public static final String PLATFORM_NOT_DEFINED 						= "NA";

	/////////////////////////////////////////////////////////////////////////////////////////
	// Constants for Kuix internal key codes
	
	public static final int KUIX_KEY_UP 									= 1 << 0;
	public static final int KUIX_KEY_DOWN 									= 1 << 1;
	public static final int KUIX_KEY_LEFT 									= 1 << 2;
	public static final int KUIX_KEY_RIGHT 									= 1 << 3;
	public static final int KUIX_KEY_FIRE 									= 1 << 4;
	
	public static final int KUIX_KEY_SOFT_LEFT 								= 1 << 5;
	public static final int KUIX_KEY_SOFT_RIGHT 							= 1 << 6;
	public static final int KUIX_KEY_SOFT_MIDDLE_INTERNET 					= 1 << 7;
	public static final int KUIX_KEY_PENCIL 								= 1 << 8; // This key is present on Nokia s60
	public static final int KUIX_KEY_DELETE 								= 1 << 9;
	public static final int KUIX_KEY_BACK 									= 1 << 10;
	
	public static final int KUIX_KEY_1 										= 1 << 11;
	public static final int KUIX_KEY_2 										= 1 << 12;
	public static final int KUIX_KEY_3 										= 1 << 13;
	public static final int KUIX_KEY_4 										= 1 << 14;
	public static final int KUIX_KEY_5 										= 1 << 15;
	public static final int KUIX_KEY_6 										= 1 << 16;
	public static final int KUIX_KEY_7 										= 1 << 17;
	public static final int KUIX_KEY_8 										= 1 << 18;
	public static final int KUIX_KEY_9 										= 1 << 19;
	public static final int KUIX_KEY_0 										= 1 << 20;
	public static final int KUIX_KEY_POUND 									= 1 << 21;
	public static final int KUIX_KEY_STAR 									= 1 << 22;

	public static final int NOT_DEFINED_KEY 								= 1 << 30;

}
