package xoric.prism.global;

import java.util.Properties;

import javax.swing.UIManager;

import xoric.prism.data.exceptions2.PrismException2;
import xoric.prism.data.global.FileIndex;
import xoric.prism.data.global.IPrismGlobal;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.types.IPath_r;
import xoric.prism.data.types.Path;

public class PrismGlobal implements IPrismGlobal
{
	private Path dataPath;
	private FileTable fileTable;

	public PrismGlobal()
	{
	}

	public void load() throws PrismException2
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
	public MetaFile loadMetaFile(FileIndex fi) throws PrismException2
	{
		return fileTable.loadMetaFile(fi);
	}

	@Override
	public IPath_r getDataPath()
	{
		return dataPath;
	}
}
