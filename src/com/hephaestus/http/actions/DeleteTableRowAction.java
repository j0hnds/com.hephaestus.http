package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.views.NameValuePair;
import com.hephaestus.http.views.NameValuePairs;

public class DeleteTableRowAction extends Action {

	private TableViewer table;

	public DeleteTableRowAction(TableViewer table) {
		this.table = table;
		setText("Delete");
		setToolTipText("Delete Row(s)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		NameValuePairs pairs = (NameValuePairs) table.getInput();
		IStructuredSelection sel = (IStructuredSelection) table.getSelection();
		for (Object nvp : sel.toArray()) {
			pairs.removeNameValuePair((NameValuePair) nvp);
		}
	}
}
