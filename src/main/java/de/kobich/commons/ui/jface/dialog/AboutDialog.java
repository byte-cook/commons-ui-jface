package de.kobich.commons.ui.jface.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.kobich.commons.ui.jface.JFaceUtils;

public class AboutDialog extends TrayDialog {
	private String title;
	private Image applicationImage;
	private String applicationName;
	private String applicationVersion;
	private String applicationCopyright;
	private IDialogSettings settings;

	public AboutDialog(Shell shell) {
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
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // image
        Canvas canvas = new Canvas (composite, SWT.NONE);
    	canvas.addPaintListener (new PaintListener () {
    		public void paintControl (PaintEvent e) {
    			e.gc.drawImage(applicationImage, 0, 0);
    		}
    	});
    	GridData canvasData = new GridData(GridData.FILL_VERTICAL);
    	canvasData.heightHint = this.applicationImage.getBounds().height;
    	canvasData.widthHint = this.applicationImage.getBounds().width;
    	canvasData.verticalAlignment = SWT.CENTER;
    	canvas.setLayoutData(canvasData);
        
        // label
    	Group group = new Group(composite, SWT.FILL);
        GridData groupData = new GridData(GridData.FILL_BOTH);
        group.setLayoutData(groupData);
        group.setLayout(new GridLayout());
    	
        Label nameLabel = new Label(group, SWT.WRAP);
        nameLabel.setText(applicationName);
        nameLabel.setFont(parent.getFont());
        Label versionLabel = new Label(group, SWT.WRAP);
        versionLabel.setText(applicationVersion);
        versionLabel.setFont(parent.getFont());
        new Label(group, SWT.WRAP);
        Label copyrightLabel = new Label(group, SWT.WRAP);
        copyrightLabel.setText(applicationCopyright);
        copyrightLabel.setFont(parent.getFont());
        
        JFaceUtils.createHorizontalSeparator(parent, 1);

        return composite;
    }
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
        Button b = createButton(parent, IDialogConstants.OK_ID,
                IDialogConstants.OK_LABEL, true);
        b.setFocus();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();
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
		this.applicationName = label;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param applicationImage the applicationImage to set
	 */
	public void setApplicationImage(Image applicationImage) {
		this.applicationImage = applicationImage;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @param applicationVersion the applicationVersion to set
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	/**
	 * @param applicationCopyright the applicationCopyright to set
	 */
	public void setApplicationCopyright(String applicationCopyright) {
		this.applicationCopyright = applicationCopyright;
	}
}
