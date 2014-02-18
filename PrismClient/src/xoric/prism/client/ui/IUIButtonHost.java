package xoric.prism.client.ui;

import xoric.prism.client.ui.button.ButtonAction;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Text;

public interface IUIButtonHost
{
	public void executeAction(ButtonAction a) throws PrismException;

	public int importIntFrom(int componentIndex) throws PrismException;

	public Text importTextFrom(int componentIndex) throws PrismException;
}
