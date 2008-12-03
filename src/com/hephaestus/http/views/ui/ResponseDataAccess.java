package com.hephaestus.http.views.ui;

/**
 * This interface provides access to the Response data of a request.
 * 
 * @author pc23dxs
 */
public interface ResponseDataAccess {

	/**
	 * Gets the value of the response data.
	 * 
	 * @return the response data as a string.
	 */
	String getResponseData();
	
	/**
	 * Sets the value of the response data
	 * 
	 * @param responseData the response data as a string.
	 */
	void setResponseData(String responseData);
}
