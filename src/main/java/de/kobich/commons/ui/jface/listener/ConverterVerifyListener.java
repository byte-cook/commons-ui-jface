package de.kobich.commons.ui.jface.listener;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

import de.kobich.commons.converter.IConverter;

/**
 * Verifies input by using converters. 
 * @author ckorn
 *
 * @param <T> the allowed type
 */
public class ConverterVerifyListener<T> implements VerifyListener {
	private final IConverter<T, String> converter;
	private final Text text;
	
	/**
	 * @param converter
	 * @param text
	 */
	public ConverterVerifyListener(Text text, IConverter<T, String> converter) {
		this.converter = converter;
		this.text = text;
	}

	@Override
	public void verifyText(VerifyEvent e) {
		StringBuffer buffer = new StringBuffer(text.getText());
		buffer.insert(e.start, e.text);
		T t = this.converter.reconvert(buffer.toString());
		if (t == null) {
			e.doit = false;
		}
	}
}
