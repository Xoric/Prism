package xoric.prism.creator.drawer.control;

import java.io.File;

import xoric.prism.swing.input.PrismFileDialog;

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
		PrismFileDialog d = new PrismFileDialog("Select program", "Select an external image editing program in order to edit sprites.");
		d.setDefaultFile(file);
		boolean b = d.showOpenDialog();

		if (b)
			file = d.getResult();
	}
}