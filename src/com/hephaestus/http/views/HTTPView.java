package com.hephaestus.http.views;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

public class HTTPView extends ViewPart {
	private static final String[] PROTOCOLS = { "http", "https" };
	private static final String[] HOST_PORTS = { "localhost:8080", "ncddev01.1dc.com:21200" };
	private Action insertNewRequestHeaderAction;
	// private Action action2;
	// private Action doubleClickAction;
	private Combo cbProtocol;
	private Combo cbHostPort;
	private Text tfURI;
	private Table tblRequestHeaders;
	private Table tblRequestPostData;
	private Text tfBulkPostData;
	private Text tfFileUploadPath;
	private Text tfStatus;
	private Text tfResultData;
	private Table tblResultHeaders;
	private TableViewer tvRequestHeaders;

	/**
	 * The constructor.
	 */
	public HTTPView() {
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
		// hookDoubleClickAction();
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
		
		tfResultData = new Text(tabs, SWT.BORDER | SWT.MULTI);
		
		item.setControl(tfResultData);
	}

	private void createResultHttpHeaders(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("HTTP Headers");
		
		// Set up a table to enter the headers
		tblResultHeaders = new Table(tabs, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
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
		
		tfFileUploadPath = new Text(fields, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tfFileUploadPath.setLayoutData(gd);
		
		Button btn = new Button(fields, SWT.PUSH);
		btn.setText("...");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		btn.setLayoutData(gd);
		
		item.setControl(fields);
	}

	private void createRequestBulkPostData(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("Bulk POST Data");
		
		tfBulkPostData = new Text(tabs, SWT.BORDER | SWT.MULTI);
		
		item.setControl(tfBulkPostData);
	}

	private void createRequestPostData(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("POST Data");
		
		// Set up a table to enter the headers
		tblRequestPostData = new Table(tabs, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
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
	}

	private void createRequestHttpHeaders(TabFolder tabs) {
		TabItem item = new TabItem(tabs, SWT.NONE);
		item.setText("HTTP Headers");
		
		// Set up a table to enter the headers
		tblRequestHeaders = new Table(tabs, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION );
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
		
		// Create a cell cursor to navigate/edit the table
		createTableEditor(tblRequestHeaders);

	}

	private void createTableEditor(final Table tbl) {
		final TableCursor cursor = new TableCursor(tbl, SWT.NONE);
		// create an editor to edit the cell when the user hits "ENTER" 
		// while over a cell in the table
		final ControlEditor editor = new ControlEditor(cursor);
		editor.grabHorizontal = true;
		editor.grabVertical = true;
		cursor.addSelectionListener(new SelectionAdapter() {
			// when the TableEditor is over a cell, select the corresponding row in 
			// the table
			public void widgetSelected(SelectionEvent e) {
				tbl.setSelection(new TableItem[] { cursor.getRow()});
			}
			// when the user hits "ENTER" in the TableCursor, pop up a text editor so that 
			// they can change the text of the cell
			public void widgetDefaultSelected(SelectionEvent e) {
				final Text text = new Text(cursor, SWT.NONE);
				TableItem row = cursor.getRow();
				int column = cursor.getColumn();
				text.setText(row.getText(column));
				text.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						// close the text editor and copy the data over 
						// when the user hits "ENTER"
						if (e.character == SWT.CR) {
							TableItem row = cursor.getRow();
							int column = cursor.getColumn();
							row.setText(column, text.getText());
							text.dispose();
						}
						// close the text editor when the user hits "ESC"
						if (e.character == SWT.ESC) {
							text.dispose();
						}
					}
				});
				// close the text editor when the user tabs away
				text.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						text.dispose();
					}
				});
				editor.setEditor(text);
				text.setFocus();
			}
		});
		// Hide the TableCursor when the user hits the "CTRL" or "SHIFT" key.
		// This alows the user to select multiple items in the table.
		cursor.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.CTRL
					|| e.keyCode == SWT.SHIFT
					|| (e.stateMask & SWT.CONTROL) != 0
					|| (e.stateMask & SWT.SHIFT) != 0) {
					cursor.setVisible(false);
				}
			}
		});
		// When the user double clicks in the TableCursor, pop up a text editor so that 
		// they can change the text of the cell
		cursor.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				final Text text = new Text(cursor, SWT.NONE);
				TableItem row = cursor.getRow();
				int column = cursor.getColumn();
				text.setText(row.getText(column));
				text.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						// close the text editor and copy the data over 
						// when the user hits "ENTER"
						if (e.character == SWT.CR) {
							TableItem row = cursor.getRow();
							int column = cursor.getColumn();
							row.setText(column, text.getText());
							text.dispose();
						}
						// close the text editor when the user hits "ESC"
						if (e.character == SWT.ESC) {
							text.dispose();
						}
					}
				});
				// close the text editor when the user clicks away
				text.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent e) {
						text.dispose();
					}
				});
				editor.setEditor(text);
				text.setFocus();
			}
		});
		
		// Show the TableCursor when the user releases the "SHIFT" or "CTRL" key.
		// This signals the end of the multiple selection task.
		tbl.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CONTROL && (e.stateMask & SWT.SHIFT) != 0)
					return;
				if (e.keyCode == SWT.SHIFT && (e.stateMask & SWT.CONTROL) != 0)
					return;
				if (e.keyCode != SWT.CONTROL
					&& (e.stateMask & SWT.CONTROL) != 0)
					return;
				if (e.keyCode != SWT.SHIFT && (e.stateMask & SWT.SHIFT) != 0)
					return;

				TableItem[] selection = tbl.getSelection();
				TableItem row = (selection.length == 0) ? tbl.getItem(tbl.getTopIndex()) : selection[0];
				tbl.showItem(row);
				cursor.setSelection(row, 0);
				cursor.setVisible(true);
				cursor.setFocus();
			}
		});
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
		layout.wrap = false;
		fields.setLayout(layout);
		
		// Drop-down for protocols
		cbProtocol = new Combo(fields, SWT.BORDER | SWT.READ_ONLY);
		cbProtocol.setItems(PROTOCOLS);
		
		Label lblFirstDelimiter = new Label(fields, SWT.NONE);
		lblFirstDelimiter.setText("://");
		
		// Drop-down for host/ports
		cbHostPort = new Combo(fields, SWT.BORDER | SWT.READ_ONLY);
		cbHostPort.setItems(HOST_PORTS);
		
		Label lblSecondDelimiter = new Label(fields, SWT.NONE);
		lblSecondDelimiter.setText("/");
		
		tfURI = new Text(fields, SWT.BORDER | SWT.SINGLE);
		RowData rd = new RowData();
		rd.width = 200;
		tfURI.setLayoutData(rd);
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
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		// manager.add(insertNewRequestHeaderAction);
		manager.add(new Separator());
		// manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		 manager.add(insertNewRequestHeaderAction);
		// manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		// manager.add(insertNewRequestHeaderAction);
		// manager.add(action2);
	}

	private void makeActions() {
		 insertNewRequestHeaderAction = new Action() {
			public void run() {
				showMessage("Insert New Header action executed");
				insertNewHeaderAction();
			}
		};
		insertNewRequestHeaderAction.setText("Insert");
		insertNewRequestHeaderAction.setToolTipText("Insert New Header");
		insertNewRequestHeaderAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		//		
		// action2 = new Action() {
		// public void run() {
		// showMessage("Action 2 executed");
		// }
		// };
		// action2.setText("Action 2");
		// action2.setToolTipText("Action 2 tooltip");
		//action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
		// .
		// getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		// doubleClickAction = new Action() {
		// public void run() {
		// ISelection selection = viewer.getSelection();
		// Object obj = ((IStructuredSelection)selection).getFirstElement();
		// showMessage("Double-click detected on "+obj.toString());
		// }
		// };
	}

	protected void insertNewHeaderAction() {
		TableItem ti = new TableItem(tblRequestHeaders, SWT.NONE);
		ti.setText(0, "HEADER");
		ti.setText(1, "VALUE");
	}

	// private void hookDoubleClickAction() {
	// viewer.addDoubleClickListener(new IDoubleClickListener() {
	// public void doubleClick(DoubleClickEvent event) {
	// doubleClickAction.run();
	// }
	// });
	// }
	private void showMessage(String message) {
		MessageDialog.openInformation(cbProtocol.getShell(),
				"HTTP Test", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		cbProtocol.setFocus();
	}
}