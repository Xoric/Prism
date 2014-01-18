package xoric.prism.global;

import java.util.Properties;

import javax.swing.UIManager;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.IPrismGlobal;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;

public class PrismGlobal implements IPrismGlobal
{
	private Path dataPath;
	private FileTable fileTable;

	public void load() throws PrismException
	{
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");

		String s;
		if (os.equals("Linux"))
			s = "/home/xoric/workspace/data";
		else
			s = "E:/Prism/data";

		// set data path
		dataPath = new Path(s);

		// create and load FileTable
		fileTable = new FileTable(dataPath);
		fileTable.load("common/toc.meta");
	}

	public static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
	}

	@Override
	public MetaFile loadMetaFile(FileTableDirectoryIndex fd, int index) throws PrismException
	{
		return fileTable.loadMetaFile(fd, index);
	}

	@Override
	public IPath_r getDataPath()
	{
		return dataPath;
	}
}
