package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class InsertTableRowAction extends Action {

	private Table tbl;

	public InsertTableRowAction(Table tbl) {
		this.tbl = tbl;
		setText("Insert");
		setToolTipText("Insert New Header");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		TableItem ti = new TableItem(tbl, SWT.NONE);
		ti.setText(0, "FIELD");
		ti.setText(1, "VALUE");
	}
}
