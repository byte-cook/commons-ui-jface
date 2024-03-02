package de.kobich.commons.ui.jface;


import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public enum JFaceUtils {
	MARGIN_TOP, MARGIN_BOTTOM, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_WIDTH, MARGIN_HEIGHT
	;
	
	/**
	 * Default spacing between two components within the same layout
	 */
	private static final int SPACING = 5;
	/**
	 * Default view margin between the layout and its edges
	 */
	private static final int MARGIN_VIEW = 3;
	/**
	 * Default dialog margin between the layout and its edges
	 */
	private static final int MARGIN_DIALOG = 5;
	/**
	 * Property for Spinner widget which is too high
	 */
	public static final String PROP_ADJUST_HEIGHT_ENABLED = "adjustHeightEnabled";

	public static GridLayout createViewGridLayout(int numColumns, boolean makeColumnsEqualWidth, JFaceUtils... types) {
		List<JFaceUtils> typeList = Arrays.asList(types);
		GridLayout gridLayout = new GridLayout(numColumns, makeColumnsEqualWidth);
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = SPACING;
		gridLayout.marginWidth = typeList.contains(JFaceUtils.MARGIN_WIDTH) ? MARGIN_VIEW : 0;
		gridLayout.marginLeft = typeList.contains(JFaceUtils.MARGIN_LEFT) ? MARGIN_VIEW : 0;
		gridLayout.marginRight = typeList.contains(JFaceUtils.MARGIN_RIGHT) ? MARGIN_VIEW : 0;
		gridLayout.marginHeight = typeList.contains(JFaceUtils.MARGIN_HEIGHT) ? MARGIN_VIEW : 0;
		gridLayout.marginBottom = typeList.contains(JFaceUtils.MARGIN_BOTTOM) ? MARGIN_VIEW : 0;
		gridLayout.marginTop = typeList.contains(JFaceUtils.MARGIN_TOP) ? MARGIN_VIEW : 0;
		return gridLayout;
	}
	
	public static GridLayout createDialogGridLayout(int numColumns, boolean makeColumnsEqualWidth, JFaceUtils... types) {
		List<JFaceUtils> typeList = Arrays.asList(types);
		GridLayout gridLayout = new GridLayout(numColumns, makeColumnsEqualWidth);
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = SPACING;
		gridLayout.marginWidth = typeList.contains(JFaceUtils.MARGIN_WIDTH) ? MARGIN_DIALOG : 0;
		gridLayout.marginLeft = typeList.contains(JFaceUtils.MARGIN_LEFT) ? MARGIN_DIALOG : 0;
		gridLayout.marginRight = typeList.contains(JFaceUtils.MARGIN_RIGHT) ? MARGIN_DIALOG : 0;
		gridLayout.marginHeight = typeList.contains(JFaceUtils.MARGIN_HEIGHT) ? MARGIN_DIALOG : 0;
		gridLayout.marginBottom = typeList.contains(JFaceUtils.MARGIN_BOTTOM) ? MARGIN_DIALOG : 0;
		gridLayout.marginTop = typeList.contains(JFaceUtils.MARGIN_TOP) ? MARGIN_DIALOG : 0;
		return gridLayout;
	}

	@Deprecated
	public static GridLayout createGridLayoutWithoutSpace(int numColumns) {
		GridLayout l = new GridLayout();
		l.numColumns = numColumns;
		l.marginLeft = 0;
		l.marginRight = 0;
		l.marginTop = 0;
		l.marginBottom = 0;
		l.marginWidth = 0;
		l.marginHeight = 0;
		return l;
	}
	
	@Deprecated
	public static GridLayout createGridLayoutWithSymmetricSpace(int numColumns, int marginLeftRight, int marginBottomTop, int marginHeightWidth) {
		GridLayout l = new GridLayout();
		l.numColumns = numColumns;
		l.marginLeft = l.marginRight = marginLeftRight;
		l.marginBottom = l.marginTop = marginBottomTop;
		l.marginHeight = l.marginWidth = marginHeightWidth;
		return l;
	}
	
	public static GridData createGridDataWithSpan(int style, int horizontalSpan, int verticalSpan) {
		GridData d = new GridData(style);
		d.horizontalSpan = horizontalSpan;
		d.verticalSpan = verticalSpan;
		return d;
	}
	
	public static GridData createGridDataWithWidth(int style, int widthHint) {
		GridData d = new GridData(style);
		d.widthHint = widthHint;
		return d;
	}

	/**
	 * Sets the same height as for Text widgets.
	 * WORKAROUND: Under Linux, some widgets have the wrong height (e.g. Spinner) 
	 * @param gd
	 * @return
	 */
	public static GridData adjustGridDataTextHeight(GridData gd) {
		if (Boolean.valueOf(System.getProperty(PROP_ADJUST_HEIGHT_ENABLED))) {
			gd.heightHint = 29;
		}
		return gd;
	}

	public static Point calculateTextSize(Label l, String text) {
		GC gc = new GC(l);
		try {
			return gc.textExtent(text);
		}
		finally {
			gc.dispose();
		}
	}
	
	public static Label createHorizontalSeparator(Composite parent, int horizontalSpan) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(JFaceUtils.createGridDataWithSpan(GridData.FILL_HORIZONTAL, horizontalSpan, 1));
		return separator;
	}
	
	public static Color getDisabledTextBackgroundColor() {
		return Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	}
	
	public static Color getInfoTextForegroundColor() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		return shell.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
	}
	
	public static ImageData scaleImageData(ImageData data, int maxWidth, int maxHeight) {
		int scaleWidth = maxWidth;
		int scaleHeight = maxHeight;
		
		double wScale = data.width / (double) maxWidth;
		double hScale = data.height / (double) maxHeight;
		if (wScale <= 1 && hScale <= 1) {
			scaleWidth = data.width;
			scaleHeight = data.height;
		}
		else {
			if (wScale > hScale) {
				scaleWidth = (int) (data.width / wScale);
				scaleHeight = (int) (data.height / wScale);
			}
			else {
				scaleWidth = (int) (data.width / hScale);
				scaleHeight = (int) (data.height / hScale);
			}
		}
		return data.scaledTo(scaleWidth, scaleHeight);
	}
}
