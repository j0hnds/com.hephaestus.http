package com.hephaestus.http.invokers;

import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import com.hephaestus.http.Messages;
import com.hephaestus.http.views.HTTPViewData;

/**
 * An invoker for an HTTP Get method.
 * 
 * @author Dave Sieh
 */
public class GetMethodInvoker extends BaseHttpMethodInvoker {

	public void invoke(HTTPViewData viewData) {
		String protocol = viewData.getProtocol();
		Protocol oldProtocol = null;
		HttpClient client = getClient();
		GetMethod method = null;
		if (isStrictSSL() || "http".equals(protocol)) {
			String url = viewData.getURL();
			method = new GetMethod(url);
		}
		else {
			oldProtocol = Protocol.getProtocol("https");
			ProtocolSocketFactory newFactory = new EasySSLProtocolSocketFactory();
			
			String hostPort = viewData.getHostPort();
			String[] hp = hostPort.split(":");
			String host = hp[0];
			String uri = "/" + viewData.getURI();
			int port = 443;
			if (hp.length > 1) {
				try {
					port = Integer.parseInt(hp[1]);
				}
				catch (NumberFormatException e) {
					port = 443;
				}
			}
			Protocol newProtocol = new Protocol("https", newFactory, 443);
			Protocol.registerProtocol("https", newProtocol);
			client.getHostConfiguration().setHost(
					host,
					port,
					newProtocol);
			method = new GetMethod(uri);
//			method = new GetMethod(viewData.getURL());
		}
		populateMethodHeaders(method, viewData);

		try {
			@SuppressWarnings("unused")
			int statusCode = client.executeMethod(method);
			collectResponse(method, viewData);
		}
		catch (Exception e) {
			Object[] arguments = { e.getLocalizedMessage() };
			viewData.showErrorMessage(MessageFormat.format(Messages
					.getString("GetMethodInvoker.InvocationError"), arguments)); //$NON-NLS-1$
		}
		finally {
			method.releaseConnection();
			if (oldProtocol != null) {
				Protocol.registerProtocol("https", oldProtocol);
			}
		}
	}

}
