package de.kobich.commons.ui.jface.listener;

import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DummySelectionProvider implements IPostSelectionProvider {
	public static final DummySelectionProvider INSTANCE = new DummySelectionProvider();

	@Override
	public void setSelection(ISelection selection) {
	}

	@Override
	public ISelection getSelection() {
		return null;
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener l) {
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener l) {
	}

	@Override
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
	}

	@Override
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
	}

}