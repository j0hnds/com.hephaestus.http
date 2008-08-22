package com.hephaestus.http.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class NameValuePairLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		
		NameValuePair nvp = (NameValuePair) element;
		switch (columnIndex) {
		case 0: // Name
			result = nvp.getName();
			break;
			
		case 1: // value
			result = nvp.getValue();
			break;
			
		default:
			break;
		}
		
		return result;
	}

}
