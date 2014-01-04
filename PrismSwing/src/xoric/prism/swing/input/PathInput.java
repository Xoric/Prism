package xoric.prism.swing.input;

import javax.swing.JFileChooser;

import xoric.prism.data.types.Path;

public class PathInput
{
	private static final JFileChooser fileChooser = new JFileChooser();
	static
	{
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	}

	public static Path showDialog(String dialogTitle)
	{
		Path path = null;
		fileChooser.setDialogTitle(dialogTitle);
		int i = fileChooser.showOpenDialog(null);

		if (i == JFileChooser.APPROVE_OPTION)
		{
			String d = fileChooser.getSelectedFile().toString().replace('\\', '/');
			path = new Path(d.toString());
		}
		return path;
	}
}
