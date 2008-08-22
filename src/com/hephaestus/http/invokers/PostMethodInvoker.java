package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.hephaestus.http.views.HTTPViewData;

/**
 * An invoker for an HTTP POST.
 * 
 * @author Dave Sieh
 */
public class PostMethodInvoker extends BaseEntityEnclosingMethodInvoker {

	public void invoke(HTTPViewData viewData) {
		String url = viewData.getURL();
		HttpClient client = getClient();
		PostMethod method = new PostMethod(url);
		populateMethodHeaders(method, viewData);
		try {
			populateEntity(method, viewData);
		}
		catch (Exception e1) {
			viewData.showErrorMessage(e1.getLocalizedMessage());
		}

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method, viewData);
		}
		catch (Exception e) {
			viewData.showErrorMessage(e.getLocalizedMessage());
		}
	}

}
