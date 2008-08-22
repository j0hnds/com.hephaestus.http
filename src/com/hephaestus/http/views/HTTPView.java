package com.hephaestus.http.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import com.hephaestus.http.Activator;
import com.hephaestus.http.actions.DeleteTableRowAction;
import com.hephaestus.http.actions.InsertTableRowAction;
import com.hephaestus.http.actions.InvokeURLAction;
import com.hephaestus.http.preferences.PreferenceConstants;

public class HTTPView extends ViewPart implements HTTPViewData,
		IPropertyChangeListener {
	private static final String[] PROTOCOLS = { "http", "https" };
	private static final String[] VERBS = { "GET", "PUT", "POST", "DELETE" };
	private static final String[] COLUMN_NAMES = { "NAME", "VALUE" };
	private Action insertNewRequestHeaderAction;
	private Action insertNewRequestPostDataAction;
	private Action deleteRequestHeaderAction;
	private Action deleteRequestPostDataAction;
	private Action invokeURLAction;
	private Combo cbProtocol;
	private Combo cbHostPort;
	private Combo cbURI;
	private Combo cbVerbs;
	private Table tblRequestHeaders;
	private Table tblRequestPostData;
	private Text tfBulkPostData;
	private Text tfFileUploadPath;
	private Text tfStatus;
	private Text tfResultData;
	private Text tfContentType;
	private Table tblResultHeaders;
	private TableViewer tvRequestHeaders;
	private TableViewer tvRequestPostData;
	private NameValuePairs nvpRequestPostData;
	private NameValuePairs nvpRequestHeaders;

	/**
	 * The constructor.
	 */
	public HTTPView() {
		Activator.getDefault().getPluginPreferences()
				.addPropertyChangeListener(this);
		
		nvpRequestPostData = new NameValuePairs();
		nvpRequestHeaders = new NameValuePairs();
	}

	@Override
	public void dispose() {
		Activator.getDefault().getPluginPreferences()
				.removePropertyChangeListener(this);
		super.dispose();
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(cbProtocol,
				"com.hephaestus.http.viewer");
		makeActions();
		hookContextMenu();
		contributeToActionBars();
	}

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

		createResultDataFields(result);
	}

	private void createResultDataFields(Composite result) {
		TabFolder tabs = new TabFolder(result, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		gd.heightHint = 100;
		tabs.setLayoutData(gd);

		createResultHttpHeaders(tabs);

		createResultData(tabs);
	}

	private void createResultData(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("Data");

		tfResultData = new Text(tabs, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		item.setControl(tfResultData);
	}

	private void createResultHttpHeaders(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("HTTP Headers");

		// Set up a table to enter the headers
		tblResultHeaders = new Table(tabs, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tblResultHeaders.setLinesVisible(true);
		tblResultHeaders.setHeaderVisible(true);

		// Set up the columns
		TableColumn col = new TableColumn(tblResultHeaders, SWT.NONE);
		col.setText("Header");
		col.setWidth(100);

		col = new TableColumn(tblResultHeaders, SWT.NONE);
		col.setText("Value");
		col.setWidth(100);
		item.setControl(tblResultHeaders);

	}

	private void createEntryFields(Composite entry) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		entry.setLayout(layout);

		Label lblURL = new Label(entry, SWT.NONE);
		lblURL.setText("URL:");
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblURL.setLayoutData(gd);

		createURLFields(entry);

		createInputDataFields(entry);
	}

	private void createInputDataFields(Composite entry) {
		TabFolder tabs = new TabFolder(entry, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		gd.heightHint = 100;
		tabs.setLayoutData(gd);

		// Create a tab item for Http Headers
		createRequestHttpHeaders(tabs);

		// Create a tab item for normal POST Data (name/values)
		createRequestPostData(tabs);

		// Create a tab item for bulk POST data
		createRequestBulkPostData(tabs);

		// Create a tab item for File Upload
		createRequestFileUpload(tabs);
	}

	private void createRequestFileUpload(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("File Upload");

		Composite fields = new Composite(tabs, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		fields.setLayout(layout);

		tfFileUploadPath = new Text(fields, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tfFileUploadPath.setLayoutData(gd);

		Button btn = new Button(fields, SWT.PUSH);
		btn.setText("...");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		btn.setLayoutData(gd);
		btn.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);

			}

			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(cbHostPort.getShell(),
						SWT.OPEN);
				dialog.setFilterNames(new String[] { "All Files (*.*)" });
				dialog.setFilterExtensions(new String[] { "*.*" });
				dialog.setFilterPath("C:/");
				String path = dialog.open();
				if (path != null) {
					tfFileUploadPath.setText(path);
				}
			}

		});

		item.setControl(fields);
	}

	private void createRequestBulkPostData(TabFolder tabs) {
		Composite cmp = new Composite(tabs, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		cmp.setLayout(layout);
		
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("Bulk POST Data");

		Label lblContentType = new Label(cmp, SWT.NONE);
		lblContentType.setText("Content Type:");
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblContentType.setLayoutData(gd);
		
		tfContentType = new Text(cmp, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tfContentType.setLayoutData(gd);
		
		tfBulkPostData = new Text(cmp, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		tfBulkPostData.setLayoutData(gd);

		item.setControl(cmp);
	}

	private void createRequestPostData(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("POST Data");

		// Set up a table to enter the headers
		tblRequestPostData = new Table(tabs, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
				SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		tblRequestPostData.setLinesVisible(true);
		tblRequestPostData.setHeaderVisible(true);

		// Set up the columns
		TableColumn col = new TableColumn(tblRequestPostData, SWT.NONE);
		col.setText("Field");
		col.setWidth(100);

		col = new TableColumn(tblRequestPostData, SWT.NONE);
		col.setText("Value");
		col.setWidth(100);

		item.setControl(tblRequestPostData);

		tvRequestPostData = new TableViewer(tblRequestPostData);
		tvRequestPostData.setColumnProperties(COLUMN_NAMES);
		// Create the Cell Editors
		CellEditor[] editors = new CellEditor[2];
		
		TextCellEditor fieldEditor = new TextCellEditor(tblRequestPostData);
		editors[0] = fieldEditor;
		
		TextCellEditor valueEditor = new TextCellEditor(tblRequestPostData);
		editors[1] = valueEditor;
		
		tvRequestPostData.setCellEditors(editors);

		tvRequestPostData.setCellModifier(new NameValuePairCellModifier(tvRequestPostData, nvpRequestPostData));
		tvRequestPostData.setLabelProvider(new NameValuePairLabelProvider());
		tvRequestPostData.setContentProvider(new NameValuePairContentProvider(nvpRequestPostData, tvRequestPostData));
		tvRequestPostData.setInput(nvpRequestPostData);
	}

	private void createRequestHttpHeaders(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("HTTP Headers");

		// Set up a table to enter the headers
		tblRequestHeaders = new Table(tabs, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | 
				SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		tblRequestHeaders.setLinesVisible(true);
		tblRequestHeaders.setHeaderVisible(true);

		// Set up the columns
		TableColumn col = new TableColumn(tblRequestHeaders, SWT.NONE);
		col.setText("Header");
		col.setWidth(100);

		col = new TableColumn(tblRequestHeaders, SWT.NONE);
		col.setText("Value");
		col.setWidth(100);
		item.setControl(tblRequestHeaders);

		tvRequestHeaders = new TableViewer(tblRequestHeaders);
		tvRequestHeaders.setLabelProvider(new NameValuePairLabelProvider());
		tvRequestHeaders.setColumnProperties(COLUMN_NAMES);
		
		// Create the Cell Editors
		CellEditor[] editors = new CellEditor[2];
		
		TextCellEditor fieldEditor = new TextCellEditor(tblRequestHeaders);
		editors[0] = fieldEditor;
		
		TextCellEditor valueEditor = new TextCellEditor(tblRequestHeaders);
		editors[1] = valueEditor;
		
		tvRequestHeaders.setCellEditors(editors);
		tvRequestHeaders.setCellModifier(new NameValuePairCellModifier(tvRequestHeaders, nvpRequestHeaders));
		tvRequestHeaders.setContentProvider(new NameValuePairContentProvider(nvpRequestHeaders, tvRequestHeaders));
		tvRequestHeaders.setInput(nvpRequestHeaders);
	}

	private void createURLFields(Composite entry) {
		Composite fields = new Composite(entry, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		fields.setLayoutData(gd);
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.center = true;
		layout.fill = true;
		layout.justify = false;
		layout.pack = true;
		layout.spacing = 5;
		layout.wrap = true;
		fields.setLayout(layout);

		// Drop-down for verbs
		cbVerbs = new Combo(fields, SWT.BORDER | SWT.READ_ONLY);
		cbVerbs.setItems(VERBS);
		cbVerbs.select(0);

		// Drop-down for protocols
		cbProtocol = new Combo(fields, SWT.BORDER | SWT.READ_ONLY);
		cbProtocol.setItems(PROTOCOLS);
		cbProtocol.select(0);

		Label lblFirstDelimiter = new Label(fields, SWT.NONE);
		lblFirstDelimiter.setText("://");

		// Drop-down for host/ports
		cbHostPort = new Combo(fields, SWT.BORDER | SWT.READ_ONLY);
		RowData rd = new RowData();
		rd.width = 200;
		loadHostPortComboBox();

		Label lblSecondDelimiter = new Label(fields, SWT.NONE);
		lblSecondDelimiter.setText("/");

		cbURI = new Combo(fields, SWT.BORDER);
		cbURI.setLayoutData(rd);
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				HTTPView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tblRequestHeaders);
		tblRequestHeaders.setMenu(menu);
		getSite().registerContextMenu(menuMgr, tvRequestHeaders);

		menu = menuMgr.createContextMenu(tblRequestPostData);
		tblRequestPostData.setMenu(menu);
		getSite().registerContextMenu(menuMgr, tvRequestPostData);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(invokeURLAction);
		manager.add(new Separator());
		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		if (tblRequestHeaders.isFocusControl()) {
			manager.add(insertNewRequestHeaderAction);
			manager.add(deleteRequestHeaderAction);
		}
		else if (tblRequestPostData.isFocusControl()) {
			manager.add(insertNewRequestPostDataAction);
			manager.add(deleteRequestPostDataAction);
		}
		// manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(invokeURLAction);
	}

	private void makeActions() {
		insertNewRequestHeaderAction = new InsertTableRowAction(
				nvpRequestHeaders);
		insertNewRequestPostDataAction = new InsertTableRowAction(
				nvpRequestPostData);
		deleteRequestHeaderAction = new DeleteTableRowAction(tvRequestHeaders);
		deleteRequestPostDataAction = new DeleteTableRowAction(
				tvRequestPostData);
		invokeURLAction = new InvokeURLAction(this);
	}

	protected void insertNewHeaderAction() {
		TableItem ti = new TableItem(tblRequestHeaders, SWT.NONE);
		ti.setText(0, "HEADER");
		ti.setText(1, "VALUE");
	}

	protected void insertNewPostDataAction() {
		TableItem ti = new TableItem(tblRequestPostData, SWT.NONE);
		ti.setText(0, "FIELD");
		ti.setText(1, "VALUE");
	}

	public void showErrorMessage(String message) {
		MessageDialog.openError(cbProtocol.getShell(), "HTTP Test", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		cbURI.setFocus();
	}

	// Implementation of HTTPViewData
	public String getBulkPostData() {
		return tfBulkPostData.getText();
	}

	public Map<String, String> getRequestHttpHeaders() {
		Map<String, String> headers = new HashMap<String, String>();

		for (NameValuePair nvp : nvpRequestHeaders.getNameValuePairs()) {
			headers.put(nvp.getName(), nvp.getValue());
		}

		return headers;
	}

	public Map<String, String> getRequestPostDataFields() {
		Map<String, String> headers = new HashMap<String, String>();

		for (NameValuePair nvp : nvpRequestPostData.getNameValuePairs()) {
			headers.put(nvp.getName(), nvp.getValue());
		}
		
		return headers;
	}

	public String getURL() {
		StringBuilder sb = new StringBuilder();
		sb.append(cbProtocol.getText());
		sb.append("://");
		sb.append(cbHostPort.getText());
		sb.append("/");
		sb.append(cbURI.getText());

		return sb.toString();
	}

	public void setResponseData(String data) {
		tfResultData.setText(data);
	}

	public void setResponseHttpHeaders(Map<String, String> headers) {
		tblResultHeaders.removeAll();

		for (String header : headers.keySet()) {
			TableItem ti = new TableItem(tblResultHeaders, SWT.NONE);
			ti.setText(0, header);
			ti.setText(1, headers.get(header));
		}
	}

	public void setStatus(String status) {
		tfStatus.setText(status);
		// If we got here, we didn't get an error, so save off the 
		// value of the URI we used in the combo box.
		String uri = cbURI.getText();
		if (cbURI.indexOf(uri) < 0) {
			cbURI.add(uri);
		}
	}

	public String getVerb() {
		return cbVerbs.getText();
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (PreferenceConstants.P_HOST_PORTS.equals(event.getProperty())) {
			resetHostPortComboBox();
		}
	}

	private void resetHostPortComboBox() {
		cbHostPort.removeAll();
		loadHostPortComboBox();
	}

	private void loadHostPortComboBox() {
		Preferences store = Activator.getDefault().getPluginPreferences();

		String hpString = store.getString(PreferenceConstants.P_HOST_PORTS);
		cbHostPort.setItems(hpString.split("\\|"));
		if (cbHostPort.getItemCount() > 0) {
			cbHostPort.select(0);
		}
	}

	public String getContentType() {
		return tfContentType.getText();
	}

	public boolean validInputs() {
		boolean valid = true;
		
		// Make sure there is a protocol selected
		String protocol = cbProtocol.getText();
		if (protocol == null || protocol.length() == 0) {
			showErrorMessage("Must select a protocol");
			valid = false;
		}
		
		// Make sure there is a host:port selected
		String hostPort = cbHostPort.getText();
		if (hostPort == null || hostPort.length() == 0) {
			showErrorMessage("Must select a host:port");
			valid = false;
		}
		
		// Make sure there is a verb provided
		String verb = cbVerbs.getText();
		if (verb == null || verb.length() == 0) {
			showErrorMessage("Must select a verb");
			valid = false;
		}
		
		String fileUploadPath = getFileUploadPath();
		// If bulk postdata is provided, there must also be a content type
		// specified.
		String bulkData = tfBulkPostData.getText();
		if (bulkData != null && bulkData.length() > 0) {
			String contentType = tfContentType.getText();
			if (contentType == null || contentType.length() == 0) {
				showErrorMessage("Must specify a content type if bulk post data is provided");
				valid = false;
			}
		}
		int numInputs = 0;
		numInputs += ((bulkData != null && bulkData.length() > 0) ? 1 : 0);
		numInputs += ((getRequestPostDataFields().size() > 0) ? 1 : 0);
		numInputs += ((fileUploadPath != null && fileUploadPath.length() > 0) ? 1 : 0);
		if (numInputs > 1) {
			showErrorMessage("Must provide only one of bulk post data, post data fields, or file upload path");
			valid = false;
		}
		
		if ("POST".equals(verb) || "PUT".equals(verb)) {
			if ((bulkData == null || bulkData.length() == 0) && (getRequestPostDataFields().size() == 0) && (fileUploadPath == null || fileUploadPath.length() == 0))
			{
				showErrorMessage("Using verb POST or PUT without data being posted");
				valid = false;
			}
		}
		
		return valid;
	}

	public String getFileUploadPath() {
		return tfFileUploadPath.getText();
	}

}