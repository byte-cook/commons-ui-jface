package de.kobich.commons.ui.jface.dialog;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.kobich.commons.ui.IUserInputValidator;
import de.kobich.commons.ui.jface.JFaceUtils;
import de.kobich.commons.ui.jface.memento.ComboSerializer;
import de.kobich.commons.ui.jface.memento.DialogSettingsAdapter;
import de.kobich.commons.ui.memento.IMementoItem;

public class FileInputDialog extends TitleAreaDialog {
	private static final String IMPORT_DIR_STATE = "importDir";
	private IUserInputValidator validator;
	private Combo fileCombo;
	private File file;
	private String title;
	private String message;
	private String label;
	private String browse;
	private IDialogSettings settings;

	public FileInputDialog(Shell shell) {
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell shell) {
        super.configureShell(shell);
		shell.setText(title);
        shell.setImage(getParentShell().getImage());
    }

	protected Control createDialogArea(Composite parent) {
		super.setTitle(title);
        super.setMessage(message);
        JFaceUtils.createHorizontalSeparator(parent, 1);
        
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // label
        Label fileLabel = new Label(composite, SWT.WRAP);
        fileLabel.setText(label);
        GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
        labelData.horizontalSpan = 2;
        labelData.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH);
        fileLabel.setLayoutData(labelData);
        fileLabel.setFont(parent.getFont());
        
        // value
        int keyStyle = SWT.BORDER | SWT.SINGLE;
        GridData fileData = new GridData(GridData.FILL_HORIZONTAL);
        fileData.widthHint = 450;
        fileCombo = new Combo(composite, keyStyle);
        fileCombo.setLayoutData(fileData);
        
    	Button browseButton = new Button(composite, SWT.NONE);
    	browseButton.setText(browse);
    	browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String[] filterNames = new String[] { "XML File", "ZIP Files", "All Files" };
				String[] filterExtensions = new String[] { "*.xml", "*.zip", "*.*" };
				String filterPath = "";
				if (!StringUtils.isBlank(fileCombo.getText())) {
					File file = new File(fileCombo.getText());
					if (file.isFile()) {
						filterPath = file.getParent();
					}
					else{
						filterPath = file.getAbsolutePath();
					}
				}
				
				FileDialog dialog = new FileDialog(FileInputDialog.this.getShell(), SWT.OPEN);
				dialog.setFilterPath(filterPath);
				dialog.setFilterNames(filterNames);
				dialog.setFilterExtensions(filterExtensions);
				String path = dialog.open();
				if (path != null) {
					fileCombo.add(path);
					fileCombo.setText(path);
				}
			} 
    	});

        JFaceUtils.createHorizontalSeparator(parent, 1);

        // restore state
		IDialogSettings settings = getDialogBoundsSettings();
		IMementoItem mementoItem = new DialogSettingsAdapter(settings);

		ComboSerializer fileMemento = new ComboSerializer(IMPORT_DIR_STATE, System.getProperty("user.dir"));
		fileMemento.restore(fileCombo, mementoItem);

        return composite;
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		String errorMessage = null;
		if (validator != null) {
            errorMessage = validator.isValid(fileCombo.getText());
        }
		if (errorMessage != null) {
			setErrorMessage(errorMessage);
			return;
		}
		file = new File(fileCombo.getText());
		
		IDialogSettings settings = getDialogBoundsSettings();
		IMementoItem mementoItem = new DialogSettingsAdapter(settings);
		
		ComboSerializer fileMemento = new ComboSerializer(IMPORT_DIR_STATE, System.getProperty("user.dir"));
		fileMemento.save(fileCombo, mementoItem);
		
		super.okPressed();
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#getDialogBoundsSettings()
	 */
	@Override
	public IDialogSettings getDialogBoundsSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setDialogBoundsSettings(IDialogSettings settings) {
		this.settings = settings;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @param browse the browse to set
	 */
	public void setBrowse(String browse) {
		this.browse = browse;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @param validator the validator to set
	 */
	public void setValidator(IUserInputValidator validator) {
		this.validator = validator;
	}
}
