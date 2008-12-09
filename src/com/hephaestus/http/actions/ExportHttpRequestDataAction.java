package com.hephaestus.http.actions;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.HTTPViewData;
import com.hephaestus.http.Messages;
import com.hephaestus.http.adapters.View2File;

/**
 * This action is responsible for exporting the contents of the request data of
 * the form to an XML file for later import.
 * 
 * @author Dave Sieh
 */
public class ExportHttpRequestDataAction extends Action {

	// The data from the UI.
	private HTTPViewData viewData;

	/**
	 * Constructs a new ExportHttpRequestDataAction object.
	 * 
	 * @param viewData
	 *            the UI view data.
	 */
	public ExportHttpRequestDataAction(HTTPViewData viewData) {
		this.viewData = viewData;
		setText(Messages.getString("ExportHttpRequestDataAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExportHttpRequestDataAction.ToolTipText")); //$NON-NLS-1$
		setImageDescriptor(Activator.getImageDescriptor("icons/door_out.png")); //$NON-NLS-1$

	}

	@Override
	public void run() {
		// Now, bring up a dialog box to get the file name, then save
		// the stuff.
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay()
				.getActiveShell(), SWT.SAVE);
		dlg.setFilterNames(new String[] { Messages.getString("ExportHttpRequestDataAction.FilterNames") }); //$NON-NLS-1$
		dlg.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
		dlg.setFilterPath(Activator.getDefault().getImportExportPath());
		String path = dlg.open();
		if (path != null) {
			File file = new File(path);
			Activator.getDefault().setImportExportPath(
					file.getParentFile().getAbsolutePath());
			View2File v2f = new View2File(viewData);
			try {
				v2f.saveView(file);
			}
			catch (IOException e) {
				Object[] arguments = { path, e.getLocalizedMessage() };
				viewData.showErrorMessage(MessageFormat.format(Messages.getString("ExportHttpRequestDataAction.ExportError"), arguments)); //$NON-NLS-1$
			}
		}
	}
}
