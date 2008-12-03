package com.hephaestus.http.views;

import java.util.Map;

/**
 * Interface to the data associated with an HTTP call.
 * 
 * @author pc23dxs
 */
public interface HTTPViewData {
	
	/**
	 * Returns the full URL of the HTTP resource.
	 * 
	 * @return the full URL as a String.
	 */
	String getURL();
	
	/**
	 * Returns the HTTP method to use.
	 * 
	 * @return one of GET, POST, PUT, DELETE
	 */
	String getVerb();
	
	/**
	 * Sets the verb to use.
	 * 
	 * @param verb one of GET, POST, PUT, DELETE
	 */
	void setVerb(String verb);
	
	/**
	 * Gets the map of HTTP request headers associated with the request.
	 * 
	 * @return a map of the request headers.
	 */
	Map<String,String> getRequestHttpHeaders();
	
	/**
	 * Sets the map of HTTP request headers to associate with a request.
	 * 
	 * @param headers a map of the request headers to use.
	 */
	void setRequestHttpHeaders(Map<String,String> headers);
	
	/**
	 * Returns the map of POST data fields for the request.
	 * 
	 * @return map of the post data fields.
	 */
	Map<String,String> getRequestPostDataFields();
	
	/**
	 * Sets the map of POST data fields for the request.
	 * 
	 * @param postData a map of POST data fields to set for the request.
	 */
	void setRequestPostDataFields(Map<String, String> postData);
	
	/**
	 * Returns the bulk POST data for the request as a string. In general
	 * this is the body of the POST request. Kind of a bad assumption that
	 * it is a string, but, oh well.
	 * 
	 * @return the content of the POST data request entity.
	 */
	String getBulkPostData();
	
	/**
	 * Sets the bulk POST data for the request as a string.
	 * 
	 * @param postData the content of the POST data request entity.
	 */
	void setBulkPostData(String postData);
	
	/**
	 * Returns the content type for the bulk POST data.
	 *  
	 * @return the content type for the bulk POST data.
	 */
	String getContentType();
	
	/**
	 * Sets the content type for the bulk POST data.
	 * 
	 * @param contentType the content type for the bulk POST data.
	 */
	void setContentType(String contentType);
	
	/**
	 * Returns the path to the file to upload.
	 * 
	 * @return the path to the file to upload.
	 */
	String getFileUploadPath();
	
	/**
	 * Sets the path to the file to upload.
	 * 
	 * @param path the path to the file to upload.
	 */
	void setFileUploadPath(String path);
	
	/**
	 * Returns the protocol to use for the request.
	 *  
	 * @return one of HTTP or HTTPS
	 */
	String getProtocol();
	
	/**
	 * Sets the protocol to use for the request.
	 * 
	 * @param protocol one of HTTP or HTTPS
	 */
	void setProtocol(String protocol);
	
	/**
	 * Returns the host/port for the request.
	 * 
	 * @return the host/port of the server for the request formatted as
	 * "host[:port]". If no port is specified, the standard port number for 
	 * the protocol is assumed.
	 */
	String getHostPort();
	
	/**
	 * Sets the host/port for the request URL.
	 * 
	 * @param hostPort the host/port of the server for the request formatted as
	 * "host[:port]". If no port is specified, the standar port number for the
	 * protocol is assumed.
	 */
	void setHostPort(String hostPort);
	
	/**
	 * Returns the URI request portion of the complete URL. For example, if the
	 * request URL is http://www.someplace.com/somewhere, then 'somewhere' is 
	 * returned as the URI. If the request URL is http://www.somplace.com, then
	 * the empty string is returne as the URI.
	 * 
	 * @return the URI of the request.
	 */
	String getURI();
	
	/**
	 * Sets the URI request portion of the complete URL.
	 * 
	 * @param uri the URI of the request.
	 */
	void setURI(String uri);
	
	/**
	 * Returns the name of the uploaded file in the request.
	 * 
	 * @return the name of the uploaded file.
	 */
	String getUploadName();
	
	/**
	 * Sets the name of the uploaded file in the request.
	 * 
	 * @param uploadName the name of the uploaded file.
	 */
	void setUploadName(String uploadName);
	
	/**
	 * Sets the status value of the completed request.
	 * 
	 * @param status the status value.
	 */
	void setStatus(String status);
	
	/**
	 * Sets the map of HTTP response headers.
	 * 
	 *@param headers the map of the HTTP response headers.
	 */
	void setResponseHttpHeaders(Map<String, String> headers);
	
	/**
	 * Sets the response data for the request.
	 * 
	 * @param data the response data (as a string).
	 */
	void setResponseData(String data);
	
	/**
	 * Displays the specified error message.
	 * 
	 * @param errorMessage the error message to be displayed.
	 */
	void showErrorMessage(String errorMessage);
	
	/**
	 * Validates the inputs.
	 * 
	 * @return true if the inputs are valid.
	 */
	boolean validInputs();
	
	/**
	 * Clears all the input values.
	 */
	void clearInputs();
}
