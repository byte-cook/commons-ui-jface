package de.kobich.commons.ui.jface.memento;

import java.io.File;


public class FileListComboSerializer extends ComboSerializer {
	private final boolean forceExists;
	public static enum FileType {
		FILE, DIRECTORY, BOTH
	}
	private final FileType forceType; 

	public FileListComboSerializer(String state, String defaultValue, boolean forceExists, FileType forceType) {
		super(state, defaultValue);
		this.forceExists = forceExists; 
		this.forceType = forceType;
	}

	@Override
	protected boolean isValid(String path) {
		boolean exists = !forceExists || new File(path).exists();
		boolean directory = !FileType.DIRECTORY.equals(forceType) || new File(path).isDirectory();
		boolean file = !FileType.FILE.equals(forceType) || new File(path).isFile();
		return exists && directory && file;
	}
}
