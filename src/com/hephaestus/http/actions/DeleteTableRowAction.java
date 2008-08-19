package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class DeleteTableRowAction extends Action {

	private Table tbl;

	public DeleteTableRowAction(Table tbl) {
		this.tbl = tbl;
		setText("Delete");
		setToolTipText("Delete Row(s)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		int[] selectedItems = tbl.getSelectionIndices();
		tbl.remove(selectedItems);
	}
}
