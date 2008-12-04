package com.hephaestus.http;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.hephaestus.http.invokers.HttpMethodInvoker;
import com.hephaestus.http.views.HTTPViewData;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hephaestus.http"; //$NON-NLS-1$
	
	// The shared instance
	private static Activator plugin;
	
	// Import/Export path. This value is cached for the life of the plugin.
	private String importExportPath = "C:/"; //$NON-NLS-1$
	
	private HttpMethodInvoker getInvoker;
	private HttpMethodInvoker postInvoker;
	private HttpMethodInvoker putInvoker;
	private HttpMethodInvoker deleteInvoker;
	
	private ServiceTracker getTracker;
	private ServiceTracker postTracker;
	private ServiceTracker putTracker;
	private ServiceTracker deleteTracker;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		/**
		 * Massive Hack. This is just a trick to get the invoker bundle
		 * started. Apparently within Eclipse, the activator won't be fired
		 * until someone loads a class from the bundle. That kind of defeats
		 * the whole purpose of making the invoker bundle a service in the
		 * first place.
		 * 
		 * This will remain until we figure out how to start the invoker
		 * bundle without having to do a class load.
		 */
		Bundle[] bundles = context.getBundles();
		for (Bundle bundle : bundles) {
			if ("com.hephaestus.invoker".equals(bundle.getSymbolicName())) {
				bundle.start();
			}
		}
		
		setUpServiceTrackers(context);
	}

	private void setUpServiceTrackers(BundleContext context) throws Exception {
		InvokerServiceTrackerCustomizer getIstc = new InvokerServiceTrackerCustomizer(context);
		InvokerServiceTrackerCustomizer postIstc = new InvokerServiceTrackerCustomizer(context);
		InvokerServiceTrackerCustomizer putIstc = new InvokerServiceTrackerCustomizer(context);
		InvokerServiceTrackerCustomizer deleteIstc = new InvokerServiceTrackerCustomizer(context);
		
		getInvoker = getIstc;
		postInvoker = postIstc;
		putInvoker = putIstc;
		deleteInvoker = deleteIstc;
		
		getTracker = new ServiceTracker(context, context.createFilter(createFilterString("GET")), getIstc);
		postTracker = new ServiceTracker(context, context.createFilter(createFilterString("POST")), postIstc);
		putTracker = new ServiceTracker(context, context.createFilter(createFilterString("PUT")), putIstc);
		deleteTracker = new ServiceTracker(context, context.createFilter(createFilterString("DELETE")), deleteIstc);
		
		///Filter obj = null;
		
		getTracker.open();
		postTracker.open();
		putTracker.open();
		deleteTracker.open();
		
	}
	
	private String createFilterString(String verb) {
		StringBuilder sb = new StringBuilder("(&");
		sb.append("(objectclass=");
		sb.append(HttpMethodInvoker.class.getName());
		sb.append(")(METHOD=");
		sb.append(verb);
		sb.append("))");
		
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		
		if (getTracker != null) {
			getTracker.close();
		}
		
		if (postTracker != null) {
			postTracker.close();
		}
		
		if (putTracker != null) {
			putTracker.close();
		}
		
		if (deleteTracker != null) {
			deleteTracker.close();
		}
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public String getImportExportPath() {
		return importExportPath;
	}

	public void setImportExportPath(String importExportPath) {
		this.importExportPath = importExportPath;
	}
	
	public void invokeGetMethod(HTTPViewData viewData) {
		if (getInvoker == null) {
			throw new IllegalStateException("GetInvoker is not yet instantiated");
		}
		
		getInvoker.invoke(viewData);
	}
	public void invokePostMethod(HTTPViewData viewData) {
		if (postInvoker == null) {
			throw new IllegalStateException("PostInvoker is not yet instantiated");
		}
		
		postInvoker.invoke(viewData);
	}
	public void invokePutMethod(HTTPViewData viewData) {
		if (putInvoker == null) {
			throw new IllegalStateException("PutInvoker is not yet instantiated");
		}
		
		putInvoker.invoke(viewData);
	}
	public void invokeDeleteMethod(HTTPViewData viewData) {
		if (deleteInvoker == null) {
			throw new IllegalStateException("DeleteInvoker is not yet instantiated");
		}
		
		getInvoker.invoke(viewData);
	}
}
