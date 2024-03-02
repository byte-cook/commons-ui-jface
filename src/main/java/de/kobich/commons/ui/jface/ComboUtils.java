package de.kobich.commons.ui.jface;

import org.eclipse.swt.widgets.Combo;

public class ComboUtils {
	public static final int HISTORY_SIZE = 5;
	
	/**
	 * Adds the current combo text to the combo box
	 * @param combo
	 */
	public static void addTextToCombo(String text, Combo combo) {
		addTextToCombo(text, combo, HISTORY_SIZE);
	}
	/**
	 * Adds the current combo text to the combo box
	 * @param combo
	 */
	public static void addTextToCombo(String text, Combo combo, int size) {
		for (String item : combo.getItems()) {
			if (item.equals(text)) {
				combo.remove(item);
				combo.setText(item);
				combo.add(item, 0);
				return;
			}
		}
		
		if (combo.getItems().length + 1 > size) {
			combo.remove(combo.getItems().length - 1);
		}
		combo.add(text, 0);
	}
	
	
}
