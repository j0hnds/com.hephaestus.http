package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Messages;
import com.hephaestus.http.util.XMLUtils;
import com.hephaestus.http.views.ui.ResponseDataAccess;

/**
 * This Action is responsible for formatting the contents of the
 * response data field as XML.
 * 
 * @author Dave Sieh
 */
public class FormatXMLAction extends Action {

	private ResponseDataAccess rdAccess;
	
	/**
	 * Constructs a new FormatXMLAction object.
	 * 
	 * @param rdAccess reference to the ResponseDataAccess interface
	 */
	public FormatXMLAction(ResponseDataAccess rdAccess) {
		this.rdAccess = rdAccess;
		setText(Messages.getString("FormatXMLAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("FormatXMLAction.ToolTipText")); //$NON-NLS-1$
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		String rd = rdAccess.getResponseData();
		if (rd != null && rd.length() > 0) {
			rdAccess.setResponseData(XMLUtils.formatXMLString(rd));
		}
	}
	
}
