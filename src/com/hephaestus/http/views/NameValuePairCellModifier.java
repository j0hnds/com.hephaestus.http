package com.hephaestus.http.views;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

import com.hephaestus.http.model.NameValuePair;
import com.hephaestus.http.model.NameValuePairs;

/**
 * A Cell Modifier for Name Value Pair objects.
 * 
 * @author Dave Sieh
 */
public final class NameValuePairCellModifier implements ICellModifier {

	// The table viewer
	private TableViewer tableViewer;
	// The NameValuePairs.
	private NameValuePairs pairs;

	/**
	 * Constructs a new NameValuePairCellModifier.
	 * 
	 * @param tableViewer
	 *            the table viewer to which the cell modifier is attached.
	 * @param pairs
	 *            the name value pairs.
	 */
	public NameValuePairCellModifier(TableViewer tableViewer,
			NameValuePairs pairs) {
		this.tableViewer = tableViewer;
		this.pairs = pairs;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		Object value = null;
		NameValuePair pair = (NameValuePair) element;

		switch (columnIndex(property)) {
		case 0: // Name
			value = pair.getName();
			break;
		case 1: // Value
			value = pair.getValue();
			break;
		}

		return value;
	}

	public void modify(Object element, String property, Object value) {
		int index = columnIndex(property);

		TableItem ti = (TableItem) element;
		NameValuePair nvp = (NameValuePair) ti.getData();
		String valueString = null;

		switch (index) {
		case 0: // Name
			if (value != null) {
				valueString = ((String) value).trim();
				nvp.setName(valueString);
			}
			break;

		case 1: // Value
			if (value != null) {
				valueString = ((String) value).trim();
				nvp.setValue(valueString);
			}
		}

		pairs.changeNameValuePair(nvp);
	}

	/**
	 * Identify the column the specified property represents.
	 * 
	 * @param property
	 *            the name of the column property
	 * @return the index of the property
	 */
	private int columnIndex(String property) {
		int index = 0;
		for (Object columnName : tableViewer.getColumnProperties()) {
			if (property.equals(columnName)) {
				break;
			}
			index++;
		}
		return index;
	}
}
