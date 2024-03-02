package de.kobich.commons.ui.jface.listener;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.TreeItem;

public class TreeExpandKeyListener extends KeyAdapter {
	private final TreeViewer treeViewer;
	
	/**
	 * @param treeViewer
	 */
	public TreeExpandKeyListener(TreeViewer treeViewer) {
		this.treeViewer = treeViewer;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		boolean collapseKey = e.keyCode == SWT.ARROW_LEFT;
		boolean expandKey = e.keyCode == SWT.ARROW_RIGHT;
		if (collapseKey || expandKey) {
			TreeItem[] items = treeViewer.getTree().getSelection();
			if (items.length > 0) {
				// cancel the operation
				e.doit = false;
				
				TreeItem firstItem = items[0];
				if (collapseKey) {
					boolean notExpandable = collapseKey && !treeViewer.isExpandable(firstItem.getData());
					boolean alreadyCollapsed = collapseKey && !firstItem.getExpanded();
					if (notExpandable || alreadyCollapsed) {
						TreeItem item2Select = firstItem.getParentItem();
						if (item2Select != null) {
							treeViewer.setSelection(new StructuredSelection(item2Select.getData()));
						}
						return;
					}
					firstItem.setExpanded(false);
					treeViewer.refresh();
				}
				else if (expandKey) {
					boolean alreadyExpanded = firstItem.getExpanded();
					if (alreadyExpanded) {
						TreeItem[] children = firstItem.getItems();
						if (children.length > 0) {
							TreeItem item2Select = children[0];
							if (item2Select != null) {
								treeViewer.getTree().setSelection(item2Select);
							}
						}
						return;
					}
					firstItem.setExpanded(true);
					treeViewer.refresh();
					
					// scroll to item
					TreeItem[] children = firstItem.getItems();
					if (children.length > 0) {
						TreeItem lastChild = children[children.length - 1];
						// show last child
						treeViewer.reveal(lastChild.getData());
						// show first item
						treeViewer.reveal(firstItem.getData());
					}
				}
			}
		}
	}
}
