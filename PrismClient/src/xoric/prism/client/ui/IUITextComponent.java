package xoric.prism.client.ui;

import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;

public interface IUITextComponent
{
	public void setText(Text text);

	public IText_r getText();
}
