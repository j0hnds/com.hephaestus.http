package com.hephaestus.http.invokers;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * An invoker for an HTTP POST.
 * 
 * @author Dave Sieh
 */
public class PostMethodInvoker extends BaseEntityEnclosingMethodInvoker {

	@Override
	protected HttpMethod getMethod(String uri) {
		return new PostMethod(uri);
	}

}
