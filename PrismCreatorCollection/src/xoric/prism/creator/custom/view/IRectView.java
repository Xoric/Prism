package xoric.prism.creator.custom.view;

import xoric.prism.creator.common.spritelist.control.SpriteNameGenerator;
import xoric.prism.creator.custom.model.ObjectModel;

public interface IRectView
{
	public void setControl(IRectControl control);

	public void displayObject(ObjectModel model, SpriteNameGenerator spriteNameGenerator);

	public void clear();

	public void enableControls();
}
