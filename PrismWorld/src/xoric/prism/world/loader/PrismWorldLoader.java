package xoric.prism.world.loader;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.PrismMetaFileException;
import xoric.prism.data.global.FileIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
import xoric.prism.data.modules.ActorID;
import xoric.prism.data.modules.ErrorCode;
import xoric.prism.data.modules.ErrorID;
import xoric.prism.world.entities.AnimationIndex;

public abstract class PrismWorldLoader
{
	public static void loadAll(boolean loadAnimationDescriptions) throws PrismException
	{
		if (loadAnimationDescriptions)
			loadAnimationDescriptions();
	}

	private static void loadAnimationDescriptions() throws PrismException
	{
		MetaFile f = Prism.global.loadMetaFile(FileIndex.ANIM_D);
		MetaList metaList = f.getMetaList();
		MetaBlock metaBlock = metaList.findMetaBlock(MetaType.ANIM_D);

		boolean isOK = AnimationIndex.loadDescriptions(metaBlock);
		if (!isOK)
		{
			ErrorCode c = new ErrorCode(ActorID.LOADER, ErrorID.CORRUPT_META_BLOCK);
			PrismMetaFileException e = new PrismMetaFileException(c, f.getMetaFilename());
			e.appendInfo("block", MetaType.ANIM_D.toString());
			throw e;
		}
	}
}