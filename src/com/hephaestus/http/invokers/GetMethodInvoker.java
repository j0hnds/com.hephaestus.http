package com.hephaestus.http.invokers;

import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.hephaestus.http.Messages;
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
			Object[] arguments = { e.getLocalizedMessage() };
			viewData.showErrorMessage(MessageFormat.format(Messages.getString("GetMethodInvoker.InvocationError"), arguments)); //$NON-NLS-1$
		}
		finally {
			method.releaseConnection();
		}
	}

}
