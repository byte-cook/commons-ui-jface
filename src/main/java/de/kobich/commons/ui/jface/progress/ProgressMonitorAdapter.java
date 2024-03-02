package de.kobich.commons.ui.jface.progress;

import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;

import de.kobich.commons.monitor.progress.IServiceProgressMonitor;
import de.kobich.commons.monitor.progress.ProgressData;

public class ProgressMonitorAdapter implements IServiceProgressMonitor {
	private final IProgressMonitor monitor;
	private final Stack<ProgressData> tasks;

	/**
	 * Constructor
	 */
	public ProgressMonitorAdapter(IProgressMonitor monitor) {
		this.monitor = monitor;
		this.tasks = new Stack<ProgressData>();
	}

	@Override
	public void beginTask(ProgressData data) {
		if (tasks.isEmpty()) {
			monitor.beginTask(data.getMessage(), data.getCount());
		}
		tasks.push(data);
	}

	@Override
	public void endTask(ProgressData data) {
		if (!tasks.isEmpty()) {
			tasks.pop();
		}
		if (tasks.isEmpty()) {
			monitor.done();
		}
	}

	@Override
	public boolean isCanceled() {
		return monitor.isCanceled();
	}

	@Override
	public void setCanceled(boolean value) {
		monitor.setCanceled(value);
	}

	@Override
	public void worked(int work) {
		monitor.worked(work);
	}

	@Override
	public void subTask(ProgressData data) {
		String subTaskMessage = data.getMessage();
		monitor.subTask(subTaskMessage);
		
		if (!data.isIndeterminate()) {
			monitor.worked(data.getCount());
		}
	}
	
	/**
	 * Returns if nested tasks are available
	 * @return
	 */
	protected boolean hasNestedTasks() {
		return !tasks.isEmpty();
	}

}
