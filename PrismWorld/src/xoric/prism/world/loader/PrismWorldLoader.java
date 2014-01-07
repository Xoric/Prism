package xoric.prism.world.loader;

import xoric.prism.data.exceptions.PrismException;
import xoric.prism.data.exceptions.UserErrorText;
import xoric.prism.data.global.FileIndex;
import xoric.prism.data.global.Prism;
import xoric.prism.data.meta.MetaBlock;
import xoric.prism.data.meta.MetaFile;
import xoric.prism.data.meta.MetaList;
import xoric.prism.data.meta.MetaType;
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
			PrismException e = new PrismException();
			// ----
			e.user.setText(UserErrorText.LOCAL_GAME_FILE_CAUSED_PROBLEM);
			// ----
			e.code.setText("an error occured while loading animation descriptions");
			e.code.addInfo("metaBlock", MetaType.ANIM_D.toString());
			// ----
			e.addInfo("file", f.getMetaFilename());
			// ----
			throw e;
		}
	}
}
