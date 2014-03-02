package xoric.prism.creator.common.spritelist.tools;

import xoric.prism.swing.input.BooleanInputPanel;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.tooltips.ToolTipFormatter;

public class HotspotsPanel extends BooleanInputPanel
{
	private static final long serialVersionUID = 1L;

	public HotspotsPanel(IValueInputListener listener)
	{
		super("Use hotspots and action points", listener);
		this.setPrompt("Choose whether or not to use hotspots and action points.");
		this.setToolTipText(ToolTipFormatter
				.split("Hotspots are used to manipulate the individual placement of sprites when drawing. Action points are used to mark limbs etc."));
	}
}
