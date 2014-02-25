package xoric.prism.creator.windows.control;

import xoric.prism.ui.UIComponent;
import xoric.prism.ui.UIIdentifier;

public interface ISceneControl
{
	public void requestAddComponent(UIComponent c);

	public void requestAddComponent(UIIdentifier id);

	public void requestDeleteComponent();

	public void requestDeleteComponent(UIComponent c);
}
