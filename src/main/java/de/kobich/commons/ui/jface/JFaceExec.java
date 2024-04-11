package de.kobich.commons.ui.jface;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.progress.IProgressConstants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Execution class for long-running operations and synchronization with the UI thread.<p/>
 * Usage:<br/>
 * <code>
 * JFaceExec.builder(window.getShell(), "Name")
 * 		.worker(ctx -> {
 * 			// do something
 * 		})
 * 		.ui(ctx -> {
 * 			// do something in UI thread
 *  	})
 * 		.runProgressMonitorDialog(true, false);
 * </code>
 * @see Wrapper
 * @author ckorn
 */
public final class JFaceExec {
	private static final Logger logger = Logger.getLogger(JFaceExec.class);
	private static final BiConsumer<TaskContext, Exception> DEFAULT_EXCEPTION_HANDLER = (ctx, exc) -> {
		logger.error(exc.getMessage(), exc);
	};
	
	private final Shell parent;
	private final String name;
	private final List<TaskStep> steps;
	private BiConsumer<TaskContext, Exception> exceptionHandler;
	
	public static JFaceExec builder(Shell parent) {
		return builder(parent, "");
	}
	
	public static JFaceExec builder(Shell parent, String name) {
		return new JFaceExec(name, parent);
	}

	private JFaceExec(String name, Shell parent) {
		this.name = name;
		this.parent = parent;
		this.steps = new ArrayList<>();
		this.exceptionHandler = DEFAULT_EXCEPTION_HANDLER;
	}
	
	public JFaceExec ui(ITaskStep c) {
		this.steps.add(new TaskStep(this, TaskStep.StepType.UI, c));
		return this;
	}
	
	public JFaceExec worker(ITaskStep c) {
		this.steps.add(new TaskStep(this, TaskStep.StepType.WORKER, c));
		return this;
	}
	
	public JFaceExec exceptionally(BiConsumer<TaskContext, Exception> c) {
		exceptionHandler = c;
		return this;
	}
	
	public JFaceExec exceptionalDialog(final String message) {
		exceptionHandler = (ctx, exc) -> {
				logger.error(exc.getMessage(), exc);
				String msg = message + ": " + exc.getMessage();
				MessageDialog.openError(ctx.getParent(), ctx.getName(), msg);
		};
		return this;
	}
	
	private void runAllState(IProgressMonitor monitor) {
		final TaskContext context = new TaskContext(this.parent, this.name, monitor, getDisplay());
		for (TaskStep subTask : steps) {
			try {
				subTask.run(context);
				if (context.isCancelled()) {
					break;
				}
			}
			catch (Exception exc) {
				handleException(exc, context);
			}
		}
	}

	private void handleException(Exception exc, TaskContext context) {
		exceptionHandler.accept(context, exc);
	}

	/**
	 * Run this operation as background job
	 * @param delay
	 * @param user
	 * @param system
	 */
	public Job runBackgroundJob(long delay, boolean user, boolean system, ImageDescriptor image) {
		Job job = new Job(this.name) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				runAllState(monitor);
				return Status.OK_STATUS;
			}
		};
		job.setUser(user);
		job.setSystem(system);
		job.setProperty(IProgressConstants.ICON_PROPERTY, image);
		job.schedule(delay);
		return job;
	}

	/**
	 * Run this operation with progress monitor dialog
	 * @param fork
	 * @param cancelable
	 * @return 
	 */
	public ProgressMonitorDialog runProgressMonitorDialog(boolean fork, boolean cancelable) {
		try {
			final ProgressMonitorDialog pd = new ProgressMonitorDialog(parent);
			pd.run(fork, cancelable, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					runAllState(monitor);
				}
			});
			return pd;
		}
		catch (Exception e) {
			handleException(e, new TaskContext(this.parent, this.name, null, getDisplay()));
			return null;
		}
	}
	
	public void run() {
		runAllState(null);
	}

	private Display getDisplay() {
		if (parent != null) {
			return parent.getDisplay();
		}
		return Display.getCurrent();
	}

	@RequiredArgsConstructor
	@Getter
	public static final class TaskContext {
		private final Shell parent;
		private final String name;
		private final IProgressMonitor monitor;
		private final Display display;
		@Setter
		private boolean cancelled;
	}
	
	@FunctionalInterface
	public static interface ITaskStep {
		void runStep(TaskContext context) throws Exception;
	}
	
	@RequiredArgsConstructor
	@Getter
	private static class TaskStep {
		private enum StepType { UI, WORKER }
		private final JFaceExec exec;
		private final StepType type;
		private final ITaskStep consumer;
		
		public void run(TaskContext context) throws Exception {
			switch (getType()) {
				case WORKER:
					// outside UI thread
					consumer.runStep(context);
					break;
				case UI:
					// within UI thread
					context.getDisplay().syncExec(new Runnable() {
						@Override
						public void run() {
							try {
								consumer.runStep(context);
							}
							catch (Exception exc) {
								exec.handleException(exc, context);
							}
						}
					});
					break;
			}
		}
	}
}