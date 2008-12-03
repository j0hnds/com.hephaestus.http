package com.hephaestus.http.views.ui;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPartSite;

import com.hephaestus.http.Messages;
import com.hephaestus.http.actions.CopyResponseDataAction;
import com.hephaestus.http.actions.FormatXMLAction;

/**
 * This control encapsulates the results from an Http request.
 * 
 * @author Dave Sieh
 */
public class ResponseData extends Composite implements ResponseDataAccess {

	// The table for the result headers
	private Table tblResultHeaders;
	// The text field for the response data
	private Text tfResultData;
	// The action to format the results as XML
	private Action formatXMLAction;
	private Action copyResponseData;

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
		
		makeActions();
	}

	/**
	 * Constructs the actions we need for this part of the view.
	 */
	private void makeActions() {
		formatXMLAction = new FormatXMLAction(this);
		copyResponseData = new CopyResponseDataAction(this);
	}
	
	/**
	 * Hooks up a context menu to the two editable tables.
	 */
	public final void hookContextMenu(IWorkbenchPartSite site) {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ResponseData.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(tfResultData);
		tfResultData.setMenu(menu);

	}
	

	protected void fillContextMenu(IMenuManager manager) {
		manager.add(formatXMLAction);
		manager.add(copyResponseData);
	}

	/**
	 * Creates the result data for http headers.
	 * 
	 * @param tabs
	 *            the tab folder
	 */
	private void createResultHttpHeaders(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText(Messages.getString("ResponseData.HTTPHeadersTabText")); //$NON-NLS-1$

		// Set up a table to enter the headers
		tblResultHeaders = new Table(tabs, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tblResultHeaders.setLinesVisible(true);
		tblResultHeaders.setHeaderVisible(true);

		// Set up the columns
		TableColumn col = new TableColumn(tblResultHeaders, SWT.NONE);
		col.setText(Messages.getString("ResponseData.HTTPHeadersHeaderColumnText")); //$NON-NLS-1$
		col.setWidth(100);

		col = new TableColumn(tblResultHeaders, SWT.NONE);
		col.setText(Messages.getString("ResponseData.HTTPHeadersValueColumnText")); //$NON-NLS-1$
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
		item.setText(Messages.getString("ResponseData.DataTabText")); //$NON-NLS-1$

		tfResultData = new Text(tabs, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		tfResultData.setFont(new Font(tfResultData.getDisplay(), "Courier", 10, 0)); //$NON-NLS-1$

		item.setControl(tfResultData);
	}

	public String getResponseData() {
		return tfResultData.getText();
	}

}
