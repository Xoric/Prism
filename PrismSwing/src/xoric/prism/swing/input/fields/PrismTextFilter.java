package xoric.prism.swing.input.fields;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import xoric.prism.data.types.TextMap;

public class PrismTextFilter extends PrismDocumentFilter
{
	public PrismTextFilter(Object owner)
	{
		super(owner);
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException
	{
		string = TextMap.convertString(string);
		super.insertString(fb, offset, string, attr);
		notifyListener();
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws BadLocationException
	{
		string = TextMap.convertString(string);
		super.replace(fb, offset, length, string, attrs);
		notifyListener();
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		super.remove(fb, offset, length);
		notifyListener();
	}
}
