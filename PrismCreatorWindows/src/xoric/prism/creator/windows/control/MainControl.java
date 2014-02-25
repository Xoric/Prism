package xoric.prism.creator.windows.control;

import java.io.File;

import javax.swing.JOptionPane;

import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.creator.windows.scene.AddComponentAction;
import xoric.prism.creator.windows.scene.DeleteComponentAction;
import xoric.prism.creator.windows.scene.SceneAction;
import xoric.prism.creator.windows.view.IMainView;
import xoric.prism.creator.windows.view.NewWindowData;
import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.types.IPath_r;
import xoric.prism.develop.meta.MetaNames;
import xoric.prism.ui.UIComponent;
import xoric.prism.ui.UIIdentifier;
import xoric.prism.ui.UIWindow;

public class MainControl implements IMainControl
{
	private final IMainView view;
	private WindowModel model;

	public MainControl(IMainView view)
	{
		this.view = view;
	}

	private void initWindow()
	{
		view.setModel(model);
		view.startScene();

		if (model != null)
		{
			UIWindow w = model.getWindow();
			w.setScreenSize(view.getScreenSize());
			w.rearrange(view.getScreenRect());
		}
		view.displayTree();
	}

	@Override
	public void requestCreateNewProject(INewDialogResult result)
	{
		NewWindowData d = (NewWindowData) result;
		model = new WindowModel(d, view.getScreenSize());
		initWindow();
		requestSave();
	}

	@Override
	public void requestOpenProject(IPath_r path)
	{
		String filename = MetaNames.makeMetaBlock("window");
		File file = path.getFile(filename);

		if (!file.exists())
		{
			JOptionPane.showMessageDialog(null, "No window could be found in the given directory.", "New Window",
					JOptionPane.WARNING_MESSAGE);
		}
		else
		{
			model = new WindowModel(path, filename, view.getScreenSize());
			try
			{
				model.load();
			}
			catch (PrismException e)
			{
				model = null;
				e.code.print();
				e.user.showMessage();
			}
			initWindow();
		}
	}

	@Override
	public void requestCloseProject()
	{
		model = null;
		view.setModel(null);
		view.closeScene();
	}

	@Override
	public void requestExit()
	{
		System.exit(0);
	}

	@Override
	public void requestSave()
	{
		try
		{
			model.save();
		}
		catch (PrismException e)
		{
			e.code.print();
			e.user.showMessage();
		}
	}

	@Override
	public void onWindowModified()
	{
		model.getWindow().rearrange(view.getScreenRect());
		requestSave();
	}

	@Override
	public void requestAddComponent(UIComponent c)
	{
		model.getWindow().addComponent(c);
		requestSave();
		view.displayTree();
	}

	@Override
	public void requestAddComponent(UIIdentifier id)
	{
		SceneAction a = new AddComponentAction(id);
		view.setSceneAction(a);
	}

	@Override
	public void requestDeleteComponent()
	{
		SceneAction a = new DeleteComponentAction();
		view.setSceneAction(a);
	}

	@Override
	public void requestDeleteComponent(UIComponent c)
	{
		model.getWindow().removeComponent(c);
		requestSave();
		view.displayTree();
	}
}
