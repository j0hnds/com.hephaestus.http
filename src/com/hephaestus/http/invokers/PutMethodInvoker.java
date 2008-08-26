package com.hephaestus.http.invokers;

import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PutMethod;

import com.hephaestus.http.Messages;
import com.hephaestus.http.views.HTTPViewData;

/**
 * Invoker for an HTTP PUT
 * 
 * @author Dave Sieh
 */
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
			Object[] arguments = { e1.getLocalizedMessage() };
			viewData.showErrorMessage(MessageFormat.format(Messages.getString("PutMethodInvoker.EntityPopulationError") , arguments)); //$NON-NLS-1$
		}

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method, viewData);
		}
		catch (Exception e) {
			Object[] arguments = { e.getLocalizedMessage() };
			viewData.showErrorMessage(MessageFormat.format(Messages.getString("PutMethodInvoker.InvocationError"), arguments)); //$NON-NLS-1$
		}
	}

}
