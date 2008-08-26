package com.hephaestus.http.views;

import java.util.Map;

public interface HTTPViewData {
	String getURL();
	String getVerb();
	void setVerb(String verb);
	Map<String,String> getRequestHttpHeaders();
	void setRequestHttpHeaders(Map<String,String> headers);
	Map<String,String> getRequestPostDataFields();
	void setRequestPostDataFields(Map<String, String> postData);
	String getBulkPostData();
	void setBulkPostData(String postData);
	String getContentType();
	void setContentType(String contentType);
	String getFileUploadPath();
	void setFileUploadPath(String path);
	String getProtocol();
	void setProtocol(String protocol);
	String getHostPort();
	void setHostPort(String hostPort);
	String getURI();
	void setURI(String uri);
	String getUploadName();
	void setUploadName(String uploadName);
	
	void setStatus(String status);
	void setResponseHttpHeaders(Map<String, String> headers);
	void setResponseData(String data);
	void showErrorMessage(String errorMessage);
	
	boolean validInputs();
	
	void clearInputs();
}
