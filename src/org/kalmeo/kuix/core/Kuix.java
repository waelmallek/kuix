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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UTFDataFormatException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.core.style.Style;
import org.kalmeo.kuix.core.style.StyleProperty;
import org.kalmeo.kuix.core.style.StyleSelector;
import org.kalmeo.kuix.util.Method;
import org.kalmeo.kuix.widget.Picture;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Text;
import org.kalmeo.kuix.widget.TextWidget;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.Filter;
import org.kalmeo.util.LinkedList;
import org.kalmeo.util.StringTokenizer;
import org.kalmeo.util.StringUtil;
import org.kalmeo.util.frame.FrameHandler;
import org.kalmeo.util.xml.LightXmlParser;
import org.kalmeo.util.xml.LightXmlParserHandler;

/**
 * This class is the central class for Kuix framework management. It pertmits to
 * load XML files, load CSS files. It contains the {@link FrameHandler} object
 * instance that manages Frames.
 * 
 * @author bbeaulant
 */
public final class Kuix {
	
	// FrameHandler
	private static final FrameHandler frameHandler = new FrameHandler();

	// List of registred styles
	private static final LinkedList registredStyles = new LinkedList();

	// Converter
	private static KuixConverter converter = new KuixConverter();
	
	/**
	 * Construct a {@link Kuix}
	 */
	private Kuix() {
		// Private constructor, no need to instanciate this class.
	}
	
	/**
	 * @return the frameHandler
	 */
	public static FrameHandler getFrameHandler() {
		return frameHandler;
	}

	/**
	 * @return the converter
	 */
	public static KuixConverter getConverter() {
		return converter;
	}

	/**
	 * @param converter the converter to set
	 */
	public static void setConverter(KuixConverter converter) {
		Kuix.converter = converter;
	}
	
	/**
	 * Clean all instances
	 */
	public static void cleanUp() {
		frameHandler.removeAllFrames();
		removeAllStyles();
	}
	
	/**
	 * Load a {@link Screen} widget from a XML file. If <code>xmlFilePath</code>
	 * is a relative path (i.e: <code>myScreen.xml</code>) the default xml
	 * folder location is automaticaly added and the path become :
	 * <code>/xml/myScreen.xml</code>. Absolute paths are kept.
	 * 
	 * @param xmlFilePath
	 * @param dataProvider
	 * @return The loaded {@link Screen} widget instance
	 */
	public static Screen loadScreen(String xmlFilePath, DataProvider dataProvider) {
		return loadScreen(getXmlResourceInputStream(xmlFilePath), dataProvider);
	}
	
	/**
	 * Load a {@link Screen} widget from an XML {@link InputStream}.
	 * 
	 * @param inputStream
	 * @param dataProvider
	 * @return The loaded {@link Screen} widget instance
	 */
	public static Screen loadScreen(InputStream inputStream, DataProvider dataProvider) {
		Screen screen = new Screen();
		loadXml(screen, inputStream, dataProvider, true);
		return screen;
	}
	
	/**
	 * Load a {@link Widget}, exactly a container (tag =
	 * KuixConstants.CONTAINER_WIDGET_TAG), from a XML file. If
	 * <code>xmlFilePath</code> is a relative path (i.e:
	 * <code>myWidget.xml</code>) the default xml folder location is
	 * automaticaly added and the path become : <code>/xml/myWidget.xml</code>.
	 * Absolute paths are kept.
	 * 
	 * @param xmlFilePath
	 * @param dataProvider
	 * @return The loaded {@link Widget} instance
	 */
	public static Widget loadWidget(String xmlFilePath, DataProvider dataProvider) {
		return loadWidget(getXmlResourceInputStream(xmlFilePath), dataProvider);
	}
	
	/**
	 * Load a {@link Widget}, exactly a container (tag =
	 * KuixConstants.CONTAINER_WIDGET_TAG), from an XML {@link InputStream}.<br>
	 * The <code>desiredWidgetClass</code> need to extends {@link Widget} and
	 * correspond to the root xml widget tag.
	 * 
	 * @param inputStream
	 * @param dataProvider
	 * @return The loaded {@link Widget} instance
	 */
	public static Widget loadWidget(InputStream inputStream, DataProvider dataProvider) {
		Widget widget = new Widget(KuixConstants.CONTAINER_WIDGET_TAG);
		loadXml(widget, inputStream, dataProvider, true);
		return widget;
	}
	
	/**
	 * Parse and load an XML ui definition and place the content as child of
	 * <code>widget</code>
	 * 
	 * @param rootWidget
	 * @param inputStream
	 * @throws Exception
	 */
	public static void loadXml(Widget rootWidget, InputStream inputStream) {
		loadXml(rootWidget, inputStream, null, false);
	}

	/**
	 * Parse and load an XML ui definition and place the content as child of
	 * <code>widget</code>
	 * 
	 * @param rootWidget
	 * @param inputStream
	 * @param dataProvider
	 * @throws Exception
	 */
	public static void loadXml(Widget rootWidget, InputStream inputStream, DataProvider dataProvider) {
		loadXml(rootWidget, inputStream, dataProvider, false);
	}

	/**
	 * Parse an load an XML ui definition and place the content as child of
	 * <code>widget</code>
	 * 
	 * @param rootWidget
	 * @param inputStream
	 * @param dataProvider
	 * @param append if <code>false</code> loaded content replace current
	 *            <code>widget</code>'s content.
	 * @throws Exception
	 */
	public static void loadXml(Widget rootWidget, InputStream inputStream, DataProvider dataProvider, boolean append) {
		if (!append) {
			rootWidget.removeAll();
		}
		parseXml(converter, rootWidget, inputStream, dataProvider);
		rootWidget.invalidate();
	}

