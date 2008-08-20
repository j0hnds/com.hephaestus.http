package com.hephaestus.http.actions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.preferences.PreferenceConstants;
import com.hephaestus.http.views.HTTPViewData;

public class InvokeURLAction extends Action {

	private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final String FORM_ENCODING = "UTF-8";
	
	private HTTPViewData viewData;

	public InvokeURLAction(HTTPViewData viewData) {
		this.viewData = viewData;
		setText("Invoke");
		setToolTipText("Invoke HTTP Request");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}
	
	private HttpClient getClient() {
		HttpClient client = new HttpClient();
		Preferences prefs = Activator.getDefault().getPluginPreferences();
		String proxy = prefs.getString(PreferenceConstants.P_PROXY_HOST_PORT);
		if (proxy != null && proxy.length() > 0) {
			String[] cmps = proxy.split(":");
			client.getHostConfiguration().setProxy(cmps[0], Integer.parseInt(cmps[1]));
		}
		
		return client;
	}

	@Override
	public void run() {
		String verb = viewData.getVerb();

		if ("GET".equals(verb)) {
			invokeGET();
		}
		else if ("POST".equals(verb)) {
			invokePOST();
		}
		else if ("PUT".equals(verb)) {
			invokePUT();
		}
		else if ("DELETE".equals(verb)) {
			invokeDELETE();
		}

	}

	private void invokeDELETE() {
		String url = viewData.getURL();
		HttpClient client = getClient();
		DeleteMethod method = new DeleteMethod(url);
		populateMethodHeaders(method);

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method);
		}
		catch (Exception e) {
			viewData.showErrorMessage(e.getLocalizedMessage());
		}
		finally {
			method.releaseConnection();
		}
	}

	private void collectResponse(HttpMethod method) throws IOException {
		viewData.setStatus(Integer.toString(method.getStatusCode()));
		viewData.setResponseData(getResponseBody(method));
		viewData.setResponseHttpHeaders(getResponseHeaders(method));
	}

	private void populateMethodHeaders(HttpMethod method) {
		for (Entry<String, String> entry : viewData.getRequestHttpHeaders()
				.entrySet()) {
			method.addRequestHeader(entry.getKey(), entry.getValue());
		}
	}

	private Map<String, String> getResponseHeaders(HttpMethod method) {
		Map<String, String> headers = new HashMap<String, String>();

		for (Header header : method.getResponseHeaders()) {
			headers.put(header.getName(), header.getValue());
		}

		return headers;
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

	private void invokePUT() {
		String url = viewData.getURL();
		HttpClient client = getClient();
		PutMethod method = new PutMethod(url);
		populateMethodHeaders(method);
		try {
			populateMethodPostData(method);
			populateMethodBulkData(method);
		}
		catch (UnsupportedEncodingException e1) {
			viewData.showErrorMessage(e1.getLocalizedMessage());
		}

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method);
		}
		catch (Exception e) {
			viewData.showErrorMessage(e.getLocalizedMessage());
		}
	}

	private void populateMethodBulkData(EntityEnclosingMethod method)
			throws UnsupportedEncodingException {
		String bulkData = viewData.getBulkPostData();
		String contentType = viewData.getContentType();
		if (bulkData != null && bulkData.length() > 0) {
			method.setRequestEntity(new StringRequestEntity(bulkData,
					contentType, FORM_ENCODING));
		}
	}

	private void populateMethodPostData(EntityEnclosingMethod method)
			throws UnsupportedEncodingException {
		Map<String, String> postDataFields = viewData
				.getRequestPostDataFields();
		int count = postDataFields.size();
		if (count > 0) {
			NameValuePair[] pairs = new NameValuePair[count];
			int index = 0;
			for (Entry<String, String> entry : postDataFields.entrySet()) {
				pairs[index++] = new NameValuePair(entry.getKey(), entry
						.getValue());
			}
			String requestParams = EncodingUtil.formUrlEncode(pairs, FORM_ENCODING);
			method.setRequestEntity(new StringRequestEntity(requestParams, FORM_CONTENT_TYPE, FORM_ENCODING));
		}
	}

	private void invokePOST() {
		String url = viewData.getURL();
		HttpClient client = getClient();
		PostMethod method = new PostMethod(url);
		populateMethodHeaders(method);
		try {
			populateMethodPostData(method);
			populateMethodBulkData(method);
		}
		catch (UnsupportedEncodingException e1) {
			viewData.showErrorMessage(e1.getLocalizedMessage());
		}

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method);
		}
		catch (Exception e) {
			viewData.showErrorMessage(e.getLocalizedMessage());
		}
	}

	private void invokeGET() {
		String url = viewData.getURL();
		HttpClient client = getClient();
		GetMethod method = new GetMethod(url);
		populateMethodHeaders(method);

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method);
		}
		catch (Exception e) {
			viewData.showErrorMessage(e.getLocalizedMessage());
		}
		finally {
			method.releaseConnection();
		}
	}

}
