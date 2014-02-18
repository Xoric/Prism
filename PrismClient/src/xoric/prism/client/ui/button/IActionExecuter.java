package xoric.prism.client.ui.button;

import xoric.prism.client.ui.UIWindow;
import xoric.prism.data.exceptions.PrismException;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:30:37
 */
public interface IActionExecuter
{
	public void execute(UIWindow parentWindow, ButtonAction a) throws PrismException;
}
