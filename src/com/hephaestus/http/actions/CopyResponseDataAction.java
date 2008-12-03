package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Messages;
import com.hephaestus.http.views.ui.ResponseDataAccess;
import com.hephaestus.util.ClipboardUtil;

/**
 * This Action class is responsible for copying the contents of the HTTP
 * response window to the clipboard.
 * 
 * @author pc23dxs
 */
public class CopyResponseDataAction extends Action {

	// Reference to the interface that exposes the response data.
	private ResponseDataAccess rdAccess;

	/**
	 * Constructs a new CopyResponseDataAction object.
	 * 
	 * @param rdAccess
	 *            reference to the interface that exposes the response data.
	 */
	public CopyResponseDataAction(ResponseDataAccess rdAccess) {
		this.rdAccess = rdAccess;
		setText(Messages.getString("CopyResponseDataAction.Text")); //$NON-NLS-1$
		setToolTipText(Messages.getString("CopyResponseDataAction.ToolTipText")); //$NON-NLS-1$
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
	}

	@Override
	public void run() {
		String rd = rdAccess.getResponseData();

		if (rd != null && rd.length() > 0) {
			Clipboard cb = ClipboardUtil.getClipboard();
			TextTransfer text_transfer = TextTransfer.getInstance();
			cb.setContents(new Object[] { rd },
					new TextTransfer[] { text_transfer });
		}
	}
}
