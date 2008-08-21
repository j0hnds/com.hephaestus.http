package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.invokers.HttpMethodInvoker;
import com.hephaestus.http.views.HTTPViewData;

public class InvokeURLAction extends Action {

	private HTTPViewData viewData;

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
