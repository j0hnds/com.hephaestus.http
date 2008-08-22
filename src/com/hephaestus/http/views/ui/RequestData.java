package com.hephaestus.http.views.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hephaestus.http.actions.DeleteAllTableRowsAction;
import com.hephaestus.http.actions.DeleteTableRowAction;
import com.hephaestus.http.actions.InsertTableRowAction;
import com.hephaestus.http.views.NameValuePair;
import com.hephaestus.http.views.NameValuePairCellModifier;
import com.hephaestus.http.views.NameValuePairContentProvider;
import com.hephaestus.http.views.NameValuePairLabelProvider;
import com.hephaestus.http.views.NameValuePairs;

/**
 * This class encapsulates the construction and management of the controls
 * used for collecting the data used for the HTTP methods. It encloses a 
 * tab folder for gathering the different types of data possible.
 * 
 * @author Dave Sieh
 */
public class RequestData extends Composite {

	// The column properties
	private static final String[] COLUMN_PROPERTIES = { "NAME", "VALUE" };

	// The text field for the file upload path
	private Text tfFileUploadPath;

	// The text field for bulk post data
	private Text tfBulkPostData;
	
	// The text field for the content type of the bulk post data
	private Text tfContentType;

	// The table for request headers
	private Table tblRequestHeaders;

	// The table for post data
	private Table tblRequestPostData;
	
	// The table viewer for the request headers
	private TableViewer tvRequestHeaders;

	// The table viewer for the request post data
	private TableViewer tvRequestPostData;

	// The model data for the request post data
	private NameValuePairs nvpRequestPostData;

	// The model data for the request headers
	private NameValuePairs nvpRequestHeaders;

	// Action to insert a new request header
	private Action insertNewRequestHeaderAction;

	// Action to insert new post data
	private Action insertNewRequestPostDataAction;

	// Action to delete selected request headers
	private Action deleteRequestHeaderAction;

	// Action to delete selected post data
	private Action deleteRequestPostDataAction;
	
	// Action to delete all request headers
	private Action deleteAllRequestHeadersAction;
	
	// Action to delete all request post data
	private Action deleteAllRequestPostDataAction;

	/**
	 * Constructs a new RequestData object.
	 * 
	 * @param parent the parent control
	 * @param style the style.
	 */
	public RequestData(Composite parent, int style) {
		super(parent, style);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);
		
		nvpRequestPostData = new NameValuePairs();
		nvpRequestHeaders = new NameValuePairs();

		createControls();
		makeActions();
	}

	/**
	 * Create the controls.
	 */
	private void createControls() {
		TabFolder tf = new TabFolder(this, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		tf.setLayoutData(gd);

		// Create a tab item for Http Headers
		createRequestHttpHeaders(tf);

		// Create a tab item for normal POST Data (name/values)
		createRequestPostData(tf);

		// Create a tab item for bulk POST data
		createRequestBulkPostData(tf);

		// Create a tab item for File Upload
		createRequestFileUpload(tf);
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
				FileDialog dialog = new FileDialog(getShell(),
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

		tfBulkPostData = new Text(cmp, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		tfBulkPostData.setLayoutData(gd);

		item.setControl(cmp);
	}
	
	private void createRequestPostData(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("POST Data");

		// Set up a table to enter the headers
		tblRequestPostData = new Table(tabs, SWT.SINGLE | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
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
		tvRequestPostData.setColumnProperties(COLUMN_PROPERTIES);
		// Create the Cell Editors
		CellEditor[] editors = new CellEditor[2];

		TextCellEditor fieldEditor = new TextCellEditor(tblRequestPostData);
		editors[0] = fieldEditor;

		TextCellEditor valueEditor = new TextCellEditor(tblRequestPostData);
		editors[1] = valueEditor;

		tvRequestPostData.setCellEditors(editors);

		tvRequestPostData.setCellModifier(new NameValuePairCellModifier(
				tvRequestPostData, nvpRequestPostData));
		tvRequestPostData.setLabelProvider(new NameValuePairLabelProvider());
		tvRequestPostData.setContentProvider(new NameValuePairContentProvider(
				nvpRequestPostData, tvRequestPostData));
		tvRequestPostData.setInput(nvpRequestPostData);
	}

	private void createRequestHttpHeaders(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("HTTP Headers");

		// Set up a table to enter the headers
		tblRequestHeaders = new Table(tabs, SWT.SINGLE | SWT.BORDER
				| SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
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
		tvRequestHeaders.setColumnProperties(COLUMN_PROPERTIES);

		// Create the Cell Editors
		CellEditor[] editors = new CellEditor[2];

		TextCellEditor fieldEditor = new TextCellEditor(tblRequestHeaders);
		editors[0] = fieldEditor;

		TextCellEditor valueEditor = new TextCellEditor(tblRequestHeaders);
		editors[1] = valueEditor;

		tvRequestHeaders.setCellEditors(editors);
		tvRequestHeaders.setCellModifier(new NameValuePairCellModifier(
				tvRequestHeaders, nvpRequestHeaders));
		tvRequestHeaders.setContentProvider(new NameValuePairContentProvider(
				nvpRequestHeaders, tvRequestHeaders));
		tvRequestHeaders.setInput(nvpRequestHeaders);
	}
	/**
	 * Hooks up a context menu to the two editable tables.
	 */
	public void hookContextMenu(IWorkbenchPartSite site) {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RequestData.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tblRequestHeaders);
		tblRequestHeaders.setMenu(menu);
		site.registerContextMenu(menuMgr, tvRequestHeaders);

		menu = menuMgr.createContextMenu(tblRequestPostData);
		tblRequestPostData.setMenu(menu);
		site.registerContextMenu(menuMgr, tvRequestPostData);
	}
	
	private void fillContextMenu(IMenuManager manager) {
		if (tblRequestHeaders.isFocusControl()) {
			manager.add(insertNewRequestHeaderAction);
			manager.add(deleteRequestHeaderAction);
			manager.add(deleteAllRequestHeadersAction);
		}
		else if (tblRequestPostData.isFocusControl()) {
			manager.add(insertNewRequestPostDataAction);
			manager.add(deleteRequestPostDataAction);
			manager.add(deleteAllRequestPostDataAction);
		}
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		insertNewRequestHeaderAction = new InsertTableRowAction(
				nvpRequestHeaders);
		insertNewRequestPostDataAction = new InsertTableRowAction(
				nvpRequestPostData);
		deleteRequestHeaderAction = new DeleteTableRowAction(tvRequestHeaders);
		deleteRequestPostDataAction = new DeleteTableRowAction(
				tvRequestPostData);
		deleteAllRequestHeadersAction = new DeleteAllTableRowsAction(tvRequestHeaders);
		deleteAllRequestPostDataAction = new DeleteAllTableRowsAction(tvRequestPostData);
	}
	
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
	
	public String getContentType() {
		return tfContentType.getText();
	}
	
	public String getFileUploadPath() {
		return tfFileUploadPath.getText();
	}
}