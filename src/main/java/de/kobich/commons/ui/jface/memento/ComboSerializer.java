package de.kobich.commons.ui.jface.memento;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Combo;

import de.kobich.commons.ui.memento.IMementoItem;
import de.kobich.commons.ui.memento.IMementoItemSerializer;

public class ComboSerializer implements IMementoItemSerializer<Combo> {
	private static final int HISTORY_SIZE = 10;
	private final String state;
	private final String defaultValue;

	public ComboSerializer(String state, String defaultValue) {
		this.state = state;
		this.defaultValue = defaultValue;
	}

	@Override
	public void restore(Combo combo, IMementoItem mementoItem) {
		String[] items = mementoItem.getArray(state);
		if (items != null) {
			for (String item : items) {
				if (isValid(item)) {
					combo.add(item);
				}
			}
			combo.setText(items[0]);
		}
		else {
			combo.setText(defaultValue);
		}

	}

	@Override
	public void save(Combo combo, IMementoItem mementoItem) {
		Set<String> newState = new LinkedHashSet<String>();
		newState.add(combo.getText());
		for (int i = 0; i < combo.getItemCount(); ++i) {
			if (newState.size() > HISTORY_SIZE - 1) {
				break;
			}
			newState.add(combo.getItem(i));
		}
		String[] newStateArray = newState.toArray(new String[0]);
		mementoItem.putArray(state, newStateArray);
	}

	protected boolean isValid(String item) {
		return true;
	}
}
