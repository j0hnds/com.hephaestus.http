package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.views.NameValuePair;
import com.hephaestus.http.views.NameValuePairs;

public class InsertTableRowAction extends Action {

	private NameValuePairs pairs;

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
