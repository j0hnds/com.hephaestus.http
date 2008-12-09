package com.hephaestus.http;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;


public class InvokerServiceTrackerCustomizer implements
		ServiceTrackerCustomizer, HttpMethodInvoker {
	
	private BundleContext bc;
	private HttpMethodInvoker invoker;
	
	public InvokerServiceTrackerCustomizer(BundleContext bc) {
		this.bc = bc;
		
	}

	public Object addingService(ServiceReference reference) {
		// Just get the service out of the reference
		invoker = (HttpMethodInvoker) bc.getService(reference);
		return invoker;
	}

	public void modifiedService(ServiceReference reference, Object service) {
		// Replace the current service reference with the new one.
		invoker = (HttpMethodInvoker) bc.getService(reference);
	}

	public void removedService(ServiceReference reference, Object service) {
		// Remove knowledge of service.
		invoker = null;
	}
	
	public void invoke(HTTPViewData viewData) {
		if (invoker == null) {
			throw new IllegalStateException("Requested Service not registered");
		}
		
		invoker.invoke(viewData);
	}

}
