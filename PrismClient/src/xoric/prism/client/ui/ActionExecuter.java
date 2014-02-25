package xoric.prism.client.ui;

import xoric.prism.client.net.INetwork;
import xoric.prism.com.ClientLoginMessage_out;
import xoric.prism.constants.Constants;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.Prism;
import xoric.prism.data.types.Text;
import xoric.prism.ui.UIWindow;
import xoric.prism.ui.action.IActionExecuter;
import xoric.prism.ui.button.ButtonAction;
import xoric.prism.ui.button.IActionParent;

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
		ClientActionCommand c = ClientActionCommand.valueOf(a.getActionCommand());

		switch (c)
		{
			case EXIT_GAME:
				parent.executeExitGame();
				break;

			case LOGIN:
				executeLogin(a.getText(0), a.getText(1));
				break;

			case CREATE_ACC:
				executeCreateAcc(a.getText(0), a.getText(1), a.getText(2), a.getText(3));
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

		b = true; // TODO (for debugging) always send

		if (b)
		{
			ClientLoginMessage_out m = new ClientLoginMessage_out();
			m.setPassword(pw);
			m.getHeap().texts.add(acc);
			m.getHeap().ints.addAll(Prism.global.getVersionHeap().ints);
			network.send(m);
		}
	}

	private void executeCreateAcc(Text acc, Text pw1, Text pw2, Text email) throws PrismException
	{
		System.out.println("Create account: acc=" + acc + ", pw1=" + pw1 + ", pw2=" + pw2 + ", email=" + email);
	}
}
