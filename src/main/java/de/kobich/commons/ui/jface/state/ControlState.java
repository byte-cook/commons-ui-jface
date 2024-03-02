package de.kobich.commons.ui.jface.state;

import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolItem;

import de.kobich.commons.ui.state.AbstractControlState;
import de.kobich.commons.ui.state.IControlState;

public class ControlState extends AbstractControlState implements IControlState {
	private ToolItem toolItem;
	private IAction action;
	private Control control;
	
	public ControlState(Control control) {
		this.control = control;
	}
	public ControlState(ToolItem toolItem) {
		this.toolItem = toolItem;
	}
	public ControlState(IAction action) {
		this.action = action;
	}

	public void setEnabled(boolean b) {
		if (toolItem != null) {
			toolItem.setEnabled(b);
		}
		else if (action != null) {
			action.setEnabled(b);
		}
		else if (control != null) {
			control.setEnabled(b);
		}
	}
}
