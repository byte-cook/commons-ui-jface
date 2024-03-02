package de.kobich.commons.ui.jface.memento;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.SashForm;

import de.kobich.commons.ui.memento.IMementoItem;
import de.kobich.commons.ui.memento.IMementoItemSerializer;

public class SashFormSerializer implements IMementoItemSerializer<SashForm> {
	private final String state;
	private final int[] defaultValue;

	public SashFormSerializer(String state, int[] defaultValue) {
		this.state = state;
		this.defaultValue = defaultValue;
	}

	@Override
	public void restore(SashForm sashForm, IMementoItem mementoItem) {
		String[] items = mementoItem.getArray(state);
		if (items != null) {
			int[] values = new int[items.length];
			for (int i = 0; i < items.length; ++ i) {
				values[i] = Integer.parseInt(items[i]);
			}
			sashForm.setWeights(values);
		}
		else {
			sashForm.setWeights(defaultValue);
		}
	}

	@Override
	public void save(SashForm sashForm, IMementoItem mementoItem) {
		List<String> newState = new ArrayList<String>();
		for (int w : sashForm.getWeights()) {
			newState.add("" + w);
		}
		String[] newStateArray = newState.toArray(new String[0]);
		mementoItem.putArray(state, newStateArray);
	}
}
