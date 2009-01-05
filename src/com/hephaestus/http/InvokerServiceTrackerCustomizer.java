package com.hephaestus.http;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * This class is responsible for customizing the service tracker to support
 * HttpMethodInvoker services with different methods.
 * 
 * @author siehd
 */
public class InvokerServiceTrackerCustomizer implements
        ServiceTrackerCustomizer, HttpMethodInvoker {

    // The plugin bundle context.
    private BundleContext bc;
    
    // A reference to the invoker that was found.
    private HttpMethodInvoker invoker;

    /**
     * Constructs a new InvokerServiceTrackerCustomizer
     * 
     * @param bc the plugin bundle context.
     */
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
            throw new IllegalStateException("Requested Service not registered"); //$NON-NLS-1$
        }

        invoker.invoke(viewData);
    }

}
