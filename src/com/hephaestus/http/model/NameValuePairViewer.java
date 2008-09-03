package com.hephaestus.http.model;


/**
 * Defines an interface for modifications to NameValuePairs. Implementers will
 * have these methods called when changes are made to the content model.
 * 
 * @author Dave Sieh
 */
public interface NameValuePairViewer {
	/**
	 * Add a NameValuePair
	 * 
	 * @param nvp
	 *            the name value pair to add.
	 */
	void addNameValuePair(NameValuePair nvp);

	/**
	 * Remove a NameValuePair
	 * 
	 * @param nvp
	 *            the name value pair to remove.
	 */
	void removeNameValuePair(NameValuePair nvp);

	/**
	 * Modify a NameValuePair.
	 * 
	 * @param nvp
	 *            the name value pair that was modified
	 */
	void updateNameValuePair(NameValuePair nvp);
}
