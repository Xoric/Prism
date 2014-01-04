package xoric.prism.creator.drawer.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.drawer.model.DrawerModel;
import xoric.prism.creator.drawer.view2.IDrawerView2;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;
import xoric.prism.data.types.Path;
import xoric.prism.swing.input.PathInput;

public class DrawerControl2 implements IDrawerControl2
{
	private IDrawerView2 view;
	private DrawerModel model;

	public DrawerControl2(IDrawerView2 view)
	{
		this.view = view;
	}

	public void requestNewModel(Path path)
	{
		boolean tryAgain = false;
		DrawerModel model = null;
		do
		{
			Path path = PathInput.showDialog("Choose a working directory");

			if (path != null)
			{
				model = new DrawerModel();
				try
				{
					model.initPath(path);
				}
				catch (IOException e)
				{
					model = null;
					String[] options = new String[] { "Choose another", "Cancel" };
					int res = JOptionPane.showOptionDialog(null, "An error occured while trying to write to the specified directory:\n\n",
							"New model", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					tryAgain = res == 0;
				}
			}
		}
		while (tryAgain);

		if (model != null)
		{
			// pass new DrawerModel to DrawerView
			view.setModel(model);
		}
	}

	@Override
	public void requestSetName(IText_r name)
	{
		model.setName(name);
		view.displayName(name);
	}

	@Override
	public void requestSetTileSize(IPoint_r tileSize)
	{
		model.setTileSize(tileSize);
		view.displayTileSize(tileSize);
	}
}
