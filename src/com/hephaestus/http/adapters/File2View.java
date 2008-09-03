package com.hephaestus.http.adapters;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.bea.xml.XmlException;
import com.hephaestus.http.views.HTTPViewData;
import com.hephaestus.httpRequestData.BulkPostDataType;
import com.hephaestus.httpRequestData.FileUploadType;
import com.hephaestus.httpRequestData.HttpRequestDataDocument;
import com.hephaestus.httpRequestData.HttpRequestDataType;
import com.hephaestus.httpRequestData.NameValuePairType;
import com.hephaestus.httpRequestData.PostDataType;

/**
 * This class is an adapter between a file on the file system and the
 * HTTPViewData. This allows us to abstract the fact that an XML document is
 * used to persist the view information.
 * 
 * @author Dave Sieh
 */
public class File2View {

	// The file containing the view data
	private File file;

	/**
	 * Constructs a new File2View adapter object.
	 * 
	 * @param file
	 *            the view data file.
	 */
	public File2View(File file) {
		this.file = file;
	}

	/**
	 * Restores the view information from the contents of the file.
	 * 
	 * @param viewData
	 *            reference to the view data
	 * @throws XmlException
	 *             if there is a problem with the XML document
	 * @throws IOException
	 *             if there was an error reading the file.
	 */
	public void restoreView(HTTPViewData viewData) throws XmlException,
			IOException {
		HttpRequestDataDocument doc = loadDocument();

		viewData.clearInputs();
		
		HttpRequestDataType request = doc.getHttpRequestData();
		viewData.setVerb(request.getVerb());
		viewData.setProtocol(request.getProtocol());
		viewData.setHostPort(request.getHostport());
		viewData.setURI(request.getUri());

		PostDataType pdt = request.getHeaders();
		if (pdt != null) {
			Map<String, String> headers = new HashMap<String, String>();
			for (NameValuePairType nvp : pdt.getNameValuePairArray()) {
				headers.put(nvp.getName(), nvp.getValue());
			}
			viewData.setRequestHttpHeaders(headers);
		}

		pdt = request.getPostData();
		if (pdt != null) {
			Map<String, String> postdata = new HashMap<String, String>();
			for (NameValuePairType nvp : pdt.getNameValuePairArray()) {
				postdata.put(nvp.getName(), nvp.getValue());
			}
			viewData.setRequestPostDataFields(postdata);
		}

		BulkPostDataType bpdt = request.getBulkPostData();
		if (bpdt != null) {
			String bulkData = bpdt.stringValue();
			if (bulkData != null && bulkData.length() > 0) {
				viewData.setBulkPostData(bulkData);
				viewData.setContentType(bpdt.getContentType());
			}
		}

		FileUploadType fut = request.getFileUpload();
		if (fut != null) {
			String fileUploadPath = fut.getPath();
			if (fileUploadPath != null && fileUploadPath.length() > 0) {
				viewData.setFileUploadPath(fileUploadPath);
			}
			String uploadName = fut.getName();
			if (uploadName != null && uploadName.length() > 0) {
				viewData.setUploadName(uploadName);
			}
		}

	}

	/**
	 * Loads and parses the XML document from the file.
	 * 
	 * @return the parsed XML document
	 * @throws XmlException
	 *             if there was an error parsing the data
	 * @throws IOException
	 *             if there was an error reading the file.
	 */
	public HttpRequestDataDocument loadDocument() throws XmlException,
			IOException {
		return HttpRequestDataDocument.Factory.parse(file);
	}

}
