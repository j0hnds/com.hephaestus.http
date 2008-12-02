package com.hephaestus.http.preferences;

import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.*;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.hephaestus.http.Activator;
import com.hephaestus.http.Messages;
import com.hephaestus.http.model.NameValuePair;
import com.hephaestus.http.model.NameValuePairs;
import com.hephaestus.http.views.NameValuePairCellModifier;
import com.hephaestus.http.views.NameValuePairContentProvider;
import com.hephaestus.http.views.NameValuePairLabelProvider;

/**
 * A PreferencePage implementation for HTTP preferences.
 * 
 * @author Dave Sieh
 */
public class HttpPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	private static final Pattern RE_HOST_PORT = Pattern
			.compile("^[^:]+:[0-9]+$"); //$NON-NLS-1$

	// The column properties
	private static final String[] COLUMN_PROPERTIES = { "NAME", "VALUE" }; //$NON-NLS-1$ //$NON-NLS-2$

	private Table tblHostPorts;
	private Text tfProxy;
	private NameValuePairs nvpHostPorts;
	private TableViewer tvHostPorts;
	private Button btnStrictSSL;

	/**
	 * Constructs a new HttpPreferencePage.
	 */
	public HttpPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.getString("HttpPreferencePage.Description")); //$NON-NLS-1$
		nvpHostPorts = new NameValuePairs();
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors() {
		Composite parent = new Composite(getFieldEditorParent(), SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label lblHostPorts = new Label(parent, SWT.NONE);
		lblHostPorts.setText(Messages.getString("HttpPreferencePage.HostPortLabel")); //$NON-NLS-1$
		GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.horizontalSpan = 3;
		lblHostPorts.setLayoutData(gd);

		tblHostPorts = new Table(parent, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		tblHostPorts.setHeaderVisible(true);
		tblHostPorts.setLinesVisible(true);
		gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.heightHint = 200;
		gd.horizontalSpan = 2;
		tblHostPorts.setLayoutData(gd);

		// Set up the columns
		TableColumn col = new TableColumn(tblHostPorts, SWT.NONE);
		col.setText(Messages.getString("HttpPreferencePage.HostColumn")); //$NON-NLS-1$
		col.setWidth(100);

		col = new TableColumn(tblHostPorts, SWT.NONE);
		col.setText(Messages.getString("HttpPreferencePage.PortColumn")); //$NON-NLS-1$
		col.setWidth(100);
		
		tvHostPorts = new TableViewer(tblHostPorts);
		tvHostPorts.setLabelProvider(new NameValuePairLabelProvider());
		tvHostPorts.setColumnProperties(COLUMN_PROPERTIES);

		// Create the Cell Editors
		CellEditor[] editors = new CellEditor[2];

		TextCellEditor fieldEditor = new TextCellEditor(tblHostPorts);
		editors[0] = fieldEditor;

		TextCellEditor valueEditor = new TextCellEditor(tblHostPorts);
		editors[1] = valueEditor;
		valueEditor.setValidator(new ICellEditorValidator() {

			public String isValid(Object value) {
				String result = null;
				
				try {
					Integer.parseInt((String) value);
				} catch (NumberFormatException e) {
					result = Messages.getString("HttpPreferencePage.PortNumberError"); //$NON-NLS-1$
				}
				
				return result;
			}
			
		});

		tvHostPorts.setCellEditors(editors);
		tvHostPorts.setCellModifier(new NameValuePairCellModifier(tvHostPorts, nvpHostPorts));
		tvHostPorts.setContentProvider(new NameValuePairContentProvider(nvpHostPorts, tvHostPorts));
		tvHostPorts.setInput(nvpHostPorts);

		loadHostPortsTable();

		// Now, set up the buttons for working with the data
		Composite buttons = new Composite(parent, SWT.NONE);
		gd = new GridData(SWT.CENTER, SWT.TOP, true, false);
		buttons.setLayoutData(gd);
		RowLayout btnLayout = new RowLayout(SWT.VERTICAL);
		buttons.setLayout(btnLayout);

		Button btnAdd = new Button(buttons, SWT.PUSH);
		btnAdd.setText(Messages.getString("HttpPreferencePage.AddButtonText")); //$NON-NLS-1$
		btnAdd.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				addHostPortRow();
			}

		});

		Button btnDel = new Button(buttons, SWT.PUSH);
		btnDel.setText(Messages.getString("HttpPreferencePage.DeleteButtonText")); //$NON-NLS-1$
		btnDel.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				deleteSelectedHostPortRows();
			}

		});

		Label lblProxy = new Label(parent, SWT.NONE);
		lblProxy.setText(Messages.getString("HttpPreferencePage.ProxyLabel")); //$NON-NLS-1$
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblProxy.setLayoutData(gd);

		tfProxy = new Text(parent, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tfProxy.setLayoutData(gd);

		loadProxy();
		
		Label lblPlaceholder1 = new Label(parent, SWT.NONE);
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblPlaceholder1.setLayoutData(gd);

		Label lblStrictSSL = new Label(parent, SWT.NONE);
		lblStrictSSL.setText(Messages.getString("HttpPreferencePage.StrictSSLLabel")); //$NON-NLS-1$
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		lblStrictSSL.setLayoutData(gd);
		
		btnStrictSSL = new Button(parent, SWT.CHECK | SWT.RIGHT);
		//btnStrictSSL.setText(Messages.getString("HttpPreferencePage.StrictSSLLabel")); //$NON-NLS-1$
		gd = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		gd.horizontalSpan = 2;
		btnStrictSSL.setLayoutData(gd);
		
		loadStrictSSL();

		parent.pack();

	}

	private void loadStrictSSL() {
		IPreferenceStore store = getPreferenceStore();

		boolean strict = store.getBoolean(PreferenceConstants.P_STRICT_SSL);
		
		btnStrictSSL.setSelection(strict);
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
		InputDialog dlg = new InputDialog(getShell(), Messages.getString("HttpPreferencePage.DialogTitle"), //$NON-NLS-1$
				Messages.getString("HttpPreferencePage.DialogPrompt"), "", new IInputValidator() { //$NON-NLS-1$ //$NON-NLS-2$

					public String isValid(String newText) {
						String errorMessage = null;
						if (newText == null || newText.length() == 0) {
							errorMessage = Messages.getString("HttpPreferencePage.MissingHostPortError"); //$NON-NLS-1$
						}

						if (!RE_HOST_PORT.matcher(newText).matches()) {
							errorMessage = Messages.getString("HttpPreferencePage.InvalidHostPortError"); //$NON-NLS-1$
						}
						return errorMessage;
					}

				});
		dlg.setBlockOnOpen(true);
		int status = dlg.open();
		if (status == 0) {
			String hostPort = dlg.getValue();
			String[] cmps = hostPort.split(":"); //$NON-NLS-1$
			nvpHostPorts.addNameValuePair(new NameValuePair(cmps[0], cmps[1]));
		}
	}

	/**
	 * Deletes the selected rows in the host:port table.
	 */
	protected void deleteSelectedHostPortRows() {
		IStructuredSelection sel = (IStructuredSelection) tvHostPorts.getSelection();
		for (Object nvp : sel.toArray()) {
			nvpHostPorts.removeNameValuePair((NameValuePair) nvp);
		}
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
		loadDefaultStrictSSL();
		super.performDefaults();
	}

	private void loadDefaultStrictSSL() {
		IPreferenceStore store = getPreferenceStore();

		boolean strict = store.getDefaultBoolean(PreferenceConstants.P_STRICT_SSL);
		
		btnStrictSSL.setSelection(strict);
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
		for (NameValuePair nvp : nvpHostPorts.getNameValuePairs()) {
			if (!firstItem) {
				sb.append("|"); //$NON-NLS-1$
			}
			sb.append(nvp.getName());
			sb.append(":"); //$NON-NLS-1$
			sb.append(nvp.getValue());
			firstItem = false;
		}

		IPreferenceStore store = getPreferenceStore();
		store.setValue(PreferenceConstants.P_HOST_PORTS, sb.toString());

		store
				.setValue(PreferenceConstants.P_PROXY_HOST_PORT, tfProxy
						.getText());
		store.setValue(PreferenceConstants.P_STRICT_SSL, btnStrictSSL.getSelection());

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
		nvpHostPorts.removeAll();
		// Split the string on the '|' symbol
		String[] hostPorts = hp.split("\\|"); //$NON-NLS-1$
		for (String hostPort : hostPorts) {
			String[] cmps = hostPort.split(":"); //$NON-NLS-1$
			nvpHostPorts.addNameValuePair(new NameValuePair(cmps[0], cmps[1]));
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