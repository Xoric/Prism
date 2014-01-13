package xoric.prism.creator.drawer.control;

import java.io.File;
import java.io.IOException;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.swing.input.PrismFileDialog;

public class ExternalImageEditor
{
	private File programFile;

	public void execute(File imageFile)
	{
		if (programFile == null)
			showInput();

		if (programFile != null)
		{
			try
			{
				String[] s = { programFile.toString(), imageFile.toString() };
				Runtime.getRuntime().exec(s);
			}
			catch (IOException e0)
			{
				PrismException e = new PrismException(e0);
				e.setText("An error occured while trying to open an image in an external editing program specified by the user.");
				e.addInfo("editing program", programFile.toString());
				e.addInfo("image file", imageFile.toString());
				e.code.print();
				e.user.showMessage();
			}
		}
	}

	public void showInput()
	{
		PrismFileDialog d = new PrismFileDialog("Select program", "Select an external image editing program in order to edit sprites.");
		d.setDefaultFile(programFile);
		boolean b = d.showOpenDialog();

		if (b)
			programFile = d.getResult();
	}
}
