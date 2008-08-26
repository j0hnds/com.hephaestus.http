package com.hephaestus.http.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.hephaestus.http.model.NameValuePair;

/**
 * A TableLabelProvider for NameValuePair data.
 * 
 * @author Dave Sieh
 */
public final class NameValuePairLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String result = ""; //$NON-NLS-1$
		
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
