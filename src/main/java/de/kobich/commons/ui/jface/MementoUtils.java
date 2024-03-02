package de.kobich.commons.ui.jface;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.ui.IMemento;

import de.kobich.commons.ui.jface.memento.DialogSettingsAdapter;
import de.kobich.commons.ui.jface.memento.MementoAdapter;
import de.kobich.commons.ui.memento.IMementoItem;

public class MementoUtils {
	/**
	 * Returns the memento item to save or null
	 * @param memento
	 * @param name
	 * @return
	 */
	public static IMementoItem getMementoItemToSave(IMemento memento, String name) {
		if (memento == null) {
			return null;
		}
		IMemento viewMemento = memento.createChild(name);
		IMementoItem mementoItem = new MementoAdapter(viewMemento);
		return mementoItem;
	}
	
	/**
	 * Returns the memento item to restore or null
	 * @param memento
	 * @param name
	 * @return
	 */
	public static IMementoItem getMementoItemToRestore(IMemento memento, String name) {
		if (memento == null) {
			return null;
		}
		IMemento viewMemento = memento.getChild(name);
		if (viewMemento == null) {
			return null;
		}
		IMementoItem mementoItem = new MementoAdapter(viewMemento);
		return mementoItem;
	}
	
	public static IMementoItem getMementoItemToSave(IDialogSettings settings) {
		IMementoItem mementoItem = new DialogSettingsAdapter(settings);
		return mementoItem;
	}
	
	/**
	 * Returns the dialog settings
	 * @return dialog settings
	 */
	public static IDialogSettings getDialogBoundsSettings(IDialogSettings settings, String name) {
		IDialogSettings dialogSettings = settings.getSection(name);
		if (dialogSettings == null) {
			dialogSettings = settings.addNewSection(name);
		}
		
		return dialogSettings;
	}
}
