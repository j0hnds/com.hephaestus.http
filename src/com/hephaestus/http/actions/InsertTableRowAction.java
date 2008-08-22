package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.views.NameValuePair;
import com.hephaestus.http.views.NameValuePairs;

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
		setText("Insert");
		setToolTipText("Insert New Header");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		pairs.addNameValuePair(new NameValuePair("Name", "Value"));
	}
}
