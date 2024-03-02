package de.kobich.commons.ui.jface.listener;

import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;

import de.kobich.commons.ListenerList;

/**
 * IPostSelectionProvider implementation that delegates to another
 * ISelectionProvider or IPostSelectionProvider. The selection provider used for
 * delegation can be exchanged dynamically. Registered listeners are adjusted
 * accordingly. This utility class may be used in workbench parts with multiple
 * viewers.
 * 
 * @author ckorn
 */
public class SelectionProviderIntermediate implements IPostSelectionProvider {
	private static final ISelectionProvider DUMMY = new DummySelectionProvider();
	private final ListenerList<ISelectionChangedListener> selectionListeners;
	private final ListenerList<ISelectionChangedListener> postSelectionListeners;
	private ISelectionProvider delegate;

	public SelectionProviderIntermediate() {
		this.selectionListeners = new ListenerList<ISelectionChangedListener>();
		this.postSelectionListeners = new ListenerList<ISelectionChangedListener>();
		this.delegate = DUMMY;
	}

	public void setSelectionProviderDelegate(ISelectionProvider newDelegate) {
		if (this.delegate == newDelegate) {
			return;
		}
		ISelectionProvider oldDelegate = this.delegate;
		this.delegate = newDelegate == null ? DUMMY : newDelegate;

		// old delegate
		for (ISelectionChangedListener l : selectionListeners) {
			oldDelegate.removeSelectionChangedListener(l);
		}
		if (oldDelegate instanceof IPostSelectionProvider) {
			for (ISelectionChangedListener l : postSelectionListeners) {
				((IPostSelectionProvider) this.delegate).removePostSelectionChangedListener(l);
			}
		}

		// new delegate
		for (ISelectionChangedListener l : selectionListeners) {
			this.delegate.addSelectionChangedListener(l);
		}
		if (oldDelegate instanceof IPostSelectionProvider) {
			for (ISelectionChangedListener l : postSelectionListeners) {
				((IPostSelectionProvider) this.delegate).addPostSelectionChangedListener(l);
			}
		}
	}

	@Override
	public ISelection getSelection() {
		return this.delegate.getSelection();
	}

	@Override
	public void setSelection(ISelection selection) {
		this.delegate.setSelection(selection);
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener l) {
		this.selectionListeners.addListener(l);
		this.delegate.addSelectionChangedListener(l);
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener l) {
		this.selectionListeners.removeListener(l);
		this.delegate.removeSelectionChangedListener(l);
	}

	@Override
	public void addPostSelectionChangedListener(ISelectionChangedListener l) {
		this.postSelectionListeners.addListener(l);
		if (this.delegate instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) this.delegate).addPostSelectionChangedListener(l);
		}
	}

	@Override
	public void removePostSelectionChangedListener(ISelectionChangedListener l) {
		this.postSelectionListeners.removeListener(l);
		if (this.delegate instanceof IPostSelectionProvider) {
			((IPostSelectionProvider) this.delegate).removePostSelectionChangedListener(l);
		}
	}

	private static final class DummySelectionProvider implements ISelectionProvider {

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

	};

}
