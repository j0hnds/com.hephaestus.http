package com.hephaestus.http.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

import com.hephaestus.http.model.NameValuePair;
import com.hephaestus.http.model.NameValuePairViewer;
import com.hephaestus.http.model.NameValuePairs;

/**
 * A Structured Content Provider for NameValuePairs.
 * 
 * @author Dave Sieh
 * 
 */
public final class NameValuePairContentProvider implements
		IStructuredContentProvider, NameValuePairViewer {

	// The name value pairs
	private NameValuePairs pairs;

	// The table viewer
	private TableViewer tableViewer;

	/**
	 * Constructs a new NameValuePairContentProvider.
	 * 
	 * @param pairs
	 *            The name value pairs
	 * @param tableViewer
	 *            the table viewer
	 */
	public NameValuePairContentProvider(NameValuePairs pairs,
			TableViewer tableViewer) {
		this.pairs = pairs;
		this.pairs.addChangeListener(this);
		this.tableViewer = tableViewer;
	}

	public Object[] getElements(Object inputElement) {
		return pairs.getNameValuePairs().toArray();
	}

	public void dispose() {
		pairs.removeChangeListener(this);
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			((NameValuePairs) newInput).addChangeListener(this);
		}
		if (oldInput != null) {
			((NameValuePairs) oldInput).removeChangeListener(this);
		}
	}

	public void addNameValuePair(NameValuePair nvp) {
		tableViewer.add(nvp);
	}

	public void removeNameValuePair(NameValuePair nvp) {
		tableViewer.remove(nvp);
	}

	public void updateNameValuePair(NameValuePair nvp) {
		tableViewer.update(nvp, null);
	}

}
