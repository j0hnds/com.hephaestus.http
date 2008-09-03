package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Messages;
import com.hephaestus.http.model.NameValuePair;
import com.hephaestus.http.model.NameValuePairs;

/**
 * This action is responsible for the deletion of a row of data in a
 * TableViewer.
 * 
 * @author Dave Sieh
 */
public class DeleteTableRowAction extends Action {

	// The table viewer to delete the row from.
	private TableViewer table;

	/**
	 * Constructs a new DeleteTableRowAction for the specified table.
	 * 
	 * @param table the table viewer to delete rows from.
	 */
	public DeleteTableRowAction(TableViewer table) {
		this.table = table;
		setText(Messages.getString("DeleteTableRowAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("DeleteTableRowAction.ToolTipText")); //$NON-NLS-1$
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
