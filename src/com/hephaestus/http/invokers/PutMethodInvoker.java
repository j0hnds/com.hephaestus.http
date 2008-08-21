package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PutMethod;

import com.hephaestus.http.views.HTTPViewData;

public class PutMethodInvoker extends BaseEntityEnclosingMethodInvoker {

	public void invoke(HTTPViewData viewData) {
		String url = viewData.getURL();
		HttpClient client = getClient();
		PutMethod method = new PutMethod(url);
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
