package de.kobich.commons.ui.jface.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import de.kobich.commons.ui.memento.IMementoItem;
import de.kobich.commons.ui.memento.IMementoItemSerializable;
import lombok.Setter;

/**
 * This tree column manager allows to show/hide columns.
 */
public class TreeColumnLayoutManager implements IMementoItemSerializable {
	private final String STATE_VISIBLE_COLUMNS = "visibleColumns";
	private final Composite parent;
	private final TreeViewer treeViewer;
	private final TreeColumnLayout treeColumnLayout;
	private final List<TreeColumnData> columns;
	@Nullable
	private final IMementoItem mementoItem;
	@Setter
	private ITreeColumnProvider treeColumnProvider;
	
	public TreeColumnLayoutManager(Composite parent, TreeViewer treeViewer, @Nullable IMementoItem mementoItem) {
		this.parent = parent;
		this.treeViewer = treeViewer;
		this.columns = new ArrayList<>();
		this.mementoItem = mementoItem;
		this.treeColumnProvider = new TreeColumnProvider(treeViewer.getTree());
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
		Tree tree = treeViewer.getTree();
		try {
			tree.setRedraw(false);
			// save state
			final TreeItem[] selection = tree.getSelection();
			final int scrollSelection = tree.getVerticalBar().getSelection();
			final Object[] expandedElements = treeViewer.getExpandedElements();
			
			// remove all columns
			for (TreeColumn c : tree.getColumns()) {
			    c.dispose();
			}
			
			// create visible columns
			createColumns();
			
			// restore state
			tree.setSelection(selection);
			tree.getVerticalBar().setSelection(scrollSelection);
			treeViewer.setExpandedElements(expandedElements);
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
	
	@Override
	public void saveState() {
		if (mementoItem == null) {
			throw new IllegalStateException("Memento item is not set");
		}
		mementoItem.putArray(STATE_VISIBLE_COLUMNS, getVisibleColumns().stream().map(TreeColumnData::getText).toList().toArray(new String[0]));
	}
	
	@Override
	public void restoreState() {
		if (mementoItem == null) {
			throw new IllegalStateException("Memento item is not set");
		}
		String[] savedColumns = mementoItem.getArray(STATE_VISIBLE_COLUMNS);
		if (savedColumns != null) {
			List<String> savedColumnList = Arrays.asList(savedColumns);
			for (TreeColumnData column : this.columns) {
				boolean visible = savedColumnList.contains(column.getText());
				column.setVisible(visible);
			}
		}
	}

}
