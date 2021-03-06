package xoric.prism.creator.models.generators;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ModelFilter extends FileFilter
{
	public static final String dotExtension = ".md";
	public static final ModelFilter instance = new ModelFilter();

	@Override
	public boolean accept(File f)
	{
		return f.getName().toLowerCase().endsWith(dotExtension) || f.isDirectory();
	}

	@Override
	public String getDescription()
	{
		return "Model files (" + dotExtension + ")";
	}
}
