package com.hephaestus.http.views;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;

import com.hephaestus.http.Activator;
import com.hephaestus.http.actions.InvokeURLAction;
import com.hephaestus.http.preferences.PreferenceConstants;
import com.hephaestus.http.views.ui.RequestData;
import com.hephaestus.http.views.ui.ResponseData;
import com.hephaestus.http.views.ui.URLFields;

public class HTTPView extends ViewPart implements HTTPViewData,
		IPropertyChangeListener {

	// Action to invoke the URL
	private Action invokeURLAction;

	// The URL formulation control
	private URLFields cmpURLFields;

	// The text field for the response status.
	private Text tfStatus;

	// The request data control
	private RequestData requestData;
	
	// The response data control
	private ResponseData responseData;

	/**
	 * Constructs a new HTTPView object.
	 */
	public HTTPView() {
		Activator.getDefault().getPluginPreferences()
				.addPropertyChangeListener(this);

	}

	@Override
	public void dispose() {
		Activator.getDefault().getPluginPreferences()
				.removePropertyChangeListener(this);
		super.dispose();
	}

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		// Create a composite for the entry fields
		Composite entry = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		entry.setLayoutData(gd);
		createEntryFields(entry);

		// Create a composite for the result fields
		Composite result = new Composite(parent, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		result.setLayoutData(gd);
		createResultFields(result);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(cmpURLFields,
				"com.hephaestus.http.viewer");
		makeActions();
		requestData.hookContextMenu(getSite());
		contributeToActionBars();
	}

	/**
	 * Creates the overall layout of the user-interface controls for the
	 * response data.
	 * 
	 * @param result
	 *            the parent of the controls
	 */
	private void createResultFields(Composite result) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		result.setLayout(layout);

		Label lblStatusCode = new Label(result, SWT.NONE);
		lblStatusCode.setText("Status:");
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblStatusCode.setLayoutData(gd);

		tfStatus = new Text(result, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tfStatus.setLayoutData(gd);

		responseData = new ResponseData(result, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		gd.heightHint = 100;
		responseData.setLayoutData(gd);
	}

	/**
	 * Creates the entry fields.
	 * 
	 * @param entry the parent
	 */
	private void createEntryFields(Composite entry) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		entry.setLayout(layout);

		Label lblURL = new Label(entry, SWT.NONE);
		lblURL.setText("URL:");
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblURL.setLayoutData(gd);

		cmpURLFields = new URLFields(entry, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		cmpURLFields.setLayoutData(gd);

		requestData = new RequestData(entry, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		gd.heightHint = 100;
		requestData.setLayoutData(gd);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(invokeURLAction);
		manager.add(new Separator());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(invokeURLAction);
	}

	private void makeActions() {
		invokeURLAction = new InvokeURLAction(this);
	}

	public void showErrorMessage(String message) {
		MessageDialog.openError(cmpURLFields.getShell(), "HTTP Test", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		cmpURLFields.setFocus();
	}

	// Implementation of HTTPViewData
	public String getBulkPostData() {
		return requestData.getBulkPostData();
	}

	public Map<String, String> getRequestHttpHeaders() {
		return requestData.getRequestHttpHeaders();
	}

	public Map<String, String> getRequestPostDataFields() {
		return requestData.getRequestPostDataFields();
	}

	public String getURL() {
		return cmpURLFields.getURL();
	}

	public void setResponseData(String data) {
		responseData.setResponseData(data);
	}

	public void setResponseHttpHeaders(Map<String, String> headers) {
		responseData.setResponseHttpHeaders(headers);
	}

	public void setStatus(String status) {
		tfStatus.setText(status);
		// If we got here, we didn't get an error, so save off the
		// value of the URI we used in the combo box.
		cmpURLFields.saveCurrentURI();
	}

	public String getVerb() {
		return cmpURLFields.getVerb();
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (PreferenceConstants.P_HOST_PORTS.equals(event.getProperty())) {
			resetHostPorts();
		}
	}

	private void resetHostPorts() {
		cmpURLFields.resetHostPorts();
	}

	public String getContentType() {
		return requestData.getContentType();
	}

	public boolean validInputs() {
		boolean valid = true;

		// Make sure there is a protocol selected
		String protocol = cmpURLFields.getProtocol();
		if (protocol == null || protocol.length() == 0) {
			showErrorMessage("Must select a protocol");
			valid = false;
		}

		// Make sure there is a host:port selected
		String hostPort = cmpURLFields.getHostPort();
		if (hostPort == null || hostPort.length() == 0) {
			showErrorMessage("Must select a host:port");
			valid = false;
		}

		// Make sure there is a verb provided
		String verb = cmpURLFields.getVerb();
		if (verb == null || verb.length() == 0) {
			showErrorMessage("Must select a verb");
			valid = false;
		}

		String fileUploadPath = getFileUploadPath();
		// If bulk postdata is provided, there must also be a content type
		// specified.
		String bulkData = requestData.getBulkPostData();
		if (bulkData != null && bulkData.length() > 0) {
			String contentType = requestData.getContentType();
			if (contentType == null || contentType.length() == 0) {
				showErrorMessage("Must specify a content type if bulk post data is provided");
				valid = false;
			}
		}
		int numInputs = 0;
		numInputs += ((bulkData != null && bulkData.length() > 0) ? 1 : 0);
		numInputs += ((getRequestPostDataFields().size() > 0) ? 1 : 0);
		numInputs += ((fileUploadPath != null && fileUploadPath.length() > 0) ? 1
				: 0);
		if (numInputs > 1) {
			showErrorMessage("Must provide only one of bulk post data, post data fields, or file upload path");
			valid = false;
		}

		if ("POST".equals(verb) || "PUT".equals(verb)) {
			if ((bulkData == null || bulkData.length() == 0)
					&& (getRequestPostDataFields().size() == 0)
					&& (fileUploadPath == null || fileUploadPath.length() == 0)) {
				showErrorMessage("Using verb POST or PUT without data being posted");
				valid = false;
			}
		}

		return valid;
	}

	public String getFileUploadPath() {
		return requestData.getFileUploadPath();
	}

}