package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Messages;
import com.hephaestus.http.model.NameValuePair;
import com.hephaestus.http.model.NameValuePairs;

/**
 * This action is responsible for adding new NameValuePair's to the
 * list of NameValuePairs.
 * 
 * @author Dave Sieh
 */
public class InsertTableRowAction extends Action {

	// The name value pairs being managed.
	private NameValuePairs pairs;

	/**
	 * Constructs a new InsertTableRowAction object.
	 * 
	 * @param pairs the name value pairs being managed.
	 */
	public InsertTableRowAction(NameValuePairs pairs) {
		this.pairs = pairs;
		setText(Messages.getString("InsertTableRowAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("InsertTableRowAction.ToolTipText")); //$NON-NLS-1$
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		pairs.addNameValuePair(new NameValuePair(Messages.getString("InsertTableRowAction.NVPName"), Messages.getString("InsertTableRowAction.NVPValue"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
