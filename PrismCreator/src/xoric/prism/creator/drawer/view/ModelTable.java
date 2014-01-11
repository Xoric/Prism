package xoric.prism.creator.drawer.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import xoric.prism.creator.drawer.control.IDrawerControl;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.swing.PrismPanel;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.PointInput;
import xoric.prism.swing.input.TextInput;
import xoric.prism.swing.input.ValueInput;

class ModelTable extends PrismPanel implements IModelTable, IValueInputListener
{
	private static final long serialVersionUID = 1L;

	private IDrawerControl control;

	private TextInput nameInput;
	private PointInput tileSizeInput;

	public ModelTable()
	{
		super("Settings");

		JPanel contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(BorderFactory.createEtchedBorder());

		nameInput = new TextInput("Name", this);
		nameInput.setValue(new Text("NONE"));
		nameInput.setPrompt("Please enter a new name for this model.");
		nameInput.setToolTipText("Provide a name for this model.");

		tileSizeInput = new PointInput("Tile size", this);
		tileSizeInput.setUnit("px");
		tileSizeInput.setValue(new Point(0, 0));
		tileSizeInput.setLabels("Width", "Height");
		tileSizeInput.setPrompt("Please enter a new tile size.");
		tileSizeInput.setToolTipText("Size of one animation tile. This is the width and height per sprite in the model's images.");

		Insets insets = new Insets(0, 0, 0, 0);

		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0,
				0);
		contentPane.add(nameInput, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		contentPane.add(new JSeparator(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		contentPane.add(tileSizeInput, c);

		setEnabled(false);
		setContent(contentPane);
	}

	public void setControl(IDrawerControl control)
	{
		this.control = control;
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);

		nameInput.setEnabled(enabled);
		tileSizeInput.setEnabled(enabled);
	}

	@Override
	public void notifyValueChanged(ValueInput input)
	{
		if (input == nameInput)
		{
			control.requestSetName(nameInput.getValue());
		}
		else if (input == tileSizeInput)
		{
			control.requestSetTileSize(tileSizeInput.getValue());
		}
	}

	@Override
	public void displayName(IText_r name)
	{
		nameInput.setEnabled(name != null);
		nameInput.setValue(name);
	}

	@Override
	public void displayTileSize(IPoint_r tileSize)
	{
		tileSizeInput.setEnabled(tileSize != null);
		tileSizeInput.setValue(tileSize);
	}
}
