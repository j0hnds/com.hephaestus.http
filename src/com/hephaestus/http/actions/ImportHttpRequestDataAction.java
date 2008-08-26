package com.hephaestus.http.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.adapters.File2View;
import com.hephaestus.http.views.HTTPViewData;

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
		setText("Import");
		setToolTipText("Import HTTP Request Data");
		setImageDescriptor(Activator.getImageDescriptor("icons/door_in.png"));

	}

	@Override
	public void run() {
		// First, bring up a dialog box, get the path to the file, then
		// parse it in.
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay()
				.getActiveShell(), SWT.OPEN);
		dlg.setFilterNames(new String[] { "XML Files (*.xml)" });
		dlg.setFilterExtensions(new String[] { "*.xml" });
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
				viewData.showErrorMessage("Error loading the import data ("
						+ path + "): " + e.getLocalizedMessage());
			}
		}

	}

}
