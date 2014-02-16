package xoric.prism.world;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.global.DevIndex;
import xoric.prism.data.global.FileTableDirectoryIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.meta.MetaBlock_in;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList_in;
import xoric.prism.data.meta.MetaType;
import xoric.prism.world.animations.AnimationIndex;

public abstract class PrismWorldLoader
{
	public static void loadAll(boolean loadAnimationDescriptions) throws PrismException
	{
		if (loadAnimationDescriptions)
			loadAnimationDevInfo();
	}

	private static void loadAnimationDevInfo() throws PrismException
	{
		MetaFile f = Prism.global.loadMetaFile(FileTableDirectoryIndex.DEV, DevIndex.ANIM_D.ordinal());
		MetaList_in metaList = f.getMetaList();
		MetaBlock_in metaBlock = metaList.claimMetaBlock(MetaType.ANIM_D);

		boolean isOK = AnimationIndex.loadDevInfoAll(metaBlock);
		if (!isOK)
		{
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("error while loading animation descriptions");
			// ----
			metaBlock.addExceptionInfoTo(e);
			// ----
			throw e;
		}
	}
}
