package com.hephaestus.http.views.ui;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.hephaestus.http.Activator;
import com.hephaestus.http.preferences.PreferenceConstants;

/**
 * This control displays the controls necessary to capture basic URL
 * information:
 * 
 * <ul>
 * <li>Verb</li>
 * <li>Protocol</li>
 * <li>Host:Port</li>
 * <li>URI</li>
 * </ul>
 * 
 * Supported styles: SWT.NONE
 * 
 * @author Dave Sieh
 */
public class URLFields extends Composite {

	// The protocols loaded into the protocol drop-down
	private static final String[] PROTOCOLS = { "http", "https" }; //$NON-NLS-1$ //$NON-NLS-2$

	// The verbs loaded into the verb drop-down
	private static final String[] VERBS = { "GET", "PUT", "POST", "DELETE" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	// Drop-down for verbs
	private Combo cbVerbs;

	// Drop-down for method protocols
	private Combo cbProtocol;

	// Drop-down for host-ports
	private Combo cbHostPort;

	// Drop-down for URI's
	private Combo cbURI;

	/**
	 * Construct a new URLFields component.
	 * 
	 * @param parent
	 *            the parent control
	 * @param style
	 *            the styles.
	 */
	public URLFields(Composite parent, int style) {
		super(parent, style);

		createControls();
	}

	/**
	 * Returns the URL currently held by the control
	 * 
	 * @return the URL
	 */
	public final String getURL() {
		StringBuilder sb = new StringBuilder();
		sb.append(cbProtocol.getText());
		sb.append("://"); //$NON-NLS-1$
		sb.append(cbHostPort.getText());
		sb.append("/"); //$NON-NLS-1$
		sb.append(cbURI.getText());

		return sb.toString();
	}

	/**
	 * Saves the URI currently displayed in the text field of the URI combo box.
	 * This allows the URI to subsequently selected from the list if necessary.
	 */
	public final void saveCurrentURI() {
		String uri = cbURI.getText();
		if (cbURI.indexOf(uri) < 0) {
			cbURI.add(uri);
		}

	}

	/**
	 * Returns the verb currently held by this control
	 * 
	 * @return the verb.
	 */
	public final String getVerb() {
		return cbVerbs.getText();
	}

	/**
	 * Create the controls within this composite.
	 */
	private void createControls() {
		RowLayout layout = new RowLayout(SWT.HORIZONTAL);
		layout.center = true;
		layout.fill = true;
		layout.justify = false;
		layout.pack = true;
		layout.spacing = 5;
		layout.wrap = true;
		this.setLayout(layout);

		// Drop-down for verbs
		cbVerbs = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
		cbVerbs.setItems(VERBS);
		cbVerbs.select(0);

		// Drop-down for protocols
		cbProtocol = new Combo(this, SWT.BORDER | SWT.READ_ONLY);
		cbProtocol.setItems(PROTOCOLS);
		cbProtocol.select(0);

		Label lblFirstDelimiter = new Label(this, SWT.NONE);
		lblFirstDelimiter.setText("://"); //$NON-NLS-1$

		// Drop-down for host/ports
		cbHostPort = new Combo(this, SWT.BORDER);
		RowData rd = new RowData();
		rd.width = 200;
		loadHostPortComboBox();

		Label lblSecondDelimiter = new Label(this, SWT.NONE);
		lblSecondDelimiter.setText("/"); //$NON-NLS-1$

		cbURI = new Combo(this, SWT.BORDER);
		cbURI.setLayoutData(rd);
	}

	private void loadHostPortComboBox() {
		Preferences store = Activator.getDefault().getPluginPreferences();

		String hpString = store.getString(PreferenceConstants.P_HOST_PORTS);
		cbHostPort.setItems(hpString.split("\\|")); //$NON-NLS-1$
		if (cbHostPort.getItemCount() > 0) {
			cbHostPort.select(0);
		}
	}

	@Override
	public boolean setFocus() {
		return cbURI.setFocus();
	}

	/**
	 * Resets the host:ports combo box back to the preferences.
	 */
	public final void resetHostPorts() {
		cbHostPort.removeAll();
		loadHostPortComboBox();
	}

	/**
	 * Returns the protocol specified by this control.
	 * 
	 * @return the protocol.
	 */
	public final String getProtocol() {
		return cbProtocol.getText();
	}

	/**
	 * Returns the host:port specified by this control.
	 * 
	 * @return the host:port.
	 */
	public final String getHostPort() {
		return cbHostPort.getText();
	}

	/**
	 * Gets the text in the URI field.
	 * 
	 * @return text value of the URI field
	 */
	public final String getURI() {
		return cbURI.getText();
	}

	/**
	 * Sets the value in the HostPort field.
	 * 
	 * @param hostPort
	 *            the host port value.
	 */
	public final void setHostPort(String hostPort) {
		cbHostPort.setText(hostPort);
	}

	/**
	 * Sets the protocol shown in the protocol field.
	 * 
	 * @param protocol
	 *            the value of the protocol
	 */
	public final void setProtocol(String protocol) {
		cbProtocol.setText(protocol);
	}

	/**
	 * Sets the value of the URI in the field.
	 * 
	 * @param uri
	 *            the URI value
	 */
	public final void setURI(String uri) {
		cbURI.setText(uri);
	}

	/**
	 * Sets the value of the verb in the field.
	 * 
	 * @param verb
	 *            the verb to display.
	 */
	public final void setVerb(String verb) {
		cbVerbs.setText(verb);
	}
}
