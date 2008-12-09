package com.hephaestus.http.actions;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.HTTPViewData;
import com.hephaestus.http.Messages;
import com.hephaestus.http.adapters.File2View;

/**
 * This action is responsible for importing the data exported earlier.
 * 
 * @author Dave Sieh
 */
public class ImportHttpRequestDataAction extends Action {

	// The UI View data.
	private HTTPViewData viewData;

	/**
	 * Constructs a new ImportHttpRequestDataAction object.
	 * 
	 * @param viewData
	 *            the UI view data.
	 */
	public ImportHttpRequestDataAction(HTTPViewData viewData) {
		this.viewData = viewData;
		setText(Messages.getString("ImportHttpRequestDataAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ImportHttpRequestDataAction.ToolTipText")); //$NON-NLS-1$
		setImageDescriptor(Activator.getImageDescriptor("icons/door_in.png")); //$NON-NLS-1$

	}

	@Override
	public void run() {
		// First, bring up a dialog box, get the path to the file, then
		// parse it in.
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay()
				.getActiveShell(), SWT.OPEN);
		dlg.setFilterNames(new String[] { Messages.getString("ImportHttpRequestDataAction.FilterNames") }); //$NON-NLS-1$
		dlg.setFilterExtensions(new String[] { "*.xml" }); //$NON-NLS-1$
		dlg.setFilterPath(Activator.getDefault().getImportExportPath());
		String path = dlg.open();
		if (path != null) {
			File file = new File(path);

			Activator.getDefault().setImportExportPath(
					file.getParentFile().getAbsolutePath());
			File2View f2v = new File2View(file);

			try {
				f2v.restoreView(viewData);
			}
			catch (Exception e) {
				Object[] arguments = { path, e.getLocalizedMessage() };
				viewData.showErrorMessage(MessageFormat.format(Messages.getString("ImportHttpRequestDataAction.ImportError"), arguments)); //$NON-NLS-1$
			}
		}

	}

}
