package com.hephaestus.http.actions;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Activator;
import com.hephaestus.http.views.HTTPViewData;
import com.hephaestus.httpRequestData.BulkPostDataType;
import com.hephaestus.httpRequestData.FileUploadType;
import com.hephaestus.httpRequestData.HttpRequestDataDocument;
import com.hephaestus.httpRequestData.HttpRequestDataType;
import com.hephaestus.httpRequestData.NameValuePairType;
import com.hephaestus.httpRequestData.PostDataType;

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
	 * @param viewData the UI view data.
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
		FileDialog dlg = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), SWT.OPEN);
		dlg.setFilterNames(new String[] { "XML Files (*.xml)" });
		dlg.setFilterExtensions(new String[] { "*.xml" });
		dlg.setFilterPath(Activator.getDefault().getImportExportPath());
		String path = dlg.open();
		if (path != null) {
			File file = new File(path);
			
			Activator.getDefault().setImportExportPath(file.getParentFile().getAbsolutePath());
			
			try {
				viewData.clearInputs();
				HttpRequestDataDocument doc = HttpRequestDataDocument.Factory.parse(file);
				HttpRequestDataType request = doc.getHttpRequestData();
				viewData.setVerb(request.getVerb());
				viewData.setProtocol(request.getProtocol());
				viewData.setHostPort(request.getHostport());
				viewData.setURI(request.getUri());
				
				PostDataType pdt = request.getHeaders();
				if (pdt != null) {
					Map<String,String> headers = new HashMap<String,String>();
					for (NameValuePairType nvp : pdt.getNameValuePairArray()) {
						headers.put(nvp.getName(), nvp.getValue());
					}
					viewData.setRequestHttpHeaders(headers);
				}
				
				pdt = request.getPostData();
				if (pdt != null) {
					Map<String,String> postdata = new HashMap<String,String>();
					for (NameValuePairType nvp : pdt.getNameValuePairArray()) {
						postdata.put(nvp.getName(), nvp.getValue());
					}
					viewData.setRequestPostDataFields(postdata);
				}
				
				BulkPostDataType bpdt = request.getBulkPostData();
				if (bpdt != null) {
					String bulkData = bpdt.stringValue();
					if (bulkData != null && bulkData.length() > 0) {
						viewData.setBulkPostData(bulkData);
						viewData.setContentType(bpdt.getContentType());
					}
				}
				
				FileUploadType fut = request.getFileUpload();
				if (fut != null) {
					String fileUploadPath = fut.getPath();
					if (fileUploadPath != null && fileUploadPath.length() > 0) {
						viewData.setFileUploadPath(fileUploadPath);
					}
				}
			}
			catch (Exception e) {
				viewData.showErrorMessage("Error loading the import data (" + path + "): " + e.getLocalizedMessage());
			}
		}

	}

}
