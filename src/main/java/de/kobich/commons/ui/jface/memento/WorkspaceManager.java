package de.kobich.commons.ui.jface.memento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IDialogSettings;

import de.kobich.commons.utils.StreamUtils;

public class WorkspaceManager {
	private static final Logger logger = Logger.getLogger(WorkspaceManager.class);
	private static final String CHARSET = "UTF-8";
	private IDialogSettings dialogSettings;
	private final File workspaceFile;

	public WorkspaceManager(File workspaceFile) {
		this.workspaceFile = workspaceFile;
	}

	public IDialogSettings getSettings() {
		if (dialogSettings == null) {
			dialogSettings = loadDialogSettings();
		}
		return dialogSettings;
	}
	
	public void saveSettings() {
		FileOutputStream os = null;
		BufferedWriter writer = null;
        try {
        	os = new FileOutputStream(workspaceFile);
        	writer = new BufferedWriter(new OutputStreamWriter(os, CHARSET));
        	getSettings().save(writer);
        } catch (IOException e) {
			logger.warn(e.getMessage(), e);
        }
        finally {
        	StreamUtils.forceClose(writer);
        	StreamUtils.forceClose(os);
        }
    }
	
	private IDialogSettings loadDialogSettings() {
		IDialogSettings settings = new DialogSettings("Workbench");
		FileInputStream is = null;
		BufferedReader reader = null;
		try {
			is = new FileInputStream(workspaceFile);
			reader = new BufferedReader(
                    new InputStreamReader(is, CHARSET));
			settings.load(reader);
		}
		catch (IOException e) {
			settings = new DialogSettings("Workbench");
		}
		finally {
			StreamUtils.forceClose(reader);
			StreamUtils.forceClose(is);
		}
		return settings;
	}
}
