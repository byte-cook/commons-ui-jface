package de.kobich.commons.ui.jface.listener;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Widget;

/**
 * Dispose listener for widgets
 * @author ckorn
 *
 */
public class WidgetDisposeListener implements DisposeListener {
	private final Widget widget;
	
	/**
	 * @param widget
	 */
	public WidgetDisposeListener(Widget widget) {
		this.widget = widget;
	}

	@Override
	public void widgetDisposed(DisposeEvent e) {
		this.widget.dispose();
	}

}
