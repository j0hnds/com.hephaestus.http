package com.hephaestus.http.views;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class NameValuePairCellModifier implements ICellModifier {

	private TableViewer tableViewer;
	private NameValuePairs pairs;
	
	public NameValuePairCellModifier(TableViewer tableViewer, NameValuePairs pairs) {
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
			valueString = ((String) value).trim();
			nvp.setName(valueString);
			break;
			
		case 1: // Value
			valueString = ((String) value).trim();
			nvp.setValue(valueString);
		}
		
		pairs.changeNameValuePair(nvp);
	}

	private int columnIndex(String property) {
		int index = 0;
		for (Object columnName : tableViewer.getColumnProperties()) {
			if (property.equals(columnName)) {
				break;
			}
		}
		return index;
	}
}
