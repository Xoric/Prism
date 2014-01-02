package xoric.prism.global;

import javax.swing.UIManager;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.global.FileIndex;
import xoric.prism.data.global.IPrismGlobal;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.IActor;
import xoric.prism.data.modules.ModuleID;
import xoric.prism.data.types.Path;

public class PrismGlobal implements IPrismGlobal, IActor
{
	private ModuleID moduleID;
	private Path dataPath;
	private FileTable fileTable;

	public PrismGlobal(ModuleID moduleID)
	{
		this.moduleID = moduleID;
	}

	public void load() throws PrismException
	{
		// set data path
		dataPath = new Path("E:/Prism/data");

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
	public ModuleID getModuleID()
	{
		return moduleID;
	}

	@Override
	public MetaFile loadMetaFile(FileIndex fi) throws PrismException
	{
		return fileTable.loadMetaFile(fi);
	}

	@Override
	public ActorID getActorID()
	{
		return ActorID.GLOBAL;
	}
}
