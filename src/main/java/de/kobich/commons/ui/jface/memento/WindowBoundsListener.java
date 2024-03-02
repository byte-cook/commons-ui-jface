package de.kobich.commons.ui.jface.memento;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Shell;

import de.kobich.commons.ui.memento.WindowBounds;

public class WindowBoundsListener extends ControlAdapter {
	private final Shell shell;
	private final WindowBounds bounds;
	
	/**
	 * Constructor
	 * @param shell 
	 * @param bounds the current bounds settings
	 */
	public WindowBoundsListener(Shell shell, WindowBounds bounds) {
		this.shell = shell;
		this.bounds = bounds;
	}
	
	public void controlMoved(ControlEvent e) {
		Shell shell = (Shell) e.getSource();
		saveBounds(shell);
	}

	public void controlResized(ControlEvent e) {
		Shell shell = (Shell) e.getSource();
		saveBounds(shell);
	}
	
	private void saveBounds(Shell shell) {
		if (shell == null) {
			return;
		}
		if (shell.isDisposed()) {
			return;
		}
		if (shell.getMinimized()) {
			return;
		}
		
		bounds.maximized = getShell().getMaximized();
		if (getShell().getMaximized()) {
			return;
		}
		bounds.x = Math.max(0, getShell().getLocation().x);
		bounds.y = Math.max(0, getShell().getLocation().y);
		bounds.width =  Math.max(100, getShell().getSize().x);
		bounds.height = Math.max(100, getShell().getSize().y);
	}

	/**
	 * @return the bounds
	 */
	public WindowBounds getBounds() {
		return bounds;
	}

	/**
	 * @return the shell
	 */
	public Shell getShell() {
		return shell;
	}

}
