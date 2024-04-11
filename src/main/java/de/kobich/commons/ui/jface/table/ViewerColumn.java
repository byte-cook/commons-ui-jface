package de.kobich.commons.ui.jface.table;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ViewerColumn {
	private final String name;
	private final int widthPercent;
	private final int minimumWidth;
	
	public ViewerColumn(String name, int widthPercent) {
		this(name, widthPercent, 20);
	}
	
}
