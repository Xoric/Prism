package xoric.prism.creator.drawer.control;

import java.io.File;

import xoric.prism.swing.input.OpenFileDialog;

public class ExternalImageEditor
{
	private File file;

	public File getFile()
	{
		if (file == null)
			showInput();

		return file;
	}

	public void showInput()
	{
		OpenFileDialog d = new OpenFileDialog("Select program", "Select an external image editing program in order to edit sprites.");
		d.setDefaultFile(file);
		boolean b = d.show();

		if (b)
			file = d.getResult();
	}
}
