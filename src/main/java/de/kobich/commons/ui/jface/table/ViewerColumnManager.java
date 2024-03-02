package de.kobich.commons.ui.jface.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewerColumnManager implements Iterable<ViewerColumn> {
	private final List<ViewerColumn> columns;

	public ViewerColumnManager(ViewerColumn... columns) {
		this.columns = new ArrayList<ViewerColumn>();
		for (ViewerColumn c : columns) {
			this.columns.add(c);
		}
	}

	public ViewerColumnManager(List<ViewerColumn> columns) {
		this.columns = columns;
	}

	@Override
	public Iterator<ViewerColumn> iterator() {
		return columns.iterator();
	}
	
	public int indexOf(ViewerColumn column) {
		return columns.indexOf(column);
	}
	
	public ViewerColumn getByIndex(int index) {
		if (index < 0 || columns.size() < index) {
			return null;
		}
		return columns.get(index);
	}
	
}
