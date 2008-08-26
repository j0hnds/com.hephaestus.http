package com.hephaestus.http.views;

import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;

import com.hephaestus.http.Activator;
import com.hephaestus.http.Messages;
import com.hephaestus.http.actions.ExportHttpRequestDataAction;
import com.hephaestus.http.actions.ImportHttpRequestDataAction;
import com.hephaestus.http.actions.InvokeURLAction;
import com.hephaestus.http.preferences.PreferenceConstants;
import com.hephaestus.http.views.ui.RequestData;
import com.hephaestus.http.views.ui.ResponseData;
import com.hephaestus.http.views.ui.URLFields;

public class HTTPView extends ViewPart implements HTTPViewData,
		IPropertyChangeListener {

	// Action to invoke the URL
	private Action invokeURLAction;

	// Action to export the URL data
	private Action exportURLAction;

	// Action to import the URL data
	private Action importURLAction;

	// The URL formulation control
	private URLFields cmpURLFields;

	// The text field for the response status.
	private Text tfStatus;

	// The request data control
	private RequestData requestData;

	// The response data control
	private ResponseData responseData;

	// Sash
	private Sash sash;

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
		final Composite theParent = parent;
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);

		// Create a composite for the entry fields
		Composite entry = new Composite(parent, SWT.NONE);
		createEntryFields(entry);

		sash = new Sash(parent, SWT.VERTICAL | SWT.BORDER);

		// Create a composite for the result fields
		Composite result = new Composite(parent, SWT.NONE);
		createResultFields(result);

		// Set up the form attachments
		FormData fd = new FormData();
		fd.left = new FormAttachment(0, 0);
		fd.right = new FormAttachment(sash, 0);
		fd.top = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, 0);
		entry.setLayoutData(fd);

		final int limit = 100, percent = 60;
		final FormData sashData = new FormData();
		sashData.left = new FormAttachment(percent, 0);
		sashData.top = new FormAttachment(0, 0);
		sashData.bottom = new FormAttachment(100, 0);
		sash.setLayoutData(sashData);
		sash.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				Rectangle sashRect = sash.getBounds();
				Rectangle shellRect = theParent.getClientArea();
				int right = shellRect.width - sashRect.width - limit;
				event.x = Math.max(Math.min(event.x, right), limit);
				if (event.x != sashRect.x) {
					sashData.left = new FormAttachment(0, event.x);
					theParent.layout();
				}
			}

		});

		fd = new FormData();
		fd.left = new FormAttachment(sash, 0);
		fd.right = new FormAttachment(100, 0);
		fd.top = new FormAttachment(0, 0);
		fd.bottom = new FormAttachment(100, 0);
		result.setLayoutData(fd);

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(cmpURLFields,
				"com.hephaestus.http.url"); //$NON-NLS-1$
		PlatformUI.getWorkbench().getHelpSystem().setHelp(entry,
				"com.hephaestus.http.entry"); //$NON-NLS-1$
		PlatformUI.getWorkbench().getHelpSystem().setHelp(result,
				"com.hephaestus.http.result"); //$NON-NLS-1$
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
		lblStatusCode.setText(Messages.getString("HTTPView.StatusLabel")); //$NON-NLS-1$
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
	 * @param entry
	 *            the parent
	 */
	private void createEntryFields(Composite entry) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		entry.setLayout(layout);

		Label lblURL = new Label(entry, SWT.NONE);
		lblURL.setText(Messages.getString("HTTPView.URLLabel")); //$NON-NLS-1$
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
		manager.add(importURLAction);
		manager.add(exportURLAction);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(invokeURLAction);
		manager.add(new Separator());
		manager.add(importURLAction);
		manager.add(exportURLAction);
	}

	private void makeActions() {
		invokeURLAction = new InvokeURLAction(this);
		importURLAction = new ImportHttpRequestDataAction(this);
		exportURLAction = new ExportHttpRequestDataAction(this);
	}

	public void showErrorMessage(String message) {
		MessageDialog.openError(cmpURLFields.getShell(), Messages
				.getString("HTTPView.ErrorDialogTitle"), message); //$NON-NLS-1$
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
			showErrorMessage(Messages
					.getString("HTTPView.MissingProtocolError")); //$NON-NLS-1$
			valid = false;
		}

		// Make sure there is a host:port selected
		String hostPort = cmpURLFields.getHostPort();
		if (hostPort == null || hostPort.length() == 0) {
			showErrorMessage(Messages
					.getString("HTTPView.MissingHostPortError")); //$NON-NLS-1$
			valid = false;
		}

		// Make sure there is a verb provided
		String verb = cmpURLFields.getVerb();
		if (verb == null || verb.length() == 0) {
			showErrorMessage(Messages.getString("HTTPView.MissingVerbError")); //$NON-NLS-1$
			valid = false;
		}

		String fileUploadPath = getFileUploadPath();
		// If bulk postdata is provided, there must also be a content type
		// specified.
		String bulkData = requestData.getBulkPostData();
		if (bulkData != null && bulkData.length() > 0) {
			String contentType = requestData.getContentType();
			if (contentType == null || contentType.length() == 0) {
				showErrorMessage(Messages
						.getString("HTTPView.MissingContentTypeError")); //$NON-NLS-1$
				valid = false;
			}
		}
		int numInputs = 0;
		numInputs += ((bulkData != null && bulkData.length() > 0) ? 1 : 0);
		numInputs += ((getRequestPostDataFields().size() > 0) ? 1 : 0);
		numInputs += ((fileUploadPath != null && fileUploadPath.length() > 0) ? 1
				: 0);
		if (numInputs > 1) {
			showErrorMessage(Messages
					.getString("HTTPView.TooManyDataTypesProvidedError")); //$NON-NLS-1$
			valid = false;
		}

		if ("POST".equals(verb) || "PUT".equals(verb)) { //$NON-NLS-1$ //$NON-NLS-2$
			if ((bulkData == null || bulkData.length() == 0)
					&& (getRequestPostDataFields().size() == 0)
					&& (fileUploadPath == null || fileUploadPath.length() == 0)) {
				showErrorMessage(Messages
						.getString("HTTPView.POSTPUTNoDataError")); //$NON-NLS-1$
				valid = false;
			}
		}

		return valid;
	}

	public String getFileUploadPath() {
		return requestData.getFileUploadPath();
	}

	public String getHostPort() {
		return cmpURLFields.getHostPort();
	}

	public String getProtocol() {
		return cmpURLFields.getProtocol();
	}

	public String getURI() {
		return cmpURLFields.getURI();
	}

	public void setBulkPostData(String postData) {
		requestData.setBulkPostData(postData);
	}

	public void setContentType(String contentType) {
		requestData.setContentType(contentType);
	}

	public void setFileUploadPath(String path) {
		requestData.setFileUploadPath(path);
	}

	public void setHostPort(String hostPort) {
		cmpURLFields.setHostPort(hostPort);
	}

	public void setProtocol(String protocol) {
		cmpURLFields.setProtocol(protocol);
	}

	public void setRequestHttpHeaders(Map<String, String> headers) {
		requestData.setRequestHttpHeaders(headers);
	}

	public void setRequestPostDataFields(Map<String, String> postData) {
		requestData.setRequestPostDataFields(postData);
	}

	public void setURI(String uri) {
		cmpURLFields.setURI(uri);
	}

	public void setVerb(String verb) {
		cmpURLFields.setVerb(verb);
	}

	public void clearInputs() {
		requestData.clearInputs();
	}
}