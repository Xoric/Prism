package xoric.prism.creator.drawer.generator;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ModelFilter extends FileFilter
{
	public static final String extension = ".md";
	public static final ModelFilter instance = new ModelFilter();

	@Override
	public boolean accept(File f)
	{
		return f.getName().toLowerCase().endsWith(extension) || f.isDirectory();
	}

	@Override
	public String getDescription()
	{
		return "Model files (" + extension + ")";
	}
}
