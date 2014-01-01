package xoric.prism.creator.drawer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import xoric.prism.data.Point;
import xoric.prism.data.Text;
import xoric.prism.swing.input.PointInput;
import xoric.prism.swing.input.TextInput;

class ModelTable extends JPanel
{
	private static final long serialVersionUID = 1L;

	private TextInput nameInput;
	private PointInput tileSizeInput;

	public ModelTable()
	{
		BoxLayout b = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(b);

		nameInput = new TextInput("Name");
		nameInput.setValue(new Text("New Model"));
		nameInput.setPrompt("Please enter a new name for this model.");
		nameInput.setToolTipText("Provide a name for this model.");

		tileSizeInput = new PointInput("Tile size");
		tileSizeInput.setUnit("px");
		tileSizeInput.setValue(new Point(20, 14));
		tileSizeInput.setLabels("Width", "Height");
		tileSizeInput.setPrompt("Please enter a new tile size.");
		tileSizeInput.setToolTipText("Size of one animation tile. This is the width and height of one sprite in the image.");

		add(nameInput);
		add(tileSizeInput);
	}
}
