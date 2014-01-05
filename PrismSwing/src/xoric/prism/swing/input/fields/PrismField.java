package xoric.prism.swing.input.fields;

import javax.swing.JTextField;

abstract class PrismField extends JTextField
{
	private static final long serialVersionUID = 1L;

	protected PrismDocumentFilter filter;

	public PrismField(String s)
	{
		super(s);
	}

	public void setInputListener(IInputListener listener)
	{
		filter.setListener(listener);
	}
}
