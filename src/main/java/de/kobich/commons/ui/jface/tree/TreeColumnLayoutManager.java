package de.kobich.commons.ui.jface.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import lombok.Setter;

/**
 * This tree column manager allows to show/hide columns.
 */
public class TreeColumnLayoutManager {
	private final Composite parent;
	private final Tree tree;
	private final TreeColumnLayout treeColumnLayout;
	private final List<TreeColumnData> columns;
	@Setter
	private ITreeColumnProvider treeColumnProvider;
	
	public TreeColumnLayoutManager(Composite parent, Tree tree) {
		this.parent = parent;
		this.tree = tree;
		this.columns = new ArrayList<>();
		this.treeColumnProvider = new TreeColumnProvider(tree);
		if (parent.getLayout() instanceof TreeColumnLayout layout) {
			this.treeColumnLayout = layout;
		}
		else {
			throw new IllegalArgumentException("Parent must use %s instead of %s".formatted(TreeColumnLayout.class.getName(), parent.getLayout().getClass().getName()));
		}
	}
	
	public void addColumn(TreeColumnData column) {
		this.columns.add(column);
	}
	
	public void createColumns() {
		List<TreeColumnData> visibleColumns = getVisibleColumns();
		final int totalWidthShare = visibleColumns.stream().mapToInt(TreeColumnData::getWidthShare).sum();
		for (TreeColumnData c : visibleColumns) {
			int widthPercent = c.getWidthShare() * 100 / totalWidthShare;
			TreeColumn treeColumn = treeColumnProvider.createTreeColumn(c);
			int minimumWidth = Math.max(c.getMinimumWidth(), ColumnWeightData.MINIMUM_WIDTH);
			treeColumnLayout.setColumnData(treeColumn, new ColumnWeightData(widthPercent, minimumWidth));
		}
		
	}
	public void updateColumns() {
		try {
			tree.setRedraw(false);
			final TreeItem[] selection = tree.getSelection();
			
			// remove all columns
			for (TreeColumn c : tree.getColumns()) {
			    c.dispose();
			}
			
			// create visible columns
			createColumns();
			
			// restore selection
			tree.setSelection(selection);
		}
		finally {
			// redraw
			tree.setRedraw(true);
			parent.layout(true, true);
		}
	}

	public List<TreeColumnData> getHideableColumns() {
		return this.columns.stream().filter(TreeColumnData::isHideable).toList();
	}
	
	public List<TreeColumnData> getVisibleColumns() {
		return this.columns.stream().filter(TreeColumnData::isVisible).toList();
	}
	
	public void setVisibleColumnsByObjectArray(Object[] visibleObjects) {
		List<TreeColumnData> visibleColumns = new ArrayList<>();
		for (Object o : visibleObjects) {
			if (o instanceof TreeColumnData c) {
				visibleColumns.add(c);
			}
		}
		setVisibleColumns(visibleColumns);
	}
	
	public void setVisibleColumns(List<TreeColumnData> visibleColumns) {
		for (TreeColumnData c : this.getHideableColumns()) {
			boolean newVisible = visibleColumns.contains(c);
			c.setVisible(newVisible);
		}
	}
	
	public Optional<Object> getElementByIndex(int index) {
		return Optional.ofNullable(getVisibleColumns().get(index).getElement());
	}

}
