package xoric.prism.ui.action;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.ui.UIWindow;
import xoric.prism.ui.button.ButtonAction;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:30:37
 */
public interface IActionExecuter
{
	public void execute(UIWindow parentWindow, ButtonAction a) throws PrismException;
}
