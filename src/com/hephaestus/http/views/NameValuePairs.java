package com.hephaestus.http.views;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The domain model for name-value pairs
 * 
 * @author Dave Sieh
 */
public class NameValuePairs {
	private List<NameValuePair> pairs;
	private Set<NameValuePairViewer> changeListeners;
	
	public NameValuePairs() {
		changeListeners = new HashSet<NameValuePairViewer>();
		pairs = new ArrayList<NameValuePair>();
	}
	
	public List<NameValuePair> getNameValuePairs() {
		return pairs;
	}
	
	public void addNameValuePair(NameValuePair nvp) {
		pairs.add(nvp);
		notifyAdd(nvp);
	}
	
	public void removeNameValuePair(NameValuePair nvp) {
		pairs.remove(nvp);
		notifyRemove(nvp);
	}
	
	public void changeNameValuePair(NameValuePair nvp) {
		notifyChange(nvp);
	}
	
	public void removeAll() {
		for (NameValuePair nvp : pairs) {
			notifyRemove(nvp);
		}
		pairs.clear();
	}
	
	public void addChangeListener(NameValuePairViewer listener) {
		changeListeners.add(listener);
	}
	
	public void removeChangeListener(NameValuePairViewer listener) {
		changeListeners.remove(listener);
	}

	private void notifyChange(NameValuePair nvp) {
		for (NameValuePairViewer listener : changeListeners) {
			listener.updateNameValuePair(nvp);
		}
	}

	private void notifyRemove(NameValuePair nvp) {
		for (NameValuePairViewer listener : changeListeners) {
			listener.removeNameValuePair(nvp);
		}
	}

	private void notifyAdd(NameValuePair nvp) {
		for (NameValuePairViewer listener : changeListeners) {
			listener.addNameValuePair(nvp);
		}
	}
}
