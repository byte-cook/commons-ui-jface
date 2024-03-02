package de.kobich.commons.ui.jface.memento;

import org.eclipse.swt.widgets.Button;

import de.kobich.commons.ui.memento.IMementoItem;
import de.kobich.commons.ui.memento.IMementoItemSerializer;

public class RadioButtonSerializer implements IMementoItemSerializer<Button[]> {
	private final String state;
	private final String defaultValue;

	public RadioButtonSerializer(String state, String defaultValue) {
		this.state = state;
		this.defaultValue = defaultValue;
	}

	@Override
	public void save(Button[] buttons, IMementoItem mementoItem) {
		for (Button b : buttons) {
			if (b.getSelection()) {
				mementoItem.putString(state, b.getText());
				return;
			}
		}
	}

	@Override
	public void restore(Button[] buttons, IMementoItem mementoItem) {
		String value = mementoItem.getString(state, defaultValue);
		for (Button b : buttons) {
			boolean selected = b.getText().equals(value);
			b.setSelection(selected);
		}
	}

}
