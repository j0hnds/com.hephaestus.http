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

/**
 * This class extends the functionality of the BaseHttpMethodInvoker to provide
 * methods to help deal with entity-type methods (POST/PUT).
 * 
 * @author Dave Sieh
 * 
 */
public abstract class BaseEntityEnclosingMethodInvoker extends
		BaseHttpMethodInvoker {

	// The content type for form data
	private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";

	// The default encoding of the data
	private static final String FORM_ENCODING = "UTF-8";

	/**
	 * Helper method to construct the entity for the method. This method
	 * currently supports three different types of entities:
	 * 
	 * <ol>
	 * <li>Bulk post data (textual with a content type)</li>
	 * <li>Form post data</li>
	 * <li>Multi-part post data (file upload)</li>
	 * </ol>
	 * 
	 * @param method
	 *            the method on which the entity is associated.
	 * @param viewData
	 *            reference to the source of the entity data.
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @throws FileNotFoundException
	 *             if the file upload file is not found.
	 */
	protected void populateEntity(EntityEnclosingMethod method,
			HTTPViewData viewData) throws UnsupportedEncodingException,
			FileNotFoundException {
		Map<String, String> postData = viewData.getRequestPostDataFields();
		String bulkPostData = viewData.getBulkPostData();
		String fileUploadPath = viewData.getFileUploadPath();

		if (!postData.isEmpty()) {
			populateMethodPostData(method, postData);
		}
		else if (bulkPostData != null && bulkPostData.length() > 0) {
			populateMethodBulkData(method, bulkPostData, viewData
					.getContentType());
		}
		else if (fileUploadPath != null && fileUploadPath.length() > 0) {
			populateMethodFileUploadData(method, fileUploadPath);
		}
	}

	/**
	 * Populates form data on the method.
	 * 
	 * @param method
	 *            the method
	 * @param postDataFields
	 *            the map of data elements for the post data.
	 * @throws UnsupportedEncodingException
	 *             if the specified encoding is not supported.
	 */
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

	/**
	 * Populates a bulk text entity.
	 * 
	 * @param method
	 *            the method
	 * @param bulkData
	 *            the data of the entity
	 * @param contentType
	 *            the content type of the data
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported.
	 */
	private void populateMethodBulkData(EntityEnclosingMethod method,
			String bulkData, String contentType)
			throws UnsupportedEncodingException {
		method.setRequestEntity(new StringRequestEntity(bulkData, contentType,
				FORM_ENCODING));
	}

	/**
	 * Populates the multi-part data for a file upload.
	 * 
	 * @param method
	 *            the method.
	 * @param fileUploadPath
	 *            the path to the file to upload.
	 * @throws FileNotFoundException
	 *             if the file was not found.
	 */
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
