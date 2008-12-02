package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PutMethod;

/**
 * Invoker for an HTTP PUT
 * 
 * @author Dave Sieh
 */
public class PutMethodInvoker extends BaseEntityEnclosingMethodInvoker {

	@Override
	protected HttpMethod getMethod(String uri) {
		return new PutMethod(uri);
	}

}
