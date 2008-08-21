package com.hephaestus.http.invokers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.util.EncodingUtil;

import com.hephaestus.http.views.HTTPViewData;

public abstract class BaseEntityEnclosingMethodInvoker extends
		BaseHttpMethodInvoker {

	private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final String FORM_ENCODING = "UTF-8";

	protected void populateEntity(EntityEnclosingMethod method,
			HTTPViewData viewData) throws UnsupportedEncodingException, FileNotFoundException {
		Map<String, String> postData = viewData.getRequestPostDataFields();
		String bulkPostData = viewData.getBulkPostData();
		String fileUploadPath = viewData.getFileUploadPath();

		if (!postData.isEmpty()) {
			populateMethodPostData(method, postData);
		}
		else if (bulkPostData != null && bulkPostData.length() > 0) {
			populateMethodBulkData(method, bulkPostData, viewData.getContentType());
		} else if (fileUploadPath != null && fileUploadPath.length() > 0) {
			populateMethodFileUploadData(method, fileUploadPath);
		}
	}

	private void populateMethodPostData(EntityEnclosingMethod method,
			Map<String, String> postDataFields)
			throws UnsupportedEncodingException {
		int count = postDataFields.size();
		if (count > 0) {
			NameValuePair[] pairs = new NameValuePair[count];
			int index = 0;
			for (Entry<String, String> entry : postDataFields.entrySet()) {
				pairs[index++] = new NameValuePair(entry.getKey(), entry
						.getValue());
			}
			String requestParams = EncodingUtil.formUrlEncode(pairs,
					FORM_ENCODING);
			method.setRequestEntity(new StringRequestEntity(requestParams,
					FORM_CONTENT_TYPE, FORM_ENCODING));
		}
	}

	private void populateMethodBulkData(EntityEnclosingMethod method,
			String bulkData, String contentType)
			throws UnsupportedEncodingException {
		method.setRequestEntity(new StringRequestEntity(bulkData, contentType,
				FORM_ENCODING));
	}

	private void populateMethodFileUploadData(EntityEnclosingMethod method,
			String fileUploadPath) throws FileNotFoundException {
		if (fileUploadPath != null && fileUploadPath.length() > 0) {
			File path = new File(fileUploadPath);
			Part[] parts = { new FilePart(path.getName(), path) };
			method.setRequestEntity(new MultipartRequestEntity(parts, method
					.getParams()));
		}
	}
}
