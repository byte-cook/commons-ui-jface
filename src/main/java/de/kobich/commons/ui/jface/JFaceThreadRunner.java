package de.kobich.commons.ui.jface;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.progress.IProgressConstants;

import de.kobich.commons.monitor.progress.IServiceProgressMonitor;
import de.kobich.commons.ui.jface.progress.ProgressMonitorAdapter;

/**
 * Abstract class for long-running operations and synchronization with the UI thread.
 * @author ckorn
 * 
 * deprecated use JFaceExec in future
 */
public abstract class JFaceThreadRunner {
	private static final Logger logger = Logger.getLogger(JFaceThreadRunner.class);
	public static enum RunningState {
		UI_1, UI_2, UI_3, UI_4, UI_ERROR, WORKER_1, WORKER_2, WORKER_3
	}
	private final String name;
	private final Shell parent;
	private final List<RunningState> states;
	private IServiceProgressMonitor progressMonitor;
	private Exception exception; 
	private RunningState nextState;
	private boolean succeeded;
	private Job job;
	
	/**
	 * Constructor
	 * @param name
	 * @param parent
	 * @param states
	 */
	public JFaceThreadRunner(String name, @Nullable Shell parent, List<RunningState> states) {
		this.name = name;
		this.parent = parent;
		this.states = states;
		this.succeeded = true;
	}
	
	/**
	 * Run this operation as background job
	 * @param delay
	 * @param user
	 * @param system
	 * @param image can be null
	 */
	public void runBackgroundJob(long delay, boolean user, boolean system, ImageDescriptor image) {
		runBackgroundJob(delay, user, system, image, null);
	}
	
	/**
	 * Run this operation as background job
	 * @param delay
	 * @param user
	 * @param system
	 * @param image can be null
	 */
	public void runBackgroundJob(long delay, boolean user, boolean system, ImageDescriptor image, final Object belongsToFamily) {
		this.job = new Job(this.name) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				runAllState(monitor);
				return Status.OK_STATUS;
			}
			
			@Override
			public boolean belongsTo(Object family) {
				return belongsToFamily != null && belongsToFamily.equals(family);
			}
		};
		job.setUser(user);
		job.setSystem(system);
		job.setProperty(IProgressConstants.ICON_PROPERTY, image);
		job.schedule(delay);
	}
	
	/**
	 * Run this operation with progress monitor dialog
	 * @param fork
	 * @param cancelable
	 */
	public void runProgressMonitorDialog(boolean fork, boolean cancelable) {
		try {
			final ProgressMonitorDialog pd = new ProgressMonitorDialog(parent);
			pd.run(fork, cancelable, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					runAllState(monitor);
				}
			});
		}
		catch (Exception e) {
			handleException(e);
		}
	}
	
	private void runAllState(IProgressMonitor monitor) {
		JFaceThreadRunner.this.progressMonitor = new ProgressMonitorAdapter(monitor);
		for (RunningState state : states) {
			if (this.nextState == null || this.nextState.equals(state)) {
				this.nextState = null;
				runState(state);
				if (!succeeded) {
					return;
				}
			}
		}
	}
	
	private void runState(final RunningState state) {
		logger.debug("Running " + state);
		switch (state) {
			case WORKER_1:
			case WORKER_2:
			case WORKER_3:
				try {
					// outside UI thread
					JFaceThreadRunner.this.run(state);
				}
				catch (Exception exc) {
					JFaceThreadRunner.this.handleException(exc);
				}
				break;
			default:
				getDisplay().syncExec(new Runnable() {
					@Override
					public void run() {
						try {
							JFaceThreadRunner.this.run(state);
						}
						catch (Exception exc) {
							JFaceThreadRunner.this.handleException(exc);
						}
					}
				});
				break;
		}
	}
	
	private void handleException(final Exception exc) {
		this.succeeded = false;
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				try {
					JFaceThreadRunner.this.exception = exc;
					JFaceThreadRunner.this.run(RunningState.UI_ERROR);
				}
				catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});
	}
	
	/**
	 * Method to override, dependent on the state run different code
	 * @param state
	 * @throws Exception
	 */
	protected abstract void run(RunningState state) throws Exception;

	/**
	 * @return the monitor
	 */
	protected IServiceProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	/**
	 * @return the exception
	 */
	protected Exception getException() {
		return exception;
	}
	
	protected Job getJob() {
		return this.job;
	}
	
	protected boolean isJobModal() {
		if (getJob() == null) {
			return false;
		}
		Boolean isModal = (Boolean) getJob().getProperty(IProgressConstants.PROPERTY_IN_DIALOG);
		return isModal != null ? isModal.booleanValue() : false;
	}
	
	protected Shell getParent() {
		return this.parent;
	}
	
	private Display getDisplay() {
		if (parent != null) {
			return parent.getDisplay();
		}
		if (Display.getCurrent() != null) {
			return Display.getCurrent();
		}
		return Display.getDefault();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param nextState the nextState to set
	 */
	public void setNextState(RunningState nextState) {
		this.nextState = nextState;
	}

	/**
	 * @return succeeded
	 */
	public boolean isSucceeded() {
		return succeeded;
	}

	/**
	 * Cancel jobs
	 * @param family
	 */
	public static void cancel(Object family) {
		IJobManager manager = Job.getJobManager();
		manager.cancel(family);
	}
	
}
