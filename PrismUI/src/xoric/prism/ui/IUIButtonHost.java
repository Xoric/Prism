package xoric.prism.ui;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Text;
import xoric.prism.ui.button.ButtonAction;

public interface IUIButtonHost
{
	public void executeAction(ButtonAction a) throws PrismException;

	public int importIntFrom(int componentIndex) throws PrismException;

	public Text importTextFrom(int componentIndex) throws PrismException;
}
