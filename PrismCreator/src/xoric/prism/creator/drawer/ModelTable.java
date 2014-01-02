package xoric.prism.creator.drawer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import xoric.prism.data.types.Point;
import xoric.prism.data.types.Text;
import xoric.prism.swing.input.IValueInputListener;
import xoric.prism.swing.input.PointInput;
import xoric.prism.swing.input.TextInput;

class ModelTable extends JPanel implements IModelTable, IValueInputListener
{
	private static final long serialVersionUID = 1L;

	private TextInput nameInput;
	private PointInput tileSizeInput;

	private DrawerModel model;

	public ModelTable()
	{
		this.setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder());

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
		add(nameInput, c);

		c = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(new JSeparator(), c);

		c = new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, insets, 0, 0);
		add(tileSizeInput, c);

		enableControls(false);
	}

	private void enableControls(boolean b)
	{
		nameInput.setEnabled(b);
		tileSizeInput.setEnabled(b);
	}

	@Override
	public void setModel(DrawerModel model)
	{
		this.model = model;

		if (model != null)
		{
			nameInput.setValue(model.getName());
			tileSizeInput.setValue(model.getTileSize());
		}
		enableControls(model != null);
	}

	@Override
	public void notifyValueChanged()
	{
		if (model != null)
			model.setChanged();
	}
}
