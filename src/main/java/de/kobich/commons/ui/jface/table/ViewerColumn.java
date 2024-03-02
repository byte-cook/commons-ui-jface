package de.kobich.commons.ui.jface.table;

public class ViewerColumn {
	private final String name;
	private final int widthPercent;
	private final int minimumWidth;
	
	public ViewerColumn(String name, int widthPercent) {
		this(name, widthPercent, 20);
	}
	
	public ViewerColumn(String name, int widthPercent, int minWidth) {
		this.name = name;
		this.widthPercent = widthPercent;
		this.minimumWidth = minWidth;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the widthPercent
	 */
	public int getWidthPercent() {
		return widthPercent;
	}

	/**
	 * @return the minimumWidth
	 */
	public int getMinimumWidth() {
		return minimumWidth;
	}
}
