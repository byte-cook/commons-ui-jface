package de.kobich.commons.ui.jface.table;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Prevents a table column to become invisible (probably only necessary on linux gtk+).
 * @author ckorn
 *
 */
public class TableColumnResizeControlListener extends ControlAdapter {
	private final int minWidth;
	
	public TableColumnResizeControlListener() {
		this(10);
	}
	public TableColumnResizeControlListener(int minWidth) {
		this.minWidth = minWidth;
	}

	@Override
	public void controlResized(ControlEvent e) {
		TableColumn col = (TableColumn) e.widget;
		if(col.getWidth()<minWidth) {
            col.setWidth(minWidth);
		}
	}
}
