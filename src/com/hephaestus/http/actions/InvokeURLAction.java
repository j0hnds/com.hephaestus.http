package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.invokers.HttpMethodInvoker;
import com.hephaestus.http.views.HTTPViewData;

/**
 * This action is responsible for making the URL call defined by the HTTP method 
 * information provided by the view data.
 * 
 * @author Dave Sieh
 */
public class InvokeURLAction extends Action {

	// The view data
	private HTTPViewData viewData;

	/**
	 * Constructs a new InvokeURLAction object for the specified view data.
	 * 
	 * @param viewData the view data
	 */
	public InvokeURLAction(HTTPViewData viewData) {
		this.viewData = viewData;
		setText("Invoke");
		setToolTipText("Invoke HTTP Request");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}
	
	@Override
	public void run() {
		if (! viewData.validInputs()) {
			// Inputs not valid; don't execute.
			return;
		}
		
		String verb = viewData.getVerb();
		
		HttpMethodInvoker invoker = null;
		try {
			invoker = (HttpMethodInvoker) Activator.getBean(verb);
			invoker.invoke(viewData);
		}
		catch (Exception e) {
			// Ignore errors retrieving the bean; simply don't do anything
		} 

	}

}
