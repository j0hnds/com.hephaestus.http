package com.hephaestus.http.util;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.widgets.Display;

/**
 * A utility class used by the plugin to access various functionality.
 * 
 * @author Dave Sieh
 * 
 */
public class ClipboardUtil {
	// Reference to the clipboard
	private static Clipboard clipboard;

	/**
	 * Retrieves a reference to the clipboard.
	 * 
	 * @return reference to the clipboard.
	 */
	public static Clipboard getClipboard() {
		if (clipboard == null) {
			clipboard = new Clipboard(Display.getCurrent());
		}

		return clipboard;
	}

}
