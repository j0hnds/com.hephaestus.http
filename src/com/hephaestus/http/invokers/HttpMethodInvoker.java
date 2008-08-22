package com.hephaestus.http.invokers;

import com.hephaestus.http.views.HTTPViewData;

/**
 * The interface to be implemented to invoke an HTTP method.
 * 
 * @author Dave Sieh
 */
public interface HttpMethodInvoker {

	/**
	 * Invokes an HTTP method with the specified view data.
	 * 
	 * @param viewData
	 *            the interface to the URL information and response fields
	 */
	void invoke(HTTPViewData viewData);
}
