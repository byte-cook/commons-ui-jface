package de.kobich.commons.ui.jface.state;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;

import de.kobich.commons.ui.state.ControlStateRepository;

public class ControlStateSelectionChangedListener implements ISelectionChangedListener {
	private final ControlStateRepository stateRepository;
	
	public ControlStateSelectionChangedListener(ControlStateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		StructuredSelection selection = (StructuredSelection) event.getSelection();
		
		if (selection.isEmpty()) {
			stateRepository.fireNoItemSelected();
		}
		else if (selection.size() == 1) {
			stateRepository.fireSingleItemSelected();
		}
		else {
			stateRepository.fireMultipleItemsSelected();
		}
	}
}
