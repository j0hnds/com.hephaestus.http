package com.hephaestus.http.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

public class NameValuePairContentProvider implements
		IStructuredContentProvider, NameValuePairViewer {

	private NameValuePairs pairs;
	private TableViewer tableViewer;

	public NameValuePairContentProvider(NameValuePairs pairs, TableViewer tableViewer) {
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
