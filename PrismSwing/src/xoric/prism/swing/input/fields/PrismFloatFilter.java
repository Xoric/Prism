package xoric.prism.swing.input.fields;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

class PrismFloatFilter extends PrismDocumentFilter
{
	private IValueController controller;

	public PrismFloatFilter(IValueController owner)
	{
		super(owner);

		controller = owner;
	}

	@Override
	public void insertString(FilterBypass fb, int offset, String s, AttributeSet attr) throws BadLocationException
	{
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.insert(offset, s);

		if (test(sb.toString()))
		{
			super.insertString(fb, offset, s, attr);
			controller.controlValue();
			notifyListener();
		} // else: ignore input
	}

	private boolean test(String s)
	{
		try
		{
			if (s.length() > 0)
				Float.parseFloat(s);
			return true;
		}
		catch (NumberFormatException e)
		{
			return false;
		}
	}

	@Override
	public void replace(FilterBypass fb, int offset, int length, String s, AttributeSet attrs) throws BadLocationException
	{
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.replace(offset, offset + length, s);

		if (test(sb.toString()))
		{
			super.replace(fb, offset, length, s, attrs);
			controller.controlValue();
			notifyListener();
		} // else: ignore input
	}

	@Override
	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException
	{
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.delete(offset, offset + length);

		if (test(sb.toString()))
		{
			super.remove(fb, offset, length);
			controller.controlValue();
			notifyListener();
		} // else: ignore input
	}
}