	/**
	 * Parse an load a CSS style definitions and register it into the
	 * StyleManager. If <code>cssFilePath</code> is a relative path (i.e:
	 * <code>myStyle.css</code>) the default css folder location is
	 * automaticaly added and the path become : <code>/css/myStyle.css</code>.
	 * Absolute paths are kept.
	 * 
	 * @param cssFilePath
	 */
	public static void loadCss(String cssFilePath) {
		if (cssFilePath != null) {
			if (!cssFilePath.startsWith("/")) {
				cssFilePath = new StringBuffer(KuixConstants.DEFAULT_CSS_RES_FOLDER).append(cssFilePath).toString();
			}
			// Use frameHandler.getClass() because of a Object.class bug
			loadCss(frameHandler.getClass().getResourceAsStream(cssFilePath));
			return;
		}
		throw new IllegalArgumentException("Invalid cssFilePath");
	}
	
	/**
	 * Parse an load a CSS style definitions and register them.
	 * 
	 * @param inputStream
	 */
	public static void loadCss(InputStream inputStream) {
		parseCss(converter, inputStream);
		// Clear all style caches to use new loaded styles
		clearStyleCache(KuixMIDlet.getDefault().getCanvas().getDesktop(), true);
	}

	/**
	 * Call the specified action method
	 * 
	 * @param method
	 */
	public static void callActionMethod(Method method) {
		if (method != null) {
			if (!frameHandler.processMessage(method.getName(), method.getArguments())) {
				
				// Default KUIX actions
				//////////////////////////////////////////////////////////////////////
				
				// Exit (exits the application)
				if (KuixConstants.EXIT_ACTION.equals(method.getName())) {
					KuixMIDlet.getDefault().destroyImpl();
				}
				
			}
		}
	}

	/**
	 * Returns a new {@link InputStream} relative to the desired
	 * <code>xmlFilePath</code>.<br>
	 * If <code>xmlFilePath</code> is a relative path (i.e:
	 * <code>myResource.xml</code>) the default xml folder location is
	 * automaticaly added and the path become : <code>/xml/myResource.xml</code>.
	 * Absolute paths are kept.
	 * 
	 * @param xmlFilePath
	 * @return a new {@link InputStream} relative to the desired xml resource
	 */
	public static InputStream getXmlResourceInputStream(String xmlFilePath) {
		if (xmlFilePath != null) {
			if (!xmlFilePath.startsWith("/")) {
				xmlFilePath = new StringBuffer(KuixConstants.DEFAULT_XML_RES_FOLDER).append(xmlFilePath).toString();
			}
			// Use frameHandler.getClass() because of a Object.class bug
			return frameHandler.getClass().getResourceAsStream(xmlFilePath);
		}
		return null;
	}

	/**
	 * Convert resource to a ByteArrayInputStream.
	 * 
	 * @param clazz The {@link Class} where the <code>getResourceAsStream()</code> function is called.
	 * @param path Path of the resource file
	 * @return The corresponding {@link ByteArrayInputStream}, or <code>null</code> if an error occure.
	 */
	public static ByteArrayInputStream getResourceAsByteArrayInputStream(Class clazz, String path) {
		if (path != null) {
			InputStream resourceInputStream = clazz.getResourceAsStream(path);
			byte[] resourceData = null;
			try {
				resourceData = new byte[resourceInputStream.available()];
				resourceInputStream.read(resourceData);
			} catch (IOException e) {
			}
			if (resourceData != null) {
				return new ByteArrayInputStream(resourceData);
			}
		}
		return null;
	}

