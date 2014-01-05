package xoric.prism.swing.input.fields;

import javax.swing.text.AbstractDocument;

public class PrismTextField extends PrismField
{
	private static final long serialVersionUID = 1L;

	public PrismTextField(String s)
	{
		super(s);

		filter = new PrismTextFilter(this);
		((AbstractDocument) (this.getDocument())).setDocumentFilter(filter);
	}
}
