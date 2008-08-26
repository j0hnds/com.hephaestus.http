package com.hephaestus.http.actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.httpRequestData.BulkPostDataType;
import com.hephaestus.httpRequestData.FileUploadType;
import com.hephaestus.httpRequestData.HttpRequestDataDocument;
import com.hephaestus.httpRequestData.NameValuePairType;
import com.hephaestus.httpRequestData.PostDataType;
import com.hephaestus.http.Activator;
import com.hephaestus.http.views.HTTPViewData;
import com.hephaestus.httpRequestData.HttpRequestDataType;

/**
 * This action is responsible for exporting the contents of the request
 * data of the form to an XML file for later import.
 * 
 * @author Dave Sieh
 */
public class ExportHttpRequestDataAction extends Action {

	// The data from the UI.
	private HTTPViewData viewData;
	

	/**
	 * Constructs a new ExportHttpRequestDataAction object.
	 * 
	 * @param viewData the UI view data.
	 */
	public ExportHttpRequestDataAction(HTTPViewData viewData) {
		this.viewData = viewData;
		setText("Export");
		setToolTipText("Export HTTP Request Data");
		setImageDescriptor(Activator.getImageDescriptor("icons/door_out.png"));

	}


	@Override
	public void run() {
		HttpRequestDataDocument doc = HttpRequestDataDocument.Factory.newInstance();
		HttpRequestDataType rd = doc.addNewHttpRequestData();
		
		rd.setVerb(viewData.getVerb());
		rd.setProtocol(viewData.getProtocol());
		rd.setHostport(viewData.getHostPort());
		rd.setUri(viewData.getURI());
		
		PostDataType pdt = rd.addNewHeaders();
		for (Entry<String,String> entry : viewData.getRequestHttpHeaders().entrySet()) {
			NameValuePairType nvp = pdt.addNewNameValuePair();
			nvp.setName(entry.getKey());
			nvp.setValue(entry.getValue());
		}

		String bulkPostData = viewData.getBulkPostData();
		Map<String,String> postData = viewData.getRequestPostDataFields();
		String fileUploadPath = viewData.getFileUploadPath();
		if (bulkPostData != null && bulkPostData.length() > 0) {
			BulkPostDataType bpdt = rd.addNewBulkPostData();
			bpdt.set(bulkPostData);
			bpdt.setContentType(viewData.getContentType());
		} else if (fileUploadPath != null && fileUploadPath.length() > 0) {
			FileUploadType fut = rd.addNewFileUpload();
			fut.setPath(fileUploadPath);
		} else if (postData.size() > 0) {
			pdt = rd.addNewPostData();
			for (Entry<String,String> entry : postData.entrySet()) {
				NameValuePairType nvp = pdt.addNewNameValuePair();
				nvp.setName(entry.getKey());
				nvp.setValue(entry.getValue());
			}
		}
		
		// Now, bring up a dialog box to get the file name, then save
		// the stuff.
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.SAVE);
		dlg.setFilterNames(new String[] { "XML Files (*.xml)" });
		dlg.setFilterExtensions(new String[] { "*.xml" });
		dlg.setFilterPath(Activator.getDefault().getImportExportPath());
		String path = dlg.open();
		if (path != null) {
			File file = new File(path);
			Activator.getDefault().setImportExportPath(file.getParentFile().getAbsolutePath());
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));
				bw.write(doc.toString());
				bw.close();
			}
			catch (IOException e) {
				viewData.showErrorMessage("Error saving export data (" + path + "):" + e.getLocalizedMessage());
			}
		}
	}
}
