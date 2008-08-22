package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.hephaestus.http.views.HTTPViewData;

/**
 * An invoker for an HTTP Get method.
 * 
 * @author Dave Sieh
 */
public class GetMethodInvoker extends BaseHttpMethodInvoker {

	public void invoke(HTTPViewData viewData) {
		String url = viewData.getURL();
		HttpClient client = getClient();
		GetMethod method = new GetMethod(url);
		populateMethodHeaders(method, viewData);

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method, viewData);
		}
		catch (Exception e) {
			viewData.showErrorMessage(e.getLocalizedMessage());
		}
		finally {
			method.releaseConnection();
		}
	}

}
