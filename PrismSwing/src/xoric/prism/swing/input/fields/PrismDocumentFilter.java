package xoric.prism.swing.input.fields;

import javax.swing.text.DocumentFilter;

abstract class PrismDocumentFilter extends DocumentFilter
{
	private final Object owner;
	private IInputListener listener;

	public PrismDocumentFilter(Object owner)
	{
		this.owner = owner;
	}

	public void setListener(IInputListener listener)
	{
		this.listener = listener;
	}

	protected void notifyListener()
	{
		if (listener != null)
			listener.changedInput(owner);
	}
}
