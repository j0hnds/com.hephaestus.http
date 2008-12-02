package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;

/**
 * An invoker for an HTTP DELETE method.
 * 
 * @author Dave Sieh
 */
public class DeleteMethodInvoker extends BaseHttpMethodInvoker {

	@Override
	protected HttpMethod getMethod(String uri) {
		return new DeleteMethod(uri);
	}

}
