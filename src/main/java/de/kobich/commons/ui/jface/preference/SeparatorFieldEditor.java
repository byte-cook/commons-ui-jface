package de.kobich.commons.ui.jface.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.kobich.commons.ui.jface.JFaceUtils;

public class SeparatorFieldEditor extends FieldEditor {
	private Label label;
	
	/**
	 * @param label
	 */
	public SeparatorFieldEditor(Composite parent) {
		super(SeparatorFieldEditor.class.getName(), "", parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) label.getLayoutData();
		gd.horizontalSpan = numColumns;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		this.label = JFaceUtils.createHorizontalSeparator(parent, numColumns);
	}

	@Override
	protected void doLoad() {
	}

	@Override
	protected void doLoadDefault() {
	}

	@Override
	protected void doStore() {
	}

	@Override
	public int getNumberOfControls() {
		return 1;
	}

}
