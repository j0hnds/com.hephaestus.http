package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.model.NameValuePairs;

/**
 * Removes all the rows of the associated table.
 * 
 * @author Dave Sieh
 */
public class DeleteAllTableRowsAction extends Action {
	
	// The table viewer to delete the rows from.
	private TableViewer table;

	/**
	 * Constructs a new DeleteAllTableRowsAction.
	 * 
	 * @param table the table to delete the rows from.
	 */
	public DeleteAllTableRowsAction(TableViewer table) {
		this.table = table;
		setText("Delete All");
		setToolTipText("Delete All Row(s)");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		NameValuePairs pairs = (NameValuePairs) table.getInput();
		pairs.removeAll();
	}
	
	
}
