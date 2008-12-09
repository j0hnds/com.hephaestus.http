package com.hephaestus.http.adapters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.hephaestus.http.HTTPViewData;
import com.hephaestus.httpRequestData.BulkPostDataType;
import com.hephaestus.httpRequestData.FileUploadType;
import com.hephaestus.httpRequestData.HttpRequestDataDocument;
import com.hephaestus.httpRequestData.HttpRequestDataType;
import com.hephaestus.httpRequestData.NameValuePairType;
import com.hephaestus.httpRequestData.PostDataType;

/**
 * This class is an adapter that transforms HTTPViewData into a file. The intent
 * is to encapsulate all the XML functionality into an adapter class rather than
 * spewing it out over the other classes in the project.
 * 
 * @author Dave Sieh
 */
public class View2File {

	// Reference to the HTTPViewData to be saved as a file.
	private HTTPViewData viewData;

	/**
	 * Constructs a new View2File adapter object.
	 * 
	 * @param viewData
	 *            reference to the HTTPViewData to be saved.
	 */
	public View2File(HTTPViewData viewData) {
		this.viewData = viewData;
	}

	/**
	 * Saves the HTTPViewData into the specified file.
	 * 
	 * @param viewFile
	 *            the file into which the view data is to be saved.
	 * @throws IOException
	 *             if there was an error writing the view data.
	 */
	public void saveView(File viewFile) throws IOException {
		HttpRequestDataDocument doc = getDocument();

		BufferedWriter bw = new BufferedWriter(new FileWriter(viewFile));
		bw.write(doc.toString());
		bw.close();

	}

	/**
	 * Collects the HTTPViewData into an XML document ready to be persisted.
	 * 
	 * @return the XML Document containing the view data.
	 */
	private HttpRequestDataDocument getDocument() {
		HttpRequestDataDocument doc = HttpRequestDataDocument.Factory
				.newInstance();
		HttpRequestDataType rd = doc.addNewHttpRequestData();

		rd.setVerb(viewData.getVerb());
		rd.setProtocol(viewData.getProtocol());
		rd.setHostport(viewData.getHostPort());
		rd.setUri(viewData.getURI());

		setNameValuePairs(viewData.getRequestHttpHeaders(), rd.addNewHeaders());

		String bulkPostData = viewData.getBulkPostData();
		Map<String, String> postData = viewData.getRequestPostDataFields();
		String fileUploadPath = viewData.getFileUploadPath();
		String uploadName = viewData.getUploadName();
		if (bulkPostData != null && bulkPostData.length() > 0) {
			BulkPostDataType bpdt = rd.addNewBulkPostData();
			bpdt.set(bulkPostData);
			bpdt.setContentType(viewData.getContentType());
		}
		else if (fileUploadPath != null && fileUploadPath.length() > 0) {
			FileUploadType fut = rd.addNewFileUpload();
			fut.setPath(fileUploadPath);
			if (uploadName != null && uploadName.length() > 0) {
				fut.setName(uploadName);
			}
		}
		else if (postData.size() > 0) {
			setNameValuePairs(postData, rd.addNewPostData());
		}

		return doc;
	}

	/**
	 * Sets name value pair elements in the XML from a Map.
	 * 
	 * @param nvps
	 *            the name value pairs to be placed in the XML elements.
	 * @param pdt
	 *            the top-level element of the XML name value pairs.
	 */
	private void setNameValuePairs(Map<String, String> nvps, PostDataType pdt) {
		for (Entry<String, String> entry : nvps.entrySet()) {
			NameValuePairType nvp = pdt.addNewNameValuePair();
			nvp.setName(entry.getKey());
			nvp.setValue(entry.getValue());
		}
	}
}
