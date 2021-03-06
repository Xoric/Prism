package xoric.prism.creator.common.view;

import xoric.prism.data.types.IPath_r;
import xoric.prism.swing.input.OpenPathDialog;

public class DefaultNewDialog implements INewDialog
{
	private final OpenPathDialog dialog;

	public DefaultNewDialog(String dataName)
	{
		dialog = new OpenPathDialog("New " + dataName.substring(0, 1).toUpperCase() + dataName.substring(1),
				"Please choose a working directory.");
	}

	@Override
	public boolean showDialog()
	{
		return dialog.showOpenPathDialog();
	}

	@Override
	public INewDialogResult getResult()
	{
		final IPath_r path = dialog.getResult();
		return new INewDialogResult()
		{
			@Override
			public IPath_r getPath()
			{
				return path;
			}
		};
	}
}
