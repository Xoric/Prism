package xoric.prism.creator.drawer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import xoric.prism.data.Point;
import xoric.prism.data.Text;
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
		BoxLayout b = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(b);

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

		add(nameInput);
		add(tileSizeInput);

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
