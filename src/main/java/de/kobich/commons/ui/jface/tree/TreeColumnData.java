package de.kobich.commons.ui.jface.tree;

import javax.annotation.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
public final class TreeColumnData {
	/**
	 * the object representing this column or null
	 */
	@Nullable
	private final Object element;

	private final String text;

	/**
	 * Returns the share of the width for this column. 
	 * Examples:
	 * 1,1 means 50%,50%
	 * 2,3 means 40%,60%
	 * 1,2,1 means 25%,50%,25%
	 */
	private final int widthShare;
	
	private final int minimumWidth;

	/**
	 * Indicates if this column can be set to hidden (visible=false)
	 */
	private final boolean hideable;

	@Setter
	private boolean visible;

	public String toString() {
		return getText();
	}
}
