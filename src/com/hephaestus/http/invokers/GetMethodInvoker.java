package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * An invoker for an HTTP Get method.
 * 
 * @author Dave Sieh
 */
public class GetMethodInvoker extends BaseHttpMethodInvoker {

	@Override
	protected HttpMethod getMethod(String uri) {
		return new GetMethod(uri);
	}

}
