package com.hephaestus.http.views;

import java.util.Map;

public interface HTTPViewData {
	String getURL();
	String getVerb();
	Map<String,String> getRequestHttpHeaders();
	Map<String,String> getRequestPostDataFields();
	String getBulkPostData();
	String getContentType();
	String getFileUploadPath();
	
	void setStatus(String status);
	void setResponseHttpHeaders(Map<String, String> headers);
	void setResponseData(String data);
	void showErrorMessage(String errorMessage);
	
	boolean validInputs();
}
