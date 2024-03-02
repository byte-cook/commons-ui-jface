package de.kobich.commons.ui.jface.state;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import de.kobich.commons.ui.state.ControlStateRepository;

public class ControlStateModifyListener implements ModifyListener {
	private final ControlStateRepository stateRepository;

	/**
	 * @param stateRepository
	 */
	public ControlStateModifyListener(ControlStateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		Widget widget = e.widget;
		if (widget instanceof Text) {
			Text text = (Text) widget;
			String value = text.getText();
			if (StringUtils.isBlank(value)) {
				stateRepository.fireNoItemSelected();
			}
			else {
				stateRepository.fireSingleItemSelected();
			}
		}
	}

}
