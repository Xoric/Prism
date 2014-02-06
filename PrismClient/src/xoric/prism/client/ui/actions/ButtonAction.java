package xoric.prism.client.ui.actions;

import java.util.ArrayList;
import java.util.List;

import xoric.prism.client.ui.IUIButtonHost;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.Text;

public class ButtonAction
{
	private ButtonActionIndex index;

	private final List<Integer> ints;
	private final List<Text> texts;

	public ButtonAction(ButtonActionIndex index)
	{
		this.index = index;
		this.ints = new ArrayList<Integer>();
		this.texts = new ArrayList<Text>();
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

		for (int i : mask.getIntSources())
			texts.add(inputHub.importTextFrom(i));
	}

	public void execute()
	{

	}
}
