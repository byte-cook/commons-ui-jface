package de.kobich.commons.ui.jface.memento;

import org.eclipse.jface.dialogs.IDialogSettings;

import de.kobich.commons.ui.memento.IMementoItem;


public class DialogSettingsAdapter implements IMementoItem {
	private IDialogSettings settings;

	public DialogSettingsAdapter(IDialogSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public String[] getArray(String key) {
		return settings.getArray(key);
	}

	@Override
	public void putArray(String key, String[] array) {
		settings.put(key, array);
	}

	@Override
	public int getInteger(String key) {
		return settings.getInt(key);
	}

	@Override
	public int getInteger(String key, int defaultValue) {
		try {
			return settings.getInt(key);
		} 
		catch (NumberFormatException exc) {
			return defaultValue;
		}
	}

	@Override
	public void putInteger(String key, int value) {
		settings.put(key, value);
	}

	@Override
	public String getString(String key) {
		return settings.get(key);
	}

	public String getString(String key, String defaultValue) {
		if (getString(key) != null) {
			return getString(key);
		}
		return defaultValue;
	}

	@Override
	public void putString(String key, String value) {
		settings.put(key, value);
	}

	@Override
	public boolean getBoolean(String key) {
		return settings.getBoolean(key);
	}

	@Override
	public void putBoolean(String key, boolean value) {
		settings.put(key, value);
	}

}
