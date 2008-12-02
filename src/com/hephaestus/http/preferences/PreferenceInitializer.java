package com.hephaestus.http.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.hephaestus.http.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_HOST_PORTS, "localhost:80"); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_PROXY_HOST_PORT, ""); //$NON-NLS-1$
		store.setDefault(PreferenceConstants.P_STRICT_SSL, true);
	}

}
