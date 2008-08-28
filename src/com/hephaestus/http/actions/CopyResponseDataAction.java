package com.hephaestus.http.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.hephaestus.http.Messages;
import com.hephaestus.http.util.ClipboardUtil;
import com.hephaestus.http.views.ui.ResponseDataAccess;

public class CopyResponseDataAction extends Action {
	
	private ResponseDataAccess rdAccess;
	
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
			cb.setContents(new Object[] { rd }, new TextTransfer[] { text_transfer } );
		}
	}
}
