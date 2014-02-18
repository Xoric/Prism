package xoric.prism.client.ui.button;

import xoric.prism.client.net.INetwork;
import xoric.prism.client.ui.UIWindow;
import xoric.prism.com.ClientLoginMessage_out;
import xoric.prism.constants.Constants;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Text;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:24:16
 */
public class ActionExecuter implements IActionExecuter
{
	private final IActionParent parent;
	private final INetwork network;

	public ActionExecuter(IActionParent parent, INetwork network)
	{
		this.parent = parent;
		this.network = network;
	}

	@Override
	public void execute(UIWindow parentWindow, ButtonAction a) throws PrismException
	{
		switch (a.getActionIndex())
		{
			case EXIT_GAME:
				parent.executeExitGame();
				break;

			case LOGIN:
				executeLogin(a.getText(0), a.getText(1));
				break;

			default:
				break;
		}

		if (a.isClosingWindow())
			parent.executeCloseWindow(parentWindow);
	}

	private void executeLogin(Text acc, Text pw) throws PrismException
	{
		boolean b = Constants.checkAccountLength(acc.length());
		b &= Constants.checkPasswordLength(pw.length());

		System.out.println("login requested, acc(" + acc.length() + ")=\"" + acc + "\", pw(" + pw.length() + ")=\"" + pw + "\"");

		if (b)
		{
			ClientLoginMessage_out m = new ClientLoginMessage_out();
			m.getHeap().texts.add(acc);
			m.setPassword(pw);

			network.send(m);
		}
	}
}
