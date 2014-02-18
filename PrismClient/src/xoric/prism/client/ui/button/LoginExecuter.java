package xoric.prism.client.ui.button;

import java.util.List;

import xoric.prism.data.types.Text;

/**
 * @author XoricLee
 * @since 18.02.2014, 12:21:04
 */
abstract class LoginExecuter
{
	public static void execute(List<Text> texts)
	{
		Text account = texts.get(0);
		Text password = texts.get(1);

		System.out.println("login acc=" + account + ", password=" + password);
	}
}
