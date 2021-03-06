package xoric.prism.creator.models.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import xoric.prism.creator.models.control.IMainControl;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.swing.PrismPanel;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.PointInputPanel;
import xoric.prism.swing.input.TextInputPanel;
import xoric.prism.swing.input.ValueInputPanel;
import xoric.prism.swing.tooltips.ToolTipFormatter;

class ModelTable extends PrismPanel implements IModelTable, IValueInputListener
{
	private static final long serialVersionUID = 1L;

	private IMainControl control;

	private TextInputPanel nameInput;
	private PointInputPanel spriteSizeInput;

	public ModelTable()
	{
		super("Settings");

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEtchedBorder());

		nameInput = new TextInputPanel("Name", this);
		nameInput.setValue(new Text("NONE"));
		nameInput.setPrompt("Enter a new name for this model.");
		nameInput.setToolTipText(ToolTipFormatter.split("Provide a name for this model."));

		spriteSizeInput = new PointInputPanel("Sprite size", this);
		spriteSizeInput.setUnit("px");
		spriteSizeInput.setValue(new Point(0, 0));
		spriteSizeInput.setLabels("Width", "Height");
		spriteSizeInput.setPrompt("Enter new sprite size.");
		spriteSizeInput.setToolTipText(ToolTipFormatter
				.split("Size of one sprite. This is the width and height of one tile in the model's images."));

		Insets insets = new Insets(0, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0);
		contentPane.add(nameInput, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		contentPane.add(new JSeparator(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		contentPane.add(spriteSizeInput, c);

		setEnabled(false);
		setContent(contentPane);
	}

	public void setControl(IMainControl control)
	{
		this.control = control;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		//		super.setEnabled(enabled);

		nameInput.setEnabled(enabled);
		spriteSizeInput.setEnabled(enabled);
	}

	@Override
	public void notifyValueChanged(ValueInputPanel input)
	{
		if (input == nameInput)
		{
			control.requestSetName(nameInput.getValue());
		}
		else if (input == spriteSizeInput)
		{
			control.requestResizeSprites(spriteSizeInput.getValue());
		}
	}

	@Override
	public void displayName(IText_r name)
	{
		nameInput.setEnabled(name != null);
		nameInput.setValue(name);
	}

	@Override
	public void displaySpriteSize(IPoint_r spriteSize)
	{
		spriteSizeInput.setEnabled(spriteSize != null);
		spriteSizeInput.setValue(spriteSize);
	}
}
