package de.kobich.commons.ui.jface.memento;

import org.eclipse.ui.IMemento;

import de.kobich.commons.ui.memento.IMementoItem;


public class MementoAdapter implements IMementoItem {
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	private IMemento memento;

	public MementoAdapter(IMemento memento) {
		this.memento = memento;
	}

	@Override
	public String[] getArray(String key) {
		String textData = memento.getChild(key).getTextData();
		if (textData != null) {
			return textData.split(LINE_SEPARATOR);
		}
		return null;
	}

	@Override
	public void putArray(String key, String[] array) {
		String textData = "";
		for (String v : array) {
			textData += v + LINE_SEPARATOR;
		}
		memento.createChild(key).putTextData(textData);
	}

	@Override
	public int getInteger(String key) {
		return memento.getInteger(key);
	}

	@Override
	public int getInteger(String key, int defaultValue) {
		Integer integer = memento.getInteger(key);
		if (integer != null) {
			return integer;
		}
		return defaultValue;
	}

	@Override
	public void putInteger(String key, int value) {
		memento.putInteger(key, value);
	}

	@Override
	public String getString(String key) {
		return memento.getString(key);
	}

	public String getString(String key, String defaultValue) {
		if (getString(key) != null) {
			return getString(key);
		}
		return defaultValue;
	}

	@Override
	public void putString(String key, String value) {
		memento.putString(key, value);
	}

	@Override
	public boolean getBoolean(String key) {
		return memento.getBoolean(key);
	}

	@Override
	public void putBoolean(String key, boolean value) {
		memento.putBoolean(key, value);
	}

}
