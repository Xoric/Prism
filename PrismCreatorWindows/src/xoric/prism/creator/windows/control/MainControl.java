package xoric.prism.creator.windows.control;

import java.io.File;

import javax.swing.JOptionPane;

import xoric.prism.client.ui.UIWindow;
import xoric.prism.creator.common.view.INewDialogResult;
import xoric.prism.creator.windows.model.WindowModel;
import xoric.prism.creator.windows.view.IMainView;
import xoric.prism.creator.windows.view.NewWindowData;
import xoric.prism.data.types.IPath_r;

public class MainControl implements IMainControl
{
	private IMainView view;
	private WindowModel model;

	public MainControl(IMainView view)
	{
		this.view = view;
	}

	private void initWindow()
	{
		view.setModel(model);
		view.startScene();

		UIWindow w = model.getWindow();
		w.setScreenSize(view.getScreenSize());
		w.rearrange(view.getScreenRect());

		view.displayTree();
	}

	@Override
	public void requestCreateNewProject(INewDialogResult result)
	{
		NewWindowData d = (NewWindowData) result;
		model = new WindowModel(d, view.getScreenSize());
		initWindow();
	}

	private String findModel(IPath_r path)
	{
		File dir = new File(path.toString());
		File[] files = dir.listFiles();

		for (File f : files)
		{
			String s = f.toString();
			int i = s.lastIndexOf('.');
			if (i > 0)
			{
				String extension = s.substring(i + 1).toLowerCase();
				if (extension.equals("wn"))
					return f.getName();
			}
		}
		return null;
	}

	@Override
	public void requestOpenProject(IPath_r path)
	{
		String filename = findModel(path);
		if (filename != null)
		{
			model = new WindowModel(path, filename, view.getScreenSize());
			initWindow();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No window could be found in the given directory.", "New Window",
					JOptionPane.WARNING_MESSAGE);
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
		//		model.save();
	}

	@Override
	public void onWindowModified()
	{
		model.getWindow().rearrange(view.getScreenRect());
		//	TODO	save();
	}
}
