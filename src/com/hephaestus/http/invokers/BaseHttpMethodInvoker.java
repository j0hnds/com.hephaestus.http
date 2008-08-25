package com.hephaestus.http.invokers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.eclipse.core.runtime.Preferences;

import com.hephaestus.http.Activator;
import com.hephaestus.http.preferences.PreferenceConstants;
import com.hephaestus.http.util.NumberUtils;
import com.hephaestus.http.views.HTTPViewData;

/**
 * Base implementation of the HttpMethodInvoker interface. This particular
 * implementation uses commons-httpclient and provides the basic methods to
 * support a variety of HTTP method types.
 * 
 * @author Dave Sieh
 * 
 */
public abstract class BaseHttpMethodInvoker implements HttpMethodInvoker {

	/**
	 * Returns a configured HttpClient to the caller. This method will take all
	 * plug-in defaults into account when constructing the client:
	 * 
	 * <ul>
	 * <li>Proxy Settings</li>
	 * </ul>
	 * 
	 * @return reference to a configured HttpClient
	 */
	protected HttpClient getClient() {
		HttpClient client = new HttpClient();
		Preferences prefs = Activator.getDefault().getPluginPreferences();
		String proxy = prefs.getString(PreferenceConstants.P_PROXY_HOST_PORT);
		if (proxy != null && proxy.length() > 0) {
			String[] cmps = proxy.split(":");
			client.getHostConfiguration().setProxy(cmps[0],
					Integer.parseInt(cmps[1]));
		}

		return client;
	}

	/**
	 * Given the method after the URL invocation, collect up all the response
	 * information and update the view data.
	 * 
	 * @param method
	 *            the http method after the URL has been invoked.
	 * @param viewData
	 *            the view data to update
	 * @throws IOException
	 *             if there was an error reading the response data.
	 */
	protected void collectResponse(HttpMethod method, HTTPViewData viewData)
			throws IOException {
		viewData.setStatus(Integer.toString(method.getStatusCode()));
		if (isTextContent(method)) {
			viewData.setResponseData(getResponseBody(method));
		}
		else {
			// Read the response bytes and throw them away.
			String hexDump = NumberUtils.formatHexDump(getResponseBodyBytes(
					method).toByteArray());
			viewData.setResponseData(hexDump);
		}
		viewData.setResponseHttpHeaders(getResponseHeaders(method));
	}

	/**
	 * Determines if the response of the method is a textual type.
	 * 
	 * @param method
	 *            the http method.
	 * @return true if the content type starts with 'text'.
	 */
	private boolean isTextContent(HttpMethod method) {
		boolean textContent = false;

		Header hContentType = method.getResponseHeader("Content-Type");
		if (hContentType != null) {
			String contentType = hContentType.getValue();
			if (contentType != null && contentType.length() > 0) {
				if (contentType.startsWith("text")) {
					textContent = true;
				}
			}
		}
		return textContent;
	}

	/**
	 * Returns the response body of the method invocation as a string.
	 * 
	 * @param method
	 *            the http method
	 * @return the text representation of the response
	 * @throws IOException
	 *             if there is an error reading the response body.
	 */
	private String getResponseBody(HttpMethod method) throws IOException {
		return getResponseBodyBytes(method).toString();
	}

	/**
	 * Returns the response body of the method invocation as a byte stream.
	 * 
	 * @param method
	 *            the http method
	 * @return the byte array output stream of the response body
	 * @throws IOException
	 *             if there is an error reading the response body.
	 */
	private ByteArrayOutputStream getResponseBodyBytes(HttpMethod method)
			throws IOException {
		InputStream is = method.getResponseBodyAsStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;

		while ((len = is.read(buffer, 0, buffer.length)) >= 0) {
			baos.write(buffer, 0, len);
		}

		return baos;
	}

	/**
	 * Retrieves a map of the response headers from the method.
	 * 
	 * @param method
	 *            the http method
	 * @return reference to a map of the response headers.
	 */
	private Map<String, String> getResponseHeaders(HttpMethod method) {
		Map<String, String> headers = new HashMap<String, String>();

		for (Header header : method.getResponseHeaders()) {
			headers.put(header.getName(), header.getValue());
		}

		return headers;
	}

	/**
	 * This method populates the request headers for the method.
	 * 
	 * @param method
	 *            the method to populate
	 * @param viewData
	 *            reference to the view data to get the header data.
	 */
	protected void populateMethodHeaders(HttpMethod method,
			HTTPViewData viewData) {
		for (Entry<String, String> entry : viewData.getRequestHttpHeaders()
				.entrySet()) {
			method.addRequestHeader(entry.getKey(), entry.getValue());
		}
	}

}