	/**
	 * Parse the XML <code>inputStream</code> to build the corresponding
	 * widget tree.
	 * 
	 * @param tagConverter
	 * @param rootWidget
	 * @param inputStream
	 * @param dataProvider
	 * @throws Exception
	 */
	public static void parseXml(final KuixConverter tagConverter, final Widget rootWidget, InputStream inputStream, final DataProvider dataProvider) {
		if (inputStream != null) {
			
			try {
				LightXmlParser.parse(inputStream, KuixConstants.DEFAULT_CHARSET_NAME, new LightXmlParserHandler() {

					private final Stack path = new Stack();
					private Widget widget = rootWidget;
					private String attribute = null;

					/* (non-Javadoc)
					 * @see com.kalmeo.util.xml.DefaultHandler#startDocument()
					 */
					public void startDocument() {
					}

					/* (non-Javadoc)
					 * @see com.kalmeo.util.xml.DefaultHandler#startElement(java.lang.String, java.util.Hashtable)
					 */
					public void startElement(String name, Hashtable attributes) {

						// Currently reading inline attribute value, startElement is not allowed
						if (attribute != null) {
							throw new IllegalArgumentException("Attribute tag (" + attribute + ") can't enclose an other tag");
						}
						
						String tag = name.toLowerCase();
						if (tag.startsWith("_")) {
							// Use tag as current widget's attribute
							attribute = tag.substring(1);
						} else {
							// Use tag as new widget
							
							// Create widget
							Widget newWidget = null;
							boolean internal = false;
							if (path.isEmpty() && tag.equals(rootWidget.getTag())) {
								newWidget = rootWidget;
								rootWidget.clearCachedStyle(true);
								path.push(rootWidget);
							} else {
								// Try to retrieve an internal instance
								newWidget = widget.getInternalChildInstance(tag);
								if (newWidget == null) {
									// Try to create a new widget instance
									newWidget = tagConverter.convertWidgetTag(tag);
								} else {
									internal = true;
								}
								if (newWidget == null && attributes != null && attributes.containsKey(KuixConstants.PACKAGE_ATTRIBUTE)) {

									// Try to construct a custom widget
									
									// Extract package attribute and construct class name
									String packageName = (String) attributes.get(KuixConstants.PACKAGE_ATTRIBUTE);
									String className = new StringBuffer(packageName).append('.').append(name).toString();	// Use name instead of tag because of Class.forName is case sensitive

									// Create a new custom widget instance
									Object customWidgetInstance = null;
										try {
											customWidgetInstance = Class.forName(className).newInstance();
										} catch (ClassNotFoundException e) {
											throw new IllegalArgumentException("Custom widget not found : " + className);
										} catch (Exception e) {
											e.printStackTrace();
											throw new IllegalArgumentException("Custom widget creation exceptiond : " + className);
										}
									if (customWidgetInstance instanceof Widget) {
										newWidget = (Widget) customWidgetInstance;
									} else {
										throw new IllegalArgumentException("Invalid custom widget : " + className);
									}

									// Remove package attribute to continue
									attributes.remove(KuixConstants.PACKAGE_ATTRIBUTE);

								}
								if (newWidget == null) {
									throw new IllegalArgumentException("Unknow tag : " + tag);
								}
								path.push(newWidget);
							}

							// Extract attributes
							if (attributes != null) {
								String attributeName;
								String attributeValue;
								Enumeration enumeration = attributes.keys();
								while (enumeration.hasMoreElements()) {
									String key = (String) enumeration.nextElement();
									attributeName = key.toLowerCase();
									attributeValue = convertParsePropertyStringValues((String) attributes.get(key));
									if (!newWidget.setAttribute(attributeName, attributeValue)) {
										throw new IllegalArgumentException(attributeName);
									}
								}
							}

							if (widget != newWidget && !internal) {
								widget.add(newWidget);
							}
							widget = newWidget;
							
						}
						
					}
					
					/* (non-Javadoc)
					 * @see org.kalmeo.util.xml.LightXmlParserHandler#characters(java.lang.String, boolean)
					 */
					public void characters(String characters, boolean isCDATA) {
						if (characters.trim().length() > 0 && widget != null) {
							
							String usedAttribute = attribute;
							Widget usedWidget = widget;
							
							// Check #inc statment
							if (characters.startsWith(KuixConstants.INCLUDE_KEYWORD_PATTERN)) {
								String fileName = null;
								String dataProviderProperty = null;
								String rawParams = StringUtil.extractRawParams(KuixConstants.INCLUDE_KEYWORD_PATTERN, characters);
								if (rawParams != null) {
									StringTokenizer st = new StringTokenizer(rawParams, ", ");
									if (st.hasMoreElements()) {
										fileName = st.nextToken();
										if (st.countTokens() >= 1) {
											dataProviderProperty = st.nextToken().trim();
										}
										if (fileName != null) {
											// File names accept parse properties
											fileName = convertParsePropertyStringValues(fileName.trim());
										}
										InputStream inputStream = getXmlResourceInputStream(fileName);
										if (inputStream != null) {
											try {
												if (usedAttribute != null) {
													
													// Attribute value, then the file content is returned as a String
													byte[] rawData = new byte[inputStream.available()];
													inputStream.read(rawData);
													characters = new String(rawData);
													
												} else {
													
													// Parse property variable to define a new dataProvider ?
													DataProvider includeDataProvider = dataProvider;
													if (dataProviderProperty != null && dataProvider != null) {
														if (dataProviderProperty.startsWith(KuixConstants.PARSE_PROPERTY_START_PATTERN)) {
															String property = dataProviderProperty.substring(KuixConstants.PARSE_PROPERTY_START_PATTERN.length(), dataProviderProperty.length() - KuixConstants.PROPERTY_END_PATTERN.length());
															Object value = dataProvider.getValue(property);
															if (value instanceof DataProvider) {
																includeDataProvider = (DataProvider) value;
															} else {
																throw new IllegalArgumentException("#inc accept only DataProvider property value");
															}
														} else {
															throw new IllegalArgumentException("#inc accept only parse property");
														}
													}
													
													// Default include: file content is parsed and added to current widget
													parseXml(tagConverter, widget, inputStream, includeDataProvider);
													return;
													
												}
											} catch (IOException e) {
												throw new IllegalArgumentException("Invalid include file : " + fileName);
											}
										} else {
											throw new IllegalArgumentException("Include file not found : " + fileName);
										}
									}
								}
							}
							
							// If no attribute is defined
							boolean defaultTextWidget = false;
							if (usedAttribute == null) {
								if (widget instanceof TextWidget) {
									usedAttribute = KuixConstants.TEXT_ATTRIBUTE;
								} else if (widget instanceof Picture) {
									usedAttribute = KuixConstants.SRC_ATTRIBUTE;
								} else {
									usedAttribute = KuixConstants.TEXT_ATTRIBUTE;
									usedWidget = new Text();
									widget.add(usedWidget);
									defaultTextWidget = true;
								}
							}
							
							if (usedWidget.isObjectAttribute(usedAttribute)) {
								
								// The attribute need an Object value : only a single property variable is allowed
								if (!isCDATA && characters.endsWith(KuixConstants.PROPERTY_END_PATTERN)) {
									String property;
									
									// Parse property variable ?
									if (dataProvider != null && characters.startsWith(KuixConstants.PARSE_PROPERTY_START_PATTERN)) {
										property = characters.substring(KuixConstants.PARSE_PROPERTY_START_PATTERN.length(), characters.length() - KuixConstants.PROPERTY_END_PATTERN.length());
										Object value = dataProvider.getValue(property);
										if (usedWidget.setObjectAttribute(usedAttribute, value)) {
											return;
										}
									}
									
									// Bind property variable ?
									if (characters.startsWith(KuixConstants.BIND_PROPERTY_START_PATTERN)) {
										property = characters.substring(KuixConstants.BIND_PROPERTY_START_PATTERN.length(), characters.length() - KuixConstants.PROPERTY_END_PATTERN.length());
										usedWidget.setAttributeBindInstruction(usedAttribute, new String[] { property }, null);
										return;
									}
									
								}
								throw new IllegalArgumentException("Bad attribute value : " + usedAttribute);
								
							} else {
								
								if (!isCDATA) {
									
									// Convert parse property variables to their string values
									characters = convertParsePropertyStringValues(characters.trim());
									
									// Extract possible bind properties
									String[] properties = extractBindProperties(characters);
									if (properties != null) {
										usedWidget.setAttributeBindInstruction(usedAttribute, properties, characters);
										// Special case for default text widget
										if (defaultTextWidget && dataProvider != null) {
											dataProvider.bind(usedWidget);
										}
										return;
									} else {
										characters = processI18nPattern(characters);
									}
									
								}
								
								// Set attribute value
								if (!usedWidget.setAttribute(usedAttribute, characters)) {
									throw new IllegalArgumentException(usedAttribute);
								}
								
							}
							
						}
					}

					/* (non-Javadoc)
					 * @see com.kalmeo.util.xml.DefaultHandler#endElement(java.lang.String)
					 */
					public void endElement(String name) {
						if (name.startsWith("_")) {
							// Use name as current widget's attribute tag
							attribute = null;
						} else {
							
							// Check widget binds
							if (widget != null && dataProvider != null && widget.hasBindInstruction()) {
								dataProvider.bind(widget);
							}
							
							// Use name as widget tag
							if (path.size() > 1) {
								path.pop();
								widget = (Widget) path.lastElement();
							}
						}
					}

					/* (non-Javadoc)
					 * @see com.kalmeo.util.xml.DefaultHandler#endDocument()
					 */
					public void endDocument() {
						path.removeAllElements();
						widget = null;
					}
					
					/**
					 * Convert the parse property variables and replace them by their
					 * string values.
					 * <p>Syntax : <code>${varName[|Null text]}</code></p>
					 * <p>Example with var1="Hello" and var2=null</p>
					 * <p><code>${var1}</code> is transform to <code>Hello</code></p>
					 * <p><code>${var1|Nothing}</code> is transform to <code>Hello</code></p>
					 * <p><code>${var2|Nothing}</code> is transform to <code>Nothing</code></p>
					 * 
					 * @param rawData
					 * @param propertyProvider
					 * @return The processed String
					 */
					private String convertParsePropertyStringValues(String rawData) {
						int posStart = rawData.indexOf(KuixConstants.PARSE_PROPERTY_START_PATTERN);
						if (posStart != -1) {
							int posEnd = rawData.indexOf(KuixConstants.PROPERTY_END_PATTERN, posStart);
							if (posEnd != -1) {
								StringBuffer buffer = new StringBuffer(rawData.substring(0, posStart));
								String property = rawData.substring(posStart + 2, posEnd);
								String propertyValue = null;
								int posPipe = property.indexOf(KuixConstants.PROPERTY_ALTERNATIVE_SEPRATOR_PATTERN);
								if (posPipe != -1) {
									if (dataProvider != null) {
										propertyValue = dataProvider.getStringValue(property.substring(0, posPipe));
									}
									if (propertyValue == null) {
										propertyValue = property.substring(posPipe + 1);
									}
								} else if (dataProvider != null) {
									propertyValue = dataProvider.getStringValue(property);
								}
								if (propertyValue != null) {
									buffer.append(propertyValue);
								}
								return buffer.append(convertParsePropertyStringValues(rawData.substring(posEnd + 1))).toString();
							}
						}
						return rawData;
					}
					
					/**
					 * Extract a bind properties list.
					 * 
					 * @param rawData
					 * @return a list of all extracted bind properties or
					 *         <code>null</code> if not bind property is defined
					 *         in the input String.
					 */
					private String[] extractBindProperties(String rawData) {
						Vector properties = null;
						int posStart = 0;
						int posEnd = 0;
						while (true) {
							posStart = rawData.indexOf(KuixConstants.BIND_PROPERTY_START_PATTERN, posEnd);
							if (posStart != -1) {
								posEnd = rawData.indexOf(KuixConstants.PROPERTY_END_PATTERN, posStart);
								if (posEnd != -1) {
									if (properties == null) {
										 properties = new Vector();
									}
									String propertyDefinition = rawData.substring(posStart + 2, posEnd);
									int posPipe = propertyDefinition.indexOf(KuixConstants.PROPERTY_ALTERNATIVE_SEPRATOR_PATTERN);
									if (posPipe == -1) {
										properties.addElement(propertyDefinition);
									} else {
										properties.addElement(propertyDefinition.substring(0, posPipe));
									}
								} else {
									break;
								}
							} else {
								break;
							}
						}
						if (properties != null) {
							String[] propertiesArray = new String[properties.size()];
							properties.copyInto(propertiesArray);
							return propertiesArray;
						}
						return null;
					}

				});
				
				return;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		throw new IllegalArgumentException("Invalid xml inputStream");
	}

	/**
	 * Parse a CSS <code>inputStream</code> and register extracted {@link Style} to the
	 * styles list.
	 * 
	 * @param propertyConverter
	 * @param inputStream
	 * @throws IOException
	 */
	public static void parseCss(KuixConverter propertyConverter, InputStream inputStream) {
		if (inputStream != null) {
			Reader reader = new InputStreamReader(inputStream);
			try {
	
				boolean selectorsCapture = true;
				boolean commentCapture = false;
	
				StringBuffer rawSelectors = new StringBuffer();
				StringBuffer rawDefinitions = new StringBuffer();
	
				for (int c = reader.read(); c != -1;) {
	
					if (commentCapture) {
						if (c == '*') {
							if ((c = reader.read()) == '/') {
								commentCapture = false;
							} else {
								continue;
							}
						}
					} else {
						
						if (c == '*') {
							throw new IllegalArgumentException("Invalid css comment block");
						}
	
						if (c == '/') {	// Caution that all '/' character are ignored
							if ((c = reader.read()) == '*') {
								commentCapture = true;
								c = reader.read();
								continue;
							}
						}
	
						if (selectorsCapture) {
							if (c == '{') {
	
								// Switch to definition capture
								selectorsCapture = false;
	
							} else {
								rawSelectors.append((char) c);
							}
						} else {
							if (c == '}') {
	
								// Create the Style sheet from raw data
								Style[] styles = extractStyleSheets(propertyConverter, rawSelectors.toString(), rawDefinitions.toString());
								for (int i = 0; i < styles.length; ++i) {
									registerStyle(styles[i]);
								}
	
								// Clear StringBuffers
								rawSelectors.delete(0, rawSelectors.length());
								rawDefinitions.delete(0, rawDefinitions.length());
	
								// Switch to selectors capture
								selectorsCapture = true;
	
							} else {
								rawDefinitions.append((char) c);
							}
						}
	
					}
					c = reader.read();
	
				}
				
				if (commentCapture) {
					throw new IllegalArgumentException("CSS : Invalide comment block");
				}
				
				if (!selectorsCapture) {
					throw new IllegalArgumentException("CSS : Invalid selector block");
				}
				
				return;
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		throw new IllegalArgumentException("Invalid css inputStream");
	}

	/**
	 * Extract {@link Style} definitions from raw datas and return a list of
	 * {@link Style} instance.
	 * 
	 * @param converter
	 * @param rawSelectors
	 * @param rawDefinitions
	 * @return A list of {@link Style} instance.
	 */
	public static Style[] extractStyleSheets(KuixConverter converter, String rawSelectors, String rawDefinitions) {

		// Extract Selectors
		StringTokenizer selectors = new StringTokenizer(rawSelectors, ",");
		int numSelectors = selectors.countTokens();
		Style[] styles = new Style[numSelectors];
		for (int i = 0; i < numSelectors; ++i) {

			// Create the StyleSelector tree
			StyleSelector previousStyleSelector = null;
			StringTokenizer contextualSelectors = new StringTokenizer(selectors.nextToken(), " \t\n\r");
			while (contextualSelectors.hasMoreTokens()) {
				StyleSelector styleSelector = new StyleSelector(contextualSelectors.nextToken().toLowerCase());
				if (previousStyleSelector != null) {
					styleSelector.parent = previousStyleSelector;
				}
				previousStyleSelector = styleSelector;
			}

			// Create the Style
			styles[i] = new Style(previousStyleSelector);

		}

		// Extract definitions
		StringTokenizer definitions = new StringTokenizer(rawDefinitions, ";");
		while (definitions.hasMoreTokens()) {
			String definition = definitions.nextToken().trim();
			if (definition.length() > 2) {
				StringTokenizer property = new StringTokenizer(definition, ":");
				if (property.countTokens() == 2) {

					String name = property.nextToken().trim();
					String rawValue = property.nextToken().trim();

					// Convert the property value
					Object value = converter.convertStyleProperty(name, rawValue);
					
					// Add property to all styles (Because of linked list, new instance is needed for each style)
					for (int i = 0; i < styles.length; ++i) {
						styles[i].add(new StyleProperty(name, value));
					}

				}
			}
		}

		return styles;
	}

	/**
	 * Returns the parsed {@link Method}, or null if no method could be extract.
	 * 
	 * @param data
	 * @param owner
	 * @return The parsed {@link Method}
	 */
	public static Method parseMethod(String data, Widget owner) {
		if (data.length() == 0) {
			return null;
		}
		StringTokenizer st = new StringTokenizer(data, "(, \t\r\n)");
		String methodName = st.nextToken();
		if (methodName != null) {

			// Create the method instance
			Method method = new Method(methodName);

			// Extract arguments
			Object[] arguments = new Object[st.countTokens()];
			int i = 0;
			while (st.hasMoreTokens()) {

				String argumentRawValue = st.nextToken();

				// Convert argument
				Object argumentValue = argumentRawValue;
				
				String widgetId = null;
				int dotPos = argumentRawValue.indexOf(".");
				if (dotPos != -1) {
					widgetId = argumentRawValue.substring(0, dotPos);
				} else {
					widgetId = argumentRawValue;
				}
				
				Widget widget = null;
				if ("this".equals(widgetId)) {
					widget = owner;
				} else if (widgetId != null && widgetId.startsWith("#")) {
					widget = KuixMIDlet.getDefault().getCanvas().getDesktop().getWidget(widgetId.substring(1));
				}

				if (widget != null) {
					if (dotPos != -1) {
						argumentValue = widget.getAttribute(argumentRawValue.substring(dotPos + 1));
					} else {
						argumentValue = widget;
					}
				}

				// Store converted value
				arguments[i++] = argumentValue;

			}
			method.setArguments(arguments);

			return method;
		}
		return null;
	}

	// Styles management
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Register a {@link Style}
	 * 
	 * @param style
	 */
	public static void registerStyle(final Style style) {
		if (style != null) {
			Style registredStyle = (Style) registredStyles.find(new Filter() {
				public int accept(Object obj) {
					return ((Style) obj).getSelector().equals(style.getSelector()) ? 1 : 0;
				}
			});
			if (registredStyle != null) {

				// A style is already registred withe the same key, lets copy all StyleAttributes
				LinkedList properties = style.getProperties();
				for (StyleProperty property = (StyleProperty) properties.getFirst(); property != null; property = (StyleProperty) property.getNext()) {
					registredStyle.add(property.copy());
				}

			} else {
				registredStyles.add(style);
			}
		}
	}

	/**
	 * Returns the list of {@link Style} associated to the <code>widget</code> or
	 * <code>null</code> if no style was found.
	 * 
	 * @param widget
	 * @return The list of {@link Style} associated to the <code>widget</code>
	 */
	public static Vector getStyles(final Widget widget) {
		if (widget != null) {

			// Define the filter
			Filter filter = new Filter() {
				
				/* (non-Javadoc)
				 * @see org.kalmeo.util.Filter#accept(java.lang.Object)
				 */
				public int accept(Object obj) {

					int score = 0;
					StyleSelector styleSelector = ((Style) obj).getSelector();
					Widget currentWidget = widget;
					while (styleSelector != null) {

						String[] pseudoClasses = widget.getAvailablePseudoClasses();
						boolean isCompatible = false;
						while (currentWidget != null && !isCompatible) {

							// Id
							if (styleSelector.hasId()) {
								if (currentWidget.getId() != null && currentWidget.getId().equals(styleSelector.getId())) {
									isCompatible = true;
									score += 1000000;
								}
							}
							
							// Class
							if (!isCompatible && styleSelector.hasClass()) {
								String[] styleClasses = currentWidget.getStyleClasses();
								if (styleClasses != null) {
									int i = styleClasses.length - 1;
									for (; i >= 0; --i) {
										String styleClass = styleClasses[i];
										if (styleClass != null && styleClass.equals(styleSelector.getStyleClass())) {
											isCompatible = true;
											score += 10000;
											break;
										}
									}
								}
							}

							if (styleSelector.hasTag()) {
								
								// Tag
								if (!isCompatible && currentWidget.getTag() != null && currentWidget.getTag().equals(styleSelector.getTag())) {
									isCompatible = true;
									score += 100;
								}
								
								// Inherited tag
								if (!isCompatible && currentWidget.getInheritedTag() != null && currentWidget.getInheritedTag().equals(styleSelector.getTag())) {
									isCompatible = true;
									score++;
								}
								
							}
							
							if (!isCompatible && score == 0) {
								return 0;
							}

							// Pseudo class
							if (styleSelector.hasPseudoClass() && pseudoClasses != null) {
								for (int i = pseudoClasses.length - 1; i>= 0; --i) {
									for (int j = styleSelector.getPseudoClasses().length - 1; j>=0; --j) {
										if (pseudoClasses[i].equals(styleSelector.getPseudoClasses()[j])) {
											isCompatible = true;
											score += 100000000;
										}
									}
								}
							}
							
							currentWidget = currentWidget.parent;
						}

						styleSelector = styleSelector.parent;
						if (currentWidget == null && styleSelector != null || !isCompatible) {
							return 0;
						}

					}
					return score;

				}
			};

			Vector styles = registredStyles.findAll(filter);
			if (widget.getAuthorStyle() != null) {
				// Insert the author style at the first position
				styles.insertElementAt(widget.getAuthorStyle(), 0);
			}
			if (styles != null) {
				return styles;
			}

		}
		return null;
	}
	
	/**
	 * Remove all registred styles
	 */
	public static void removeAllStyles() {
		registredStyles.removeAll();
	}

	/**
	 * Clear style cache from the specified {@link Widget} and its childs
	 * 
	 * @param target
	 * @param propagateToChildren
	 */
	public static void clearStyleCache(Widget target, boolean propagateToChildren) {
		if (target != null) {
			target.clearCachedStyle(propagateToChildren);
		}
	}
	
	// Internationalization support
	///////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Initializes internationalization support based on currently set locale (obtained
	 * from "microedition.locale" system property). The initialization method is
	 * called automatically when a call to {@link #getMessage(java.lang.String)}
	 * method is attempted for the first time.
	 * 
	 * You can call this method explicitly to see whether there was any problem
	 * with initialization of the i18n support. Method returns a status
	 * of the successfulness.
	 * 
	 * @return true if the intialization was succesfull, false if there was any
	 *         problem.
	 */
	public static boolean initI18nSupport() {
		String locale = null;
		try {
			locale = System.getProperty("microedition.locale");
		} catch (Exception e) {
		}
		return initI18nSupport(KuixConstants.DEFAULT_I18N_MESSAGES_BUNDLE, locale);
	}

	/**
	 * Explicit initialization of the internationalization support. This method is
	 * usually called when a particular locale used in the application. E.g. the
	 * application contains only french messages (no default messages, only
	 * <CODE>messages_fr.properties</CODE> files is available), you should
	 * initialize the i18n support (by calling
	 * <CODE>initI18nSupport("fr");</CODE>) before using
	 * {@link #getMessage(java.lang.String)} method for the first time.
	 * 
	 * @param locale locale which will be used to determine which message file
	 *            from bundle will be used
	 * @return true if the intialization was succesfull, false if there was any
	 *         problem.
	 */
	public static boolean initI18nSupport(String locale) {
		return initI18nSupport(KuixConstants.DEFAULT_I18N_MESSAGES_BUNDLE, locale);
	}
	
	/**
	 * Explicit initialization of the internationalization support. This method is
	 * usually called when a particular locale used in the application. E.g. the
	 * application contains only french messages (no default messages, only
	 * <CODE>messages_fr.properties</CODE> files is available), you should
	 * initialize the i18n support (by calling
	 * <CODE>initI18nSupport("fr");</CODE>) before using
	 * {@link #getMessage(java.lang.String)} method for the first time.
	 * 
	 * @param messageBundle full custom messages bundle path
	 * @param locale locale which will be used to determine which message file
	 *            from bundle will be used
	 * @return true if the intialization was succesfull, false if there was any
	 *         problem.
	 */
	public static boolean initI18nSupport(String messageBundle, String locale) {
		InputStream inputStream = null;
		// Use frameHandler.getClass() because of a Object.class bug
		Class clazz = frameHandler.getClass();
		try {
			
			// First, load Kuix default bundle
			inputStream = clazz.getResourceAsStream(KuixConstants.KUIX_DEFAULT_I18N_MESSAGES_BUNDLE);
			if (inputStream != null) {
				// load messages to messageTable hashtable
				messageTable = new Hashtable();
				loadMessages(inputStream);
			} else {
				return false;
			}
			inputStream = null;
			
			// Construct messageBundle
			// try to find localized resource first (in format ${name}_locale.${suffix})
			if ((locale != null) && (locale.length() > 1)) {
				int lastIndex = messageBundle.lastIndexOf('.');
				String prefix = messageBundle.substring(0, lastIndex);
				String suffix = messageBundle.substring(lastIndex);
				// replace '-' with '_', some phones returns locales with
				// '-' instead of '_'. For example Nokia or Motorola
				locale = locale.replace('-', '_');
				inputStream = clazz.getResourceAsStream(new StringBuffer(prefix).append('.').append(locale).append(suffix).toString());
				if (inputStream == null) {
					// if no localized resource is found or localization is available
					// try broader???? locale (i.e. instead og en_US, try just en)
					locale = locale.substring(0, 2); 
					inputStream = clazz.getResourceAsStream(new StringBuffer(prefix).append('.').append(locale).append(suffix).toString());
				}
			}
			if (inputStream == null) {
				// if not found or locale is not set, try default locale
				inputStream = clazz.getResourceAsStream(messageBundle);
			}
			if (inputStream != null) {
				// load messages to messageTable hashtable
				loadMessages(inputStream);
			}
		} catch (UTFDataFormatException e) {
			System.err.println("I18N Error : *.properties files need to be UTF-8 encoded");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Save the locale
		Kuix.locale = locale;
		
		return messageTable != null;
	}

	/**
	 * Finds a localized string in a message bundle.
	 * 
	 * @param key key of the localized string to look for
	 * @return the localized string. If key is not found, then
	 *         <CODE>DEFAULT_UNKNOWN_I18N_MESSAGE</CODE> string is returned
	 */
	public static final String getMessage(String key) {
		return getMessage(key, null);
	}
	
	/**
	 * Returns the last used locale, or null if never set.
	 * 
	 * @return the locale
	 */
	public static String getLocale() {
		return locale;
	}

	/**
	 * Finds a localized string in a message bundle and formats the message by
	 * passing requested parameters.
	 * 
	 * @param key key of the localized string to look for
	 * @param args array of arguments to use for formatting the message
	 * @return the localized string. If key is not found, then
	 *         <CODE>DEFAULT_UNKNOWN_I18N_MESSAGE</CODE> string is returned
	 */
	public static final String getMessage(String key, Object[] args) {
		if (messageTable == null) {
			if (!initI18nSupport()) {
				return KuixConstants.DEFAULT_UNKNOWN_I18N_MESSAGE;
			}
		}
		String s = (String) messageTable.get(key);
		if (s != null) {
			return StringUtil.format(s, args);
		}
		return KuixConstants.DEFAULT_UNKNOWN_I18N_MESSAGE;
	}

	/**
	 * Process the internationalization and replace found keys by their values.
	 * <p>
	 * Syntax : <code>%KEY%</code> if value is like
	 * <code>KEY=Hello world, thanks for using Kuix.</code>
	 * </p>
	 * <p>
	 * Syntax : <code>%KEY(arg0,arg1)%</code> if value is like
	 * <code>KEY=Hello {0}, thanks for using {1}.</code>
	 * </p>
	 * 
	 * @param pattern
	 * @return The processed String
	 */
	public static String processI18nPattern(String pattern) {
		if (pattern != null && pattern.startsWith("%") && pattern.endsWith("%")) {
			int keyEndIndex = pattern.length() - 1;
			String[] argsValues = null;
			int argStartIndex;
			if ((argStartIndex = pattern.indexOf('(', 1)) != -1) {
				int argEndIndex = pattern.indexOf(')', argStartIndex);
				if (argEndIndex > argStartIndex + 1) {
					StringTokenizer args = new StringTokenizer(pattern.substring(argStartIndex + 1, argEndIndex), ",");
					argsValues = new String[args.countTokens()];
					int i = 0;
					while (args.hasMoreTokens()) {
						argsValues[i++] = args.nextToken().trim();
					}
					keyEndIndex = argStartIndex;
				}
			}
			return Kuix.getMessage(pattern.substring(1, keyEndIndex), argsValues);
		}
		return pattern;
	}
	
	/* 
	 * Internal i18n code
	 */

	// Characters separating keys and values
	private static final String KEY_VALUE_SEPARATORS = "=: \t\r\n\f";
	
	// Characters strictly separating keys and values
	private static final String STRICT_KEY_VALUE_SEPARTORS = "=:";
	
	// white space characters understood by the support (these can be in the message file)
	private static final String WHITESPACE_CHARS = " \t\r\n\f";

	// Contains the parsed message bundle.
	private static Hashtable messageTable;
	
	// Contains the last used locale, or null is never set.
	private static String locale;
	
	/**
	 * Loads messages from input stream to hash table.
	 * 
	 * @param inStream stream from which the messages are read
	 * @throws IOException if there is any problem with reading the messages
	 */
	private static synchronized void loadMessages(InputStream inStream) throws Exception {

		InputStreamReader inputStream = new InputStreamReader(inStream, "UTF-8");
		while (true) {
			// Get next line
			String line = readLine(inputStream);
			if (line == null)
				return;

			if (line.length() > 0) {

				// Find start of key
				int len = line.length();
				int keyStart;
				for (keyStart = 0; keyStart < len; keyStart++) {
					if (WHITESPACE_CHARS.indexOf(line.charAt(keyStart)) == -1) {
						break;
					}
				}

				// Blank lines are ignored
				if (keyStart == len) {
					continue;
				}

				// Continue lines that end in slashes if they are not comments
				char firstChar = line.charAt(keyStart);
				if ((firstChar != '#') && (firstChar != '!')) {
					while (continueLine(line)) {
						String nextLine = readLine(inputStream);
						if (nextLine == null) {
							nextLine = "";
						}
						String loppedLine = line.substring(0, len - 1);
						// Advance beyond whitespace on new line
						int startIndex;
						for (startIndex = 0; startIndex < nextLine.length(); startIndex++) {
							if (WHITESPACE_CHARS.indexOf(nextLine.charAt(startIndex)) == -1) {
								break;
							}
						}
						nextLine = nextLine.substring(startIndex, nextLine.length());
						line = new String(loppedLine + nextLine);
						len = line.length();
					}

					// Find separation between key and value
					int separatorIndex;
					for (separatorIndex = keyStart; separatorIndex < len; separatorIndex++) {
						char currentChar = line.charAt(separatorIndex);
						if (currentChar == '\\') {
							separatorIndex++;
						} else if (KEY_VALUE_SEPARATORS.indexOf(currentChar) != -1) {
							break;
						}
					}

					// Skip over whitespace after key if any
					int valueIndex;
					for (valueIndex = separatorIndex; valueIndex < len; valueIndex++) {
						if (WHITESPACE_CHARS.indexOf(line.charAt(valueIndex)) == -1) {
							break;
						}
					}

					// Skip over one non whitespace key value separators if any
					if (valueIndex < len) {
						if (STRICT_KEY_VALUE_SEPARTORS.indexOf(line.charAt(valueIndex)) != -1) {
							valueIndex++;
						}
					}

					// Skip over white space after other separators if any
					while (valueIndex < len) {
						if (WHITESPACE_CHARS.indexOf(line.charAt(valueIndex)) == -1) {
							break;
						}
						valueIndex++;
					}
					String key = line.substring(keyStart, separatorIndex);
					String value = (separatorIndex < len) ? line.substring(valueIndex, len) : "";

					// Convert then store key and value
					key = convertString(key);
					value = convertString(value);
					messageTable.put(key, value);
				}
			}
		}

	}

	/**
	 * reads a single line from InputStreamReader
	 * 
	 * @param in InputStreamReader used to read the line
	 * @throws IOException if there is any problem with reading
	 * @return the read line
	 */
	private static String readLine(InputStreamReader in) throws IOException {
		StringBuffer strBuf = new StringBuffer("");
		int i;
		while ((i = in.read()) != -1) {
			if ((char) i == '\r' || (char) i == '\n') {
				return strBuf.toString();
			}
			strBuf.append((char) i);
		}
		return strBuf.length() > 0 ? strBuf.toString() : null;
	}

	/**
	 * determines whether the line of the supplied string continues on the next
	 * line
	 * 
	 * @param line a line of String
	 * @return true if the string contines on the next line, false otherwise
	 */
	private static boolean continueLine(String line) {
		int slashCount = 0;
		int index = line.length() - 1;
		while ((index >= 0) && (line.charAt(index--) == '\\'))
			slashCount++;
		return (slashCount % 2 == 1);
	}

	/**
	 * Decodes a String which uses unicode characters in \\uXXXX format.
	 * 
	 * @param theString String with \\uXXXX characters
	 * @return resolved string
	 */
	private static String convertString(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);

		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								// return KuixConstants.DEFAULT_UNKNOWN_I18N_MESSAGE STRING if there is any problem
								return KuixConstants.DEFAULT_UNKNOWN_I18N_MESSAGE;
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

}