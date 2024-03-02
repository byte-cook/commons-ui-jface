package de.kobich.commons.ui.jface.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class LabelFieldEditor extends FieldEditor {
	
	/**
	 * @param label
	 */
	public LabelFieldEditor(String label, Composite parent) {
		super(LabelFieldEditor.class.getName(), label, parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) getLabelControl().getLayoutData();
		gd.horizontalSpan = numColumns;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		super.getLabelControl(parent).setLayoutData(new GridData());
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
