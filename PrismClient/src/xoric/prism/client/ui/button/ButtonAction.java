package xoric.prism.client.ui.button;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.ui.IUIButtonHost;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.types.Text;

public class ButtonAction
{
	private ButtonActionIndex index;

	private final List<Integer> ints;
	private final List<Text> texts;

	private boolean isClosingWindow;

	public ButtonAction(ButtonActionIndex index, boolean isClosingWindow)
	{
		this.index = index;
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
			e.code.addInfo(ButtonActionIndex.class.getSimpleName(), this.index.toString());
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
			e.code.addInfo(ButtonActionIndex.class.getSimpleName(), this.index.toString());
			throw e;
		}
		return texts.get(index);
	}

	public boolean isClosingWindow()
	{
		return isClosingWindow;
	}

	public void setActionIndex(ButtonActionIndex index)
	{
		this.index = index;
	}

	public ButtonActionIndex getActionIndex()
	{
		return index;
	}

	public void importData(IUIButtonHost inputHub, ButtonActionMask mask) throws PrismException
	{
		for (int i : mask.getIntSources())
			ints.add(inputHub.importIntFrom(i));

		for (int i : mask.getTextSources())
			texts.add(inputHub.importTextFrom(i));
	}
}
