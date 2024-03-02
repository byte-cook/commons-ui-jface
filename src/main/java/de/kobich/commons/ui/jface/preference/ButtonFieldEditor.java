package de.kobich.commons.ui.jface.preference;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ButtonFieldEditor extends FieldEditor {
	private Button button;
	
	public ButtonFieldEditor(String label, Composite parent) {
		super(ButtonFieldEditor.class.getName(), label, parent);
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) button.getLayoutData();
		gd.horizontalSpan = numColumns;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		this.button = new Button(parent, SWT.NONE);
		this.button.setText(getLabelText());
		this.button.setLayoutData(new GridData());
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
	
	public void addSelectionListener(SelectionListener l) {
		this.button.addSelectionListener(l);
	}

	/**
	 * @return the button
	 */
	public Button getButton() {
		return button;
	}

}
