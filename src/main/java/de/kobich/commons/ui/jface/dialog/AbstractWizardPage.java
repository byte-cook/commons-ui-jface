package de.kobich.commons.ui.jface.dialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;

/**
 * Base wizard page. 
 * Calls the method onEnterPage() as soon as this page get visible.
 * @author ckorn
 */
public abstract class AbstractWizardPage extends WizardPage {
	
	protected AbstractWizardPage(String pageName) {
		super(pageName);
	}

	public abstract void onEnterPage();
	
	@Override
	public IWizardPage getNextPage() {
		IWizardPage wizardPage = super.getNextPage();
		if (wizardPage instanceof AbstractWizardPage) {
			AbstractWizardPage page = (AbstractWizardPage) wizardPage; 
			page.onEnterPage();
		}
		return wizardPage;
	}
	
	@Override
	public IWizardPage getPreviousPage() {
		IWizardPage wizardPage = super.getPreviousPage();
		if (wizardPage instanceof AbstractWizardPage) {
			AbstractWizardPage page = (AbstractWizardPage) wizardPage; 
			page.onEnterPage();
		}
		return wizardPage;
	}
}
