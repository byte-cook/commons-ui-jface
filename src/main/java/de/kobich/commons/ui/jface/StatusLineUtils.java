package de.kobich.commons.ui.jface;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

public class StatusLineUtils {
	/**
	 * Sets status line message
	 * @param part
	 * @param message
	 */
	public static void setStatusLineMessage(IWorkbenchPart part, String message, boolean error) {
		if (part instanceof IViewPart) {
			StatusLineUtils.setStatusLineMessage((IViewPart) part, message, error);
		}
		else if (part instanceof IEditorPart) {
			StatusLineUtils.setStatusLineMessage((IEditorPart) part, message, error);
		}

	}
	
	/**
	 * Sets status line message
	 * @param viewPart
	 * @param message
	 */
	public static void setStatusLineMessage(IViewPart viewPart, String message, boolean error) {
		IStatusLineManager statusLineManager = viewPart.getViewSite().getActionBars().getStatusLineManager();
		if (error) {
			statusLineManager.setErrorMessage(message);
		}
		else {
			statusLineManager.setMessage(message);
		}
	}

	/**
	 * Sets status line message
	 * @param editorPart
	 * @param message
	 */
	public static void setStatusLineMessage(IEditorPart editorPart, String message, boolean error) {
		IStatusLineManager statusLineManager = editorPart.getEditorSite().getActionBars().getStatusLineManager();
		if (error) {
			statusLineManager.setErrorMessage(message);
			statusLineManager.setMessage(null);
		}
		else {
			statusLineManager.setErrorMessage(null);
			statusLineManager.setMessage(message);
		}
	}
}
