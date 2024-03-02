package de.kobich.commons.ui.jface.listener;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Resource;

/**
 * Dispose listener for resources (Image, Font, etc.).
 * Add this listener to parent component to guarantee that resources are disposed.
 * @author ckorn
 *
 */
public class ResourceDisposeListener implements DisposeListener {
	private final Resource resource;
	
	/**
	 * @param resource
	 */
	public ResourceDisposeListener(Resource resource) {
		this.resource = resource;
	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		this.resource.dispose();
	}

}
