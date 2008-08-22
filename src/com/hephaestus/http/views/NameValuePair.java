package com.hephaestus.http.views;

/**
 * A POJO for hanging onto name/value pairs.
 * 
 * @author Dave Sieh
 */
public class NameValuePair {
	// The name
	private String name;

	// The Value
	private String value;

	/**
	 * Constructs a new name value pair with the specified name and value.
	 * 
	 * @param name
	 *            the name
	 * @param value
	 *            the value
	 */
	public NameValuePair(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
