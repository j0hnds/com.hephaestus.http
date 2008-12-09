package com.hephaestus.http.actions;

import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.HTTPViewData;
import com.hephaestus.http.Messages;

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
		setText(Messages.getString("InvokeURLAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("InvokeURLAction.ToolTipText")); //$NON-NLS-1$
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
	}
	
	@Override
	public void run() {
		if (! viewData.validInputs()) {
			// Inputs not valid; don't execute.
			return;
		}
		
		String verb = viewData.getVerb();
		
//		HttpMethodInvoker invoker = null;
		try {
//			invoker = (HttpMethodInvoker) Activator.getBean(verb);
//			invoker.invoke(viewData);
			if ("GET".equals(verb)) {
				Activator.getDefault().invokeGetMethod(viewData);
			} else if ("POST".equals(verb)) {
				Activator.getDefault().invokePostMethod(viewData);
			} else if ("PUT".equals(verb)) {
				Activator.getDefault().invokePutMethod(viewData);
			} else if ("DELETE".equals(verb)) {
				Activator.getDefault().invokeDeleteMethod(viewData);
			}
		}
		catch (Exception e) {
			Object[] arguments = { verb, e.getLocalizedMessage() };
			viewData.showErrorMessage(MessageFormat.format(Messages.getString("InvokeURLAction.BeanVerbError"), arguments)); //$NON-NLS-1$
		} 

	}

}
