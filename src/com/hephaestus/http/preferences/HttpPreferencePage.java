package com.hephaestus.http.preferences;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.hephaestus.http.Activator;

/**
 * A PreferencePage implementation for HTTP preferences.
 * 
 * @author Dave Sieh
 */
public class HttpPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private static final Pattern RE_HOST_PORT = Pattern
			.compile("^[^:]+:[0-9]+$");
	private Table tblHostPorts;
	private Text tfProxy;

	/**
	 * Constructs a new HttpPreferencePage.
	 */
	public HttpPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("HTTP Preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		Composite parent = new Composite(getFieldEditorParent(), SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblHostPorts = new Label(parent, SWT.NONE);
		lblHostPorts.setText("Registered Servers:");
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.horizontalSpan = 2;
		lblHostPorts.setLayoutData(gd);

		tblHostPorts = new Table(parent, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tblHostPorts.setHeaderVisible(true);
		tblHostPorts.setLinesVisible(true);
		gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.heightHint = 200;
		tblHostPorts.setLayoutData(gd);

		// Set up the columns
		TableColumn col = new TableColumn(tblHostPorts, SWT.NONE);
		col.setText("Host");
		col.setWidth(100);

		col = new TableColumn(tblHostPorts, SWT.NONE);
		col.setText("Port");
		col.setWidth(100);

		loadHostPortsTable();

		// Now, set up the buttons for working with the data
		Composite buttons = new Composite(parent, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.TOP, true, false);
		buttons.setLayoutData(gd);
		RowLayout btnLayout = new RowLayout(SWT.VERTICAL);
		buttons.setLayout(btnLayout);

		Button btnAdd = new Button(buttons, SWT.PUSH);
		btnAdd.setText("Add");
		btnAdd.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				addHostPortRow();
			}

		});

		Button btnDel = new Button(buttons, SWT.PUSH);
		btnDel.setText("Delete");
		btnDel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				deleteSelectedHostPortRows();
			}

		});

		Label lblProxy = new Label(parent, SWT.NONE);
		lblProxy.setText("Proxy:");
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblProxy.setLayoutData(gd);

		tfProxy = new Text(parent, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tfProxy.setLayoutData(gd);

		loadProxy();

		parent.pack();

	}

	/**
	 * Loads the proxy information from the preferences.
	 */
	private void loadProxy() {
		IPreferenceStore store = getPreferenceStore();

		String proxy = store.getString(PreferenceConstants.P_PROXY_HOST_PORT);
		tfProxy.setText(proxy);
	}

	/**
	 * Displays a dialog to have the user specify a new host:port.
	 */
	protected void addHostPortRow() {
		InputDialog dlg = new InputDialog(getShell(), "Http Preferences",
				"Enter the desired host:port", "", new IInputValidator() {

					public String isValid(String newText) {
						String errorMessage = null;
						if (newText == null || newText.length() == 0) {
							errorMessage = "Must specify host:port";
						}

						if (!RE_HOST_PORT.matcher(newText).matches()) {
							errorMessage = "Entry must be of the form host:port";
						}
						return errorMessage;
					}

				});
		dlg.setBlockOnOpen(true);
		int status = dlg.open();
		if (status == 0) {
			String hostPort = dlg.getValue();
			String[] cmps = hostPort.split(":");
			TableItem ti = new TableItem(tblHostPorts, SWT.NONE);
			ti.setText(cmps);
		}
	}

	/**
	 * Deletes the selected rows in the host:port table.
	 */
	protected void deleteSelectedHostPortRows() {
		tblHostPorts.remove(tblHostPorts.getSelectionIndices());
	}

	/**
	 * Loads the host:port table from preferences.
	 */
	private void loadHostPortsTable() {
		IPreferenceStore store = getPreferenceStore();

		String hostPorts = store.getString(PreferenceConstants.P_HOST_PORTS);

		loadHostPorts(hostPorts);
	}

	@Override
	protected void performDefaults() {
		loadDefaultHostPorts();
		loadDefaultProxy();
		super.performDefaults();
	}

	/**
	 * Loads the default proxy information from preferences.
	 */
	private void loadDefaultProxy() {
		IPreferenceStore store = this.getPreferenceStore();
		String proxy = store
				.getDefaultString(PreferenceConstants.P_PROXY_HOST_PORT);
		tfProxy.setText(proxy);
	}

	@Override
	public boolean performOk() {
		StringBuilder sb = new StringBuilder();
		boolean firstItem = true;
		for (TableItem ti : tblHostPorts.getItems()) {
			if (!firstItem) {
				sb.append("|");
			}
			sb.append(ti.getText(0));
			sb.append(":");
			sb.append(ti.getText(1));
			firstItem = false;
		}

		IPreferenceStore store = getPreferenceStore();
		store.setValue(PreferenceConstants.P_HOST_PORTS, sb.toString());

		store
				.setValue(PreferenceConstants.P_PROXY_HOST_PORT, tfProxy
						.getText());

		return super.performOk();
	}

	/**
	 * Loads the default host:ports from preference information.
	 */
	private void loadDefaultHostPorts() {
		IPreferenceStore store = this.getPreferenceStore();
		String hp = store.getDefaultString(PreferenceConstants.P_HOST_PORTS);

		loadHostPorts(hp);
	}

	/**
	 * Loads the host:ports into the table.
	 * 
	 * @param hp
	 *            the string containing the host:ports information from
	 *            preferences.
	 */
	private void loadHostPorts(String hp) {
		tblHostPorts.removeAll();
		// Split the string on the '|' symbol
		String[] hostPorts = hp.split("\\|");
		for (String hostPort : hostPorts) {
			String[] cmps = hostPort.split(":");
			TableItem ti = new TableItem(tblHostPorts, SWT.NONE);
			ti.setText(cmps);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	public boolean isValid() {
		String proxy = tfProxy.getText();
		if (proxy != null && proxy.length() > 0) {
			if (!RE_HOST_PORT.matcher(proxy).matches()) {
				return false;
			}
		}
		return super.isValid();
	}

}