package de.kobich.commons.ui.jface.tree;

import org.eclipse.swt.widgets.TreeColumn;

public interface ITreeColumnProvider {
	/**
	 * Creates a tree column
	 */
	TreeColumn createTreeColumn(TreeColumnData columnData);
}
