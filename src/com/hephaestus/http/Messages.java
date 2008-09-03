package com.hephaestus.http;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides access to the localized strings for the application.
 * 
 * Originally generated using the Eclipse "Externalize Strings..." wizard.
 * 
 * @author Dave Sieh
 */
public class Messages {

	// The name of the resource bundle.
	private static final String BUNDLE_NAME = "com.hephaestus.http.messages"; //$NON-NLS-1$

	// The resource bundle.
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	/**
	 * Private constructor to keep others from instantiating this class.
	 */
	private Messages() {
	}

	/**
	 * Returns the localized string associated with the specified key.
	 * 
	 * @param key
	 *            the localized string key.
	 * 
	 * @return the localized string. If the key was not found, the returned
	 *         string will be of the form "!key!".
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		}
		catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
