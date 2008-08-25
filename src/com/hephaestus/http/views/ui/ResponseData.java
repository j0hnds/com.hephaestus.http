package com.hephaestus.http.views.ui;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * This control encapsulates the results from an Http request.
 * 
 * @author Dave Sieh
 */
public class ResponseData extends Composite {

	// The table for the result headers
	private Table tblResultHeaders;
	// The text field for the response data
	private Text tfResultData;

	/**
	 * Constructs a new ResponseData control
	 * 
	 * @param parent the parent
	 * @param style the style
	 */
	public ResponseData(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);

		createControls();
	}

	public final void setResponseData(String data) {
		tfResultData.setText(data);
	}

	public final void setResponseHttpHeaders(Map<String, String> headers) {
		tblResultHeaders.removeAll();

		for (String header : headers.keySet()) {
			TableItem ti = new TableItem(tblResultHeaders, SWT.NONE);
			ti.setText(0, header);
			ti.setText(1, headers.get(header));
		}
	}

	private void createControls() {
		TabFolder tabs = new TabFolder(this, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 100;
		tabs.setLayoutData(gd);

		createResultData(tabs);

		createResultHttpHeaders(tabs);
	}

	/**
	 * Creates the result data for http headers.
	 * 
	 * @param tabs
	 *            the tab folder
	 */
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

	/**
	 * More result fields.
	 * 
	 * @param tabs
	 *            the parent for the controls.
	 */
	private void createResultData(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("Data");

		tfResultData = new Text(tabs, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);

		item.setControl(tfResultData);
	}

}
