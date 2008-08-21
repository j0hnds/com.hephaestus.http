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
import com.hephaestus.http.views.HTTPViewData;

public abstract class BaseHttpMethodInvoker implements HttpMethodInvoker {
	
	protected HttpClient getClient() {
		HttpClient client = new HttpClient();
		Preferences prefs = Activator.getDefault().getPluginPreferences();
		String proxy = prefs.getString(PreferenceConstants.P_PROXY_HOST_PORT);
		if (proxy != null && proxy.length() > 0) {
			String[] cmps = proxy.split(":");
			client.getHostConfiguration().setProxy(cmps[0], Integer.parseInt(cmps[1]));
		}
		
		return client;
	}


	protected void collectResponse(HttpMethod method, HTTPViewData viewData) throws IOException {
		viewData.setStatus(Integer.toString(method.getStatusCode()));
		viewData.setResponseData(getResponseBody(method));
		viewData.setResponseHttpHeaders(getResponseHeaders(method));
	}


	private String getResponseBody(HttpMethod method) throws IOException {
		InputStream is = method.getResponseBodyAsStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;

		while ((len = is.read(buffer, 0, buffer.length)) >= 0) {
			baos.write(buffer, 0, len);
		}

		return baos.toString();
	}

	private Map<String, String> getResponseHeaders(HttpMethod method) {
		Map<String, String> headers = new HashMap<String, String>();

		for (Header header : method.getResponseHeaders()) {
			headers.put(header.getName(), header.getValue());
		}

		return headers;
	}
	
	protected void populateMethodHeaders(HttpMethod method, HTTPViewData viewData) {
		for (Entry<String, String> entry : viewData.getRequestHttpHeaders()
				.entrySet()) {
			method.addRequestHeader(entry.getKey(), entry.getValue());
		}
	}

}
