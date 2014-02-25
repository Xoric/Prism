package xoric.prism.ui.button;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Text;
import xoric.prism.ui.IUIButtonHost;

public class ButtonAction
{
	private Text command;

	private final List<Integer> ints;
	private final List<Text> texts;

	private boolean isClosingWindow;

	public ButtonAction(Text command, boolean isClosingWindow)
	{
		this.command = command;
		this.ints = new ArrayList<Integer>();
		this.texts = new ArrayList<Text>();
		this.isClosingWindow = isClosingWindow;
	}

	public int getInt(int index) throws PrismException
	{
		if (index < 0 || index >= ints.size())
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("int index (" + index + ") out of bounds in " + ButtonAction.class.getSimpleName());
			e.code.addInfo("available ints", ints.size());
			e.code.addInfo("command", this.command.toString());
			throw e;
		}
		return ints.get(index);
	}

	public Text getText(int index) throws PrismException
	{
		if (index < 0 || index >= texts.size())
		{
			PrismException e = new PrismException();
			e.user.setText(UserErrorText.INTERNAL_PROBLEM);
			e.code.setText("text index (" + index + ") out of bounds in " + ButtonAction.class.getSimpleName());
			e.code.addInfo("available texts", texts.size());
			e.code.addInfo("command", this.command.toString());
			throw e;
		}
		return texts.get(index);
	}

	public boolean isClosingWindow()
	{
		return isClosingWindow;
	}

	public void setActionCommand(Text command)
	{
		this.command = command;
	}

	public IText_r getActionCommand()
	{
		return command;
	}

	public void importData(IUIButtonHost inputHub, ButtonActionMask mask) throws PrismException
	{
		for (int i : mask.getIntSources())
			ints.add(inputHub.importIntFrom(i));

		for (int i : mask.getTextSources())
			texts.add(inputHub.importTextFrom(i));
	}
}
