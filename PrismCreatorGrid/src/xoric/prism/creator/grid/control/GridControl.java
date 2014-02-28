package xoric.prism.creator.grid.control;

import java.io.IOException;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.spritelist.view.HotSpotList;
import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.grid.model.GridModel;
import xoric.prism.creator.grid.view.IGridView;
import xoric.prism.creator.grid.view.NewGridData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.IPoint_r;
import xoric.prism.data.types.IText_r;

public class GridControl implements IGridControl
{
	private final IGridView view;
	private GridModel model;

	public GridControl(IGridView view)
	{
		this.view = view;
	}

	@Override
	public void requestCreateNewProject(INewDialogResult result)
	{
		try
		{
			NewGridData d = (NewGridData) result;
			GridModel m = new GridModel(d);
			m.save();

			model = m;
			view.setModel(m);

		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "There was an error trying to save the newly created grid in the selected directory.",
					"New Grid", JOptionPane.WARNING_MESSAGE);
		}
		view.displayAll();
	}

	@Override
	public void requestOpenProject(IPath_r path)
	{
		try
		{
			GridModel m = new GridModel(path);
			m.load();

			model = m;
			view.setModel(m);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "There was an error trying to load a grid from the selected directory.", "Open Grid",
					JOptionPane.WARNING_MESSAGE);
		}
		view.displayAll();
	}

	@Override
	public void requestCloseProject()
	{
		model = null;
		view.setModel(null);
		view.displayAll();
	}

	@Override
	public void requestExit()
	{
		System.exit(0);
	}

	@Override
	public void requestRenameGrid(IText_r name)
	{
		if (model != null)
		{
			model.setName(name);
			try
			{
				model.save();
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "There was an error trying to save the grid.", "Rename Grid",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@Override
	public void requestSetSpriteSize(IPoint_r spriteSize)
	{
		if (model != null)
		{
			model.setSpriteSize(spriteSize);
			try
			{
				model.save();
			}
			catch (IOException e)
			{
				JOptionPane.showMessageDialog(null, "There was an error trying to save the grid.", "Set sprite size",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@Override
	public void requestCreateTexture()
	{
		TextureGenerator t = new TextureGenerator(model);
		t.create();
	}

	@Override
	public void requestCreateGrid()
	{
		GridGenerator g = new GridGenerator(model);
		g.create();
	}

	// IHotSpotListener:
	@Override
	public void setHotSpot(int spriteIndex, HotSpotList list)
	{
		model.setHotSpotList(spriteIndex, list);
		view.updatedHotSpots();
	}

	// IHotSpotListener:
	@Override
	public HotSpotList getHotSpotList(int spriteIndex)
	{
		return model.getHotSpotList(spriteIndex);
	}

	// IGridControl:
	@Override
	public void requestEnableHotSpots(boolean b)
	{
		model.setHotSpotsEnabled(b);
		view.updateHotSpotsEnabled();
	}
}
