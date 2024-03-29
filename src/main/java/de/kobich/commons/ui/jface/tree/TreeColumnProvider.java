package de.kobich.commons.ui.jface.tree;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TreeColumnProvider implements ITreeColumnProvider {
	private final Tree tree;

	@Override
	public TreeColumn createTreeColumn(TreeColumnData columnData) {
		TreeColumn treeColumn = new TreeColumn(tree, SWT.LEFT);
		treeColumn.setText(columnData.getText());
		return treeColumn;
	}

}
