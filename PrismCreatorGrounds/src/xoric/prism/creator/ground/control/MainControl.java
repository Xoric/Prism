package xoric.prism.creator.ground.control;

import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.ground.model.AllDrawableGrounds;
import xoric.prism.creator.ground.model.GroundModel;
import xoric.prism.creator.ground.view.IMainView;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.meta.MetaBlock_out;
import xoric.prism.data.meta.MetaKey;
import xoric.prism.data.meta.MetaLine_out;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Point;
import xoric.prism.develop.meta.MetaNames;
import xoric.prism.world.client.map2.DrawableGround2;
import xoric.prism.world.map.AllGrounds;
import xoric.prism.world.map2.GroundType2;

/**
 * @author XoricLee
 * @since 22.02.2014, 20:20:56
 */
public class MainControl implements IMainControl
{
	private IMainView view;
	private GroundModel model;

	public MainControl(IMainView view)
	{
		this.view = view;
	}

	// IActionControl:
	@Override
	public void requestAddGround()
	{
		try
		{
			int n = AllGrounds.list.size();

			GroundType2 g = new GroundType2(n);
			g.initialize(0, 1, 0);
			AllGrounds.list.add(g);

			int x = n % 3;
			int y = n / 3;
			DrawableGround2 d = new DrawableGround2(g, g, new Point(x, y), 0.0f);
			AllDrawableGrounds.add(d);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	private void saveGrounds()
	{
		try
		{
			MetaBlock_out mb = new MetaBlock_out(MetaType.GROUND, 0);

			for (int i = 1; i < AllGrounds.list.size(); ++i)
			{
				GroundType2 g = AllGrounds.list.get(i);
				MetaLine_out ml = new MetaLine_out(MetaKey.ITEM);
				g.appendTo(ml.getHeap());
				mb.addMetaLine(ml);
			}

			String filename = MetaNames.makeMetaBlock("grounds");
			File file = model.getPath().getFile(filename);

			FileOutputStream stream = new FileOutputStream(file);
			mb.pack(stream);
			stream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occured while trying to save:\n\n" + e.toString(), "Save grounds",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	// ISceneControl:
	@Override
	public void requestSelectGround(int index)
	{
		try
		{
			view.selectGround(index);
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	// IGroundControl:
	@Override
	public void onGroundModified()
	{
		try
		{
			AllDrawableGrounds.loadAll();
			saveGrounds();
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	// IMainMenuListener:
	@Override
	public void requestCreateNewProject(INewDialogResult result)
	{
		model = new GroundModel(result.getPath());
	}

	// IMainMenuListener:
	@Override
	public void requestOpenProject(IPath_r path)
	{
		model = new GroundModel(path);
	}

	// IMainMenuListener:
	@Override
	public void requestCloseProject()
	{
		// TODO
	}

	// IMainMenuListener:
	@Override
	public void requestExit()
	{
		System.exit(0);
	}
}
