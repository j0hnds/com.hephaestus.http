package com.hephaestus.http;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hephaestus.http"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	// The bean factory
	private BeanFactory context;
	
	// Import/Export path
	private String importExportPath = "C:/"; //$NON-NLS-1$
	
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
		
		// Load up the application context.
		try {
			this.context = new ClassPathXmlApplicationContext("/com.hephaestus/http/config/HttpContext.xml"); //$NON-NLS-1$
		}
		catch (Exception e) {
			System.out.println(Messages.getString("Activator.ApplicationContextException")); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	public static Object getBean(String beanName) {
		return getDefault().getBeanFactory().getBean(beanName);
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
	
	/**
	 * Returns the bean factory for this plugin.
	 * 
	 * @return reference to the bean factory.
	 */
	public BeanFactory getBeanFactory() {
		return context;
	}

	public String getImportExportPath() {
		return importExportPath;
	}

	public void setImportExportPath(String importExportPath) {
		this.importExportPath = importExportPath;
	}
}
